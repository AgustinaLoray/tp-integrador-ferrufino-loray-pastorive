package ar.edu.unq.po2.unqtravel.catalogo.Reserva;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import ar.edu.unq.po2.unqtravel.catalogo.Item;
import ar.edu.unq.po2.unqtravel.catalogo.Estados.*;

public class Reserva {
    private Estado estado;
    private final List<Item> items = new ArrayList<>();

    public Reserva() {
        this.estado = new SolicitudPendiente(this);
    }

    public List<Item> getItems() {
        return List.copyOf(items);
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
        List<Item> reservados = new ArrayList<>();
        try {
            for (Item item : items) {
                item.decrementarCupo();
                reservados.add(item);
            }
        } catch (RuntimeException error) {
            for (int i = reservados.size() - 1; i >= 0; i--) {
                reservados.get(i).incrementarCupo();
            }
            throw error;
        }
    }

    public void liberarCupos() {
        for (Item item : items) {
            item.incrementarCupo();
        }
    }

}
