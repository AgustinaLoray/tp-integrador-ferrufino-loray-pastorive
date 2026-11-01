package ar.edu.unq.po2.unqtravel.Estados;

import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

import java.util.Objects;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class Estado {
    protected Reserva reserva;

    public Estado(Reserva reserva) {
        this.reserva = Objects.requireNonNull(reserva, "La reserva no puede ser null");
    }

    public void agregarItem(Item item) {
        throw new UnsupportedOperationException("No se puede agregar items");
    }

    public void quitarItem(Item item) {
        throw new UnsupportedOperationException("No se puede quitar items");
    }

    public void confirmar() {
        throw new UnsupportedOperationException("No se puede confirmar la reserva en el estado actual.");
    }

    public void checkIn() {
        throw new UnsupportedOperationException("No se puede realizar el check-in en el estado actual.");
    }

    public void iniciarViaje() {
        throw new UnsupportedOperationException("No se puede iniciar el viaje en el estado actual.");
    }

    public void finalizar() {
        throw new UnsupportedOperationException("No se puede finalizar la reserva en el estado actual.");
    }

    public void cancelar() {
        throw new UnsupportedOperationException("No se puede cancelar la reserva en el estado actual.");
    }
}
