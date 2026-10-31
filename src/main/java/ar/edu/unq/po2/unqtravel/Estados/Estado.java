package ar.edu.unq.po2.unqtravel.catalogo.Estados;

import ar.edu.unq.po2.unqtravel.catalogo.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.catalogo.Item;
import java.util.Objects;

public class Estado {
    protected Reserva reserva;

    public Estado(Reserva reserva) {
        this.reserva = Objects.requireNonNull(reserva, "La reserva no puede ser null");
    }

    public void agregarItem(Item item) {
        throw transicionInvalida("agregar items");
    }

    public void quitarItem(Item item) {
        throw transicionInvalida("quitar items");
    }

    public void confirmar() {
        throw transicionInvalida("confirmar");
    }

    public void checkIn() {
        throw transicionInvalida("hacer check-in");
    }

    public void iniciarViaje() {
        throw transicionInvalida("iniciar el viaje");
    }

    public void finalizar() {
        throw transicionInvalida("finalizar");
    }

    public void cancelar() {
        throw transicionInvalida("cancelar");
    }

    private IllegalStateException transicionInvalida(String accion) {
        return new IllegalStateException("No se puede " + accion + " desde " + getClass().getSimpleName());
    }
}
