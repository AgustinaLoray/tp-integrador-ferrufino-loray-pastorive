package ar.edu.unq.po2.unqtravel.Reserva;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Estados.*;
import ar.edu.unq.po2.unqtravel.Observer.ObserverReserva;

public class Reserva {
    private Estado estado;
    private double montoReembolso;
    private final List<Item> items = new ArrayList<>();
    private final List<ObserverReserva> observers = new ArrayList<>();

    public Reserva() {
        this.estado = new SolicitudPendiente(this);
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
            throw new IllegalArgumentException("El item no puede ser null");
        } else {
            this.items.add(item);
        }
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
        this.estado.agregarItem(item);
    }

    public void quitarItem(Item item) {
        this.estado.quitarItem(item);
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
        this.estado.confirmar();
    }

    public void checkIn() {
        this.estado.checkIn();
    }

    public void iniciarViaje() {
        this.estado.iniciarViaje();
    }

    public void finalizar() {
        this.estado.finalizar();
    }

    public void cancelar() {
        this.estado.cancelar();
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
            throw new IllegalStateException("No se pudo bloquear el cupo disponible para todos los items. Se han revertido los cambios.", error);
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
            throw new IllegalStateException("No se pudo liberar el cupo para todos los items. Se han revertido los cambios.", error);
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

}
