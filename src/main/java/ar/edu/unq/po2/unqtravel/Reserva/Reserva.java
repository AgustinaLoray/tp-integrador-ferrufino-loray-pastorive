package ar.edu.unq.po2.unqtravel.Reserva;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Estados.*;
import ar.edu.unq.po2.unqtravel.Observer.ObserverReserva;
import ar.edu.unq.po2.unqtravel.Traslado.MetodoTraslado;

public class Reserva {
    private Estado estado;
    private double montoReembolso;
    private final List<Item> items = new ArrayList<>();
    private final List<ObserverReserva> observers = new ArrayList<>();
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cantidadPasajeros;
    private MetodoTraslado metodoTraslado;
    private List<DetalleReserva> detallesReserva = List.of();
    private boolean pagoConfirmado;

    public Reserva(LocalDate fechaInicio, LocalDate fechaFin, int cantidadPasajeros, MetodoTraslado metodoTraslado) {

        if (fechaInicio == null || fechaFin == null) {
            throw new Excepciones("Las fechas no pueden ser null");
        }

        if (fechaFin.isBefore(fechaInicio)) {
            throw new Excepciones("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;

        if (cantidadPasajeros <= 0) {
            throw new Excepciones("La cantidad de pasajeros debe ser un número positivo");
        }
        if (metodoTraslado == null) {
            throw new Excepciones("El método de traslado no puede ser null");
        }

        this.cantidadPasajeros = cantidadPasajeros;
        this.metodoTraslado = metodoTraslado;
        this.estado = new SolicitudPendiente();
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void agregarObserver(ObserverReserva o) {
        if (o != null) {
            this.observers.add(o);
        }
    }

    public void notificarObservers() {
        for (ObserverReserva o : this.observers) {
            o.notificar(this);
        }
    }

    public void quitarObservador(ObserverReserva o) {
        this.observers.remove(o);
    }

    public void agregarItemDirecto(Item item) {
        if (item == null) {
            throw new Excepciones("El item no puede ser null");
        }
        this.items.add(item);
    }

    public void quitarItemDirecto(Item item) {
        this.items.remove(item);
    }

    public List<Item> getItems() {
        return List.copyOf(this.items);
    }

    public double getReembolso(){
        return this.montoReembolso;
    }

    public void agregarItem(Item item) {
        this.estado.agregarItem(this, item);
    }

    public void quitarItem(Item item) {
        this.estado.quitarItem(this, item);
    }

    public void agregarItemPendiente(Item item) {
        if (item == null) {
            throw new Excepciones("El item no puede ser null");
        }
        items.add(item);
    }

    public void quitarItemPendiente(Item item) {
        items.remove(item);
    }

    public void cambiarEstado(Estado estado) {
        if (estado == null) {
            throw new Excepciones("El estado no puede ser null");
        }
        Estado nuevoEstado = estado;
        if (nuevoEstado instanceof Confirmada && !this.pagoConfirmado) {
            throw new Excepciones("No se puede confirmar una reserva sin un pago exitoso.");
        }
        this.estado = nuevoEstado;
        this.notificarObservers();
    }

    public void checkIn() {
        this.estado.checkIn(this);
    }

    public void iniciarViaje() {
        this.estado.iniciarViaje(this);
    }

    public void finalizar() {
        this.estado.finalizar(this);
    }

    public void cancelar() {
        this.estado.cancelar(this);
    }

    public void bloquearCupoDisponible() {
        List<Item> itemBloqueados = new ArrayList<>();

        try {
            for (Item item : items) {
                item.decrementarCupo();
                itemBloqueados.add(item);
            }
        } catch (RuntimeException error) {
            for (int i = itemBloqueados.size() - 1; i >= 0; i--) {
                itemBloqueados.get(i).incrementarCupo();
            }
            throw new Excepciones(
                    "No se pudo bloquear el cupo disponible para todos los items. Se han revertido los cambios.",
                    error);
        }
    }

    public void liberarCupos() {
        List<Item> itemLiberados = new ArrayList<>();

        try {
            for (Item item : items) {
                item.incrementarCupo();
                itemLiberados.add(item);
            }
        } catch (RuntimeException error) {
            for (int i = itemLiberados.size() - 1; i >= 0; i--) {
                itemLiberados.get(i).decrementarCupo();
            }
            throw new Excepciones(
                    "No se pudo liberar el cupo para todos los items. Se han revertido los cambios.",
                    error);
        }
    }

    public double getCostoServicios() {
        if (this.pagoConfirmado) {
            return this.detallesReserva.stream()
                    .mapToDouble(DetalleReserva::getImporteTotal)
                    .sum();
        }
        return this.items.stream().mapToDouble(i -> i.getPrecioFinal()).sum();
    }

    public double getCostoTasas() {
        return this.metodoTraslado.calcularCosto(this);
    }

    public void generarVoucherDeReembolso(double monto) {
        this.montoReembolso = monto;
    }
    
    //Metodos para la parte de traslado
    public float getValorTotal() {
    	return (float) (this.getCostoServicios() + this.getCostoTasas());
    }
    
    List<DetalleReserva> prepararCotizacion() {
        if (!(this.estado instanceof SolicitudPendiente)) {
            throw new Excepciones("Solo se puede cotizar una reserva pendiente.");
        }
        if (this.items.isEmpty()) {
            throw new Excepciones("No se puede confirmar una reserva sin items.");
        }
        return this.items.stream()
                .map(item -> new DetalleReserva(item, item.getPrecioFinal(), 1))
                .toList();
    }

    double calcularTotalCotizado(List<DetalleReserva> cotizacion) {
        double costoServiciosCotizados = cotizacion.stream()
                .mapToDouble(DetalleReserva::getImporteTotal)
                .sum();
        return costoServiciosCotizados + this.getCostoTasas();
    }

    void registrarPagoYConfirmar(List<DetalleReserva> cotizacion) {
        if (!(this.estado instanceof SolicitudPendiente)) {
            throw new Excepciones("Solo se puede confirmar una reserva pendiente.");
        }
        this.detallesReserva = List.copyOf(cotizacion);
        this.pagoConfirmado = true;
        this.cambiarEstado(new Confirmada());
    }

    public List<DetalleReserva> getDetallesReserva() {
        return List.copyOf(this.detallesReserva);
    }

    public boolean tienePagoConfirmado() {
        return this.pagoConfirmado;
    }

    public double getCostoItem(Item item) {
        if (item == null) {
            throw new Excepciones("El item no puede ser null");
        }
        List<DetalleReserva> detallesDelItem = this.detallesReserva.stream()
                .filter(detalle -> detalle.getItem() == item)
                .toList();
        if (detallesDelItem.isEmpty()) {
            throw new Excepciones("La reserva no tiene un precio cobrado registrado para este item.");
        }
        int cantidadTotal = detallesDelItem.stream()
                .mapToInt(DetalleReserva::getCantidad)
                .sum();
        double importeTotal = detallesDelItem.stream()
                .mapToDouble(DetalleReserva::getImporteTotal)
                .sum();
        return importeTotal / cantidadTotal;
    }
    
    public LocalDate getFechaInicio() {
    	return fechaInicio;
    }
    
    public LocalDate getFechaFin() {
    	return fechaFin;
    }
    
    public int getCantidadPasajeros() {
    	return cantidadPasajeros;
    }
}
