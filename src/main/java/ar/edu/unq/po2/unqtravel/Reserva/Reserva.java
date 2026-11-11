package ar.edu.unq.po2.unqtravel.Reserva;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Estados.*;
import ar.edu.unq.po2.unqtravel.Observer.ObserverReserva;

public class Reserva {
    private Estado estado;
    private double montoReembolso;
    private final List<Item> items = new ArrayList<>();
    private final List<ObserverReserva> observers = new ArrayList<>();
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Reserva(LocalDate fechaInicio, LocalDate fechaFin) {
        this.estado = new SolicitudPendiente();
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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
        this.items.add(Objects.requireNonNull(item, "El item no puede ser null"));
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
        items.add(Objects.requireNonNull(item, "El item no puede ser null"));
    }

    public void quitarItemPendiente(Item item) {
        items.remove(item);
    }

    public void cambiarEstado(Estado estado) {
        this.estado = Objects.requireNonNull(estado, "El estado no puede ser null");
        this.notificarObservers();
    }

    public void confirmar() {
        this.estado.confirmar(this);
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
            throw new Excepciones("No se pudo bloquear el cupo disponible para todos los items. Se han revertido los cambios.");
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
            throw new Excepciones("No se pudo liberar el cupo para todos los items. Se han revertido los cambios.");
        }
    }

    public double getCostoServicios() {
        return this.items.stream().mapToDouble(i -> i.getPrecioFinal()).sum();
    }

    public double getCostoTasas() {
        return 0.0; //completar modulo 2.3
    }

    public void generarVoucherDeReembolso(double monto) {
        this.montoReembolso = monto;
    }
    
    //Metodos para la parte de traslado
    public float getValorTotal() {
    	return 0;
    }
    
    public int getCantidadPasajeros() {
    	return 0;
    }
    
    //Metodos para la parte de formato
    
    public double getCostoItem() {
    	return 0.0; 
    }
    
    public LocalDate getFechaInicio() {
    	return fechaInicio;
    }
    
    public LocalDate getFechaFin() {
    	return fechaFin;
    }
    
    
    
    
    
    

}
