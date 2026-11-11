package ar.edu.unq.po2.unqtravel.EstadoYReserva;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Estados.Estado;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

class EstadoTest {

    @Test
    void estadoBaseRechazaOperacionesNoDefinidasYNoActivaNotificaciones() {
        Estado estado = new Estado() {};
        Reserva reserva = mock(Reserva.class);
        Item item = mock(Item.class);

        assertFalse(estado.debeNotificarEmail());
        assertFalse(estado.debeNotificarClub());
        assertFalse(estado.debeNotificarFactura());
        assertThrows(Excepciones.class, () -> estado.agregarItem(reserva, item));
        assertThrows(Excepciones.class, () -> estado.quitarItem(reserva, item));
        assertThrows(Excepciones.class, () -> estado.checkIn(reserva));
        assertThrows(Excepciones.class, () -> estado.iniciarViaje(reserva));
        assertThrows(Excepciones.class, () -> estado.finalizar(reserva));
        assertThrows(Excepciones.class, () -> estado.cancelar(reserva));
    }
}
