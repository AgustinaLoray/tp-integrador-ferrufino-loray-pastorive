package ar.edu.unq.po2.unqtravel.EstadoYReserva;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Estados.*;
import ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico.PaqueteTuristico;
import ar.edu.unq.po2.unqtravel.Catalogo.ServicioIndividual.ServicioIndividual;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class EstadosYReservaTest {
    private Reserva reserva;
    private PaqueteTuristico vuelo;
    private ServicioIndividual hotel;

    @BeforeEach
    public void setUp() {
        reserva = new Reserva(); // se inicializa en solicitud pendiente

        vuelo = new PaqueteTuristico (
            "Vuelo Buenos Aires - Mendoza", 
            "Aerolineas Argentinas", 
            0.0
        );

       hotel = new ServicioIndividual(
            1, 
            "Hotel Puerto Madero",
            20,
            "Hilton",
            "Alojamiento",
            "Desayuno incluido",
            150000.0,
            0.0
        );
    }

    @Test
    public void testReservaIniciada() {
        assertInstanceOf(SolicitudPendiente.class, reserva.getEstado());
    }

    @Test 
    public void agregarYSacarItem() {
        reserva.agregarItem(vuelo);
        assertEquals(1, reserva.getItems().size());
        reserva.quitarItem(vuelo);
        assertEquals(0,reserva.getItems().size());
    }

    @Test 
    public void testReservaExitosa() { //desde solicitudPendiente a finalizada
        reserva.agregarItem(vuelo);

        reserva.confirmar();
        assertInstanceOf(Confirmada.class, reserva.getEstado());

        reserva.checkIn();
        assertInstanceOf(CheckIn.class, reserva.getEstado());

        reserva.iniciarViaje();
        assertInstanceOf(EnCurso.class, reserva.getEstado());

        reserva.finalizar();
        assertInstanceOf(Finalizada.class, reserva.getEstado());
    }

    @Test 
    public void bloquaElCupoDisponible() {
        assertInstanceOf(SolicitudPendiente.class, reserva.getEstado());
        reserva.agregarItem(hotel);
        reserva.confirmar();
        assertEquals(19, hotel.getCuposDisponibles());
    }

    @Test 
    public void liberaElCupoDisponible() {
        assertInstanceOf(SolicitudPendiente.class, reserva.getEstado());
        reserva.agregarItem(hotel);
        reserva.confirmar();
        reserva.cancelar();
        assertEquals(20, hotel.getCuposDisponibles());
    }

    @Test 
    public void liberaCupoYReembolso() {
        reserva.agregarItem(hotel);
        reserva.confirmar();
        reserva.checkIn();
        reserva.cancelar();
        assertInstanceOf(Cancelada.class, reserva.getEstado());
        assertEquals(20, hotel.getCuposDisponibles());
        assertEquals(150000.0, reserva.getReembolso());
    }

    @Test
    public void rechazaTransicionesNoPermitidas() {
        assertThrows(UnsupportedOperationException.class, () -> reserva.checkIn());

        reserva.confirmar();
        assertThrows(UnsupportedOperationException.class, () -> reserva.confirmar());

        reserva.checkIn();
        reserva.iniciarViaje();
        reserva.finalizar();
        assertThrows(UnsupportedOperationException.class, () -> reserva.cancelar());
    }

    @Test
    public void lasColeccionesDeReservaSonInmodificablesYRechazaItemsNulos() {
        assertThrows(UnsupportedOperationException.class, () -> reserva.getItems().add(vuelo));
        assertThrows(IllegalArgumentException.class, () -> reserva.agregarItem(null));
        assertTrue(reserva.getItems().isEmpty());
    }

    @Test
    public void revierteLosCuposSiFallaLaConfirmacionDeUnItem() {
        ServicioIndividual sinCupo = new ServicioIndividual(
                2, "Sin cupo", 0, "Proveedor", "Alojamiento", "", 100.0, 0.0);
        reserva.agregarItem(hotel);
        reserva.agregarItem(sinCupo);

        IllegalStateException error = assertThrows(IllegalStateException.class, reserva::confirmar);

        assertInstanceOf(IllegalStateException.class, error.getCause());
        assertEquals(20, hotel.getCuposDisponibles());
        assertEquals(0, sinCupo.getCuposDisponibles());
        assertInstanceOf(SolicitudPendiente.class, reserva.getEstado());
    }
}
