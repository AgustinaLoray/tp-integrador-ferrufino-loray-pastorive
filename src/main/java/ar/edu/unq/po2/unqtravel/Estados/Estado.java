package ar.edu.unq.po2.unqtravel.Estados;

import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import java.util.Objects;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;

public abstract class Estado {
    
    public boolean debeNotificarEmail() {
        return false;
    }

    public boolean debeNotificarClub() {
        return false;
    }

    public boolean debeNotificarFactura() {
        return false;
    }

    public void agregarItem(Reserva reserva, Item item) {
        throw new Excepciones("No se puede agregar items");
    }

    public void quitarItem(Reserva reserva, Item item) {
        throw new Excepciones("No se puede quitar items");
    }

    public void confirmar(Reserva reserva) {
        throw new Excepciones("No se puede confirmar la reserva en el estado actual.");
    }

    public void checkIn(Reserva reserva) {
        throw new Excepciones("No se puede realizar el check-in en el estado actual.");
    }

    public void iniciarViaje(Reserva reserva) {
        throw new Excepciones("No se puede iniciar el viaje en el estado actual.");
    }

    public void finalizar(Reserva reserva) {
        throw new Excepciones("No se puede finalizar la reserva en el estado actual.");
    }

    public void cancelar(Reserva reserva) {
        throw new Excepciones("No se puede cancelar la reserva en el estado actual.");
    }
}
