package ar.edu.unq.po2.unqtravel.EstadoYReserva;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Estados.*;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico.PaqueteTuristico;

import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class EstadosYReserva {
    private Reserva reserva;
    private Item item;

    @BeforeEach
    public void setUp() {
        reserva = new Reserva(); // se inicializa en solicitud pendiente
        item = new PaqueteTuristico (
            "Vuelo Buenos Aires - Mendoza", 
            "Aerolineas Argentinas", 
            0.0
        );
    }

    @Test
    public void testReservaIniciada() {
        assertInstanceOf(SolicitudPendiente.class, reserva.getEstado());
    }

    @Test 
    public void agregarYSacarItem() {
        reserva.agregarItem(item);
        assertEquals(1, reserva.getItems().size());
        reserva.quitarItem(item);
        assertEquals(0,reserva.getItems().size());
    }

    @Test 
    public void testReservaExitosa() { //desde solicitudPendiente a finalizada
        reserva.agregarItem(item);

        reserva.confirmar();
        assertInstanceOf(Confirmada.class, reserva.getEstado());

        reserva.checkIn();
        assertInstanceOf(CheckIn.class, reserva.getEstado());

        reserva.iniciarViaje();
        assertInstanceOf(EnCurso.class, reserva.getEstado());

        reserva.finalizar();
        assertInstanceOf(Finalizada.class, reserva.getEstado());
    }

    //faltan mas casos
}
