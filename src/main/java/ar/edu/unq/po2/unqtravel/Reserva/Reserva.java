package ar.edu.unq.po2.unqtravel.Reserva;
import java.util.List;
import java.util.ArrayList;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Estados.*;

public class Reserva {
    private Estado estado;
    private List<Item> items = new ArrayList<>();
    //faltan cosas del uml 

    public Reserva() {
        this.estado = new SolicitudPendiente(this); // se crea siempre en solicitud pendiente
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void agregarItemDirecto(Item item) {
        this.items.add(item);
    }

    public void quitarItemDirecto(Item item) {
        this.items.remove(item);
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void agregarItem(Item item) {
        this.estado.agregarItem(item);
    }

    public void quitarItem(Item item) {
        this.estado.quitarItem(item);
    }

    public void cambiarEstado(Estado estado) {
        this.estado = estado;
        // notificar observes, mas adelante
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

    public void bloquearCupoDisponible() { //para cada item de la reserva, decrementa el cupo si el estado de la reserva es CONFIRMADA
        for (Item item : items) { 
            //item.decrementarCupo(); 
        }
    } 

    public void liberarCupos() {} 
    public void generarReembolso(double monto) {} 

}
