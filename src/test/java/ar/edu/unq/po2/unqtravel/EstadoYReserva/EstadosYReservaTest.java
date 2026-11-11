package ar.edu.unq.po2.unqtravel.EstadoYReserva;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Estados.*;
import ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico.PaqueteTuristico;
import ar.edu.unq.po2.unqtravel.Catalogo.ServicioIndividual.ServicioIndividual;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import ar.edu.unq.po2.unqtravel.Reserva.ConfirmadorReserva;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Traslado.PuntoEncuentro;
import ar.edu.unq.po2.unqtravel.pagos.MetodoDePago;

public class EstadosYReservaTest {
    private Reserva reserva;
    private PaqueteTuristico vuelo;
    private ServicioIndividual hotel;
    private final ConfirmadorReserva confirmador = new ConfirmadorReserva();
    private final MetodoDePago pagoExitoso = new MetodoDePago() {
        @Override
        protected void validarDatos(double monto) {}

        @Override
        protected void reservarFondos(double monto) {}

        @Override
        protected void ejecutarTransaccion(double monto) {}
    };

    @BeforeEach
    public void setUp() {
        reserva = new Reserva(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 17),
                1,
                new PuntoEncuentro(0));

        vuelo = new PaqueteTuristico (
            "Vuelo Buenos Aires - Mendoza", 
            "Aerolineas Argentinas", 
            0.0, null, 0
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

        confirmarReserva();
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
        confirmarReserva();
        assertEquals(19, hotel.getCupos());
    }

    @Test 
    public void liberaElCupoDisponible() {
        assertInstanceOf(SolicitudPendiente.class, reserva.getEstado());
        reserva.agregarItem(hotel);
        confirmarReserva();
        reserva.cancelar();
        assertEquals(20, hotel.getCupos());
    }

    @Test 
    public void liberaCupoYReembolso() {
        reserva.agregarItem(hotel);
        confirmarReserva();
        reserva.checkIn();
        reserva.cancelar();
        assertInstanceOf(Cancelada.class, reserva.getEstado());
        assertEquals(20, hotel.getCupos());
        assertEquals(150000.0, reserva.getReembolso());
    }

    @Test
    public void cancelarDuranteElViajeLiberaCupoYReembolsaLosServicios() {
        reserva.agregarItem(hotel);
        confirmarReserva();
        reserva.cambiarEstado(new EnCurso());

        reserva.cancelar();

        assertInstanceOf(Cancelada.class, reserva.getEstado());
        assertEquals(20, hotel.getCupos());
        assertEquals(150000.0, reserva.getReembolso());
    }

    @Test
    public void rechazaTransicionesNoPermitidas() {
        assertThrows(Excepciones.class, () -> reserva.checkIn());

        reserva.agregarItem(hotel);
        confirmarReserva();
        assertThrows(Excepciones.class, this::confirmarReserva);

        reserva.checkIn();
        reserva.iniciarViaje();
        reserva.finalizar();
        assertThrows(Excepciones.class, () -> reserva.cancelar());
    }

    @Test
    public void lasColeccionesDeReservaSonInmodificablesYRechazaItemsNulos() {
        assertThrows(UnsupportedOperationException.class, () -> reserva.getItems().add(vuelo));
        assertThrows(NullPointerException.class, () -> reserva.agregarItem(null));
        assertTrue(reserva.getItems().isEmpty());
    }

    @Test
    public void revierteLosCuposSiFallaLaConfirmacionDeUnItem() {
        ServicioIndividual sinCupo = new ServicioIndividual(
                2, "Sin cupo", 0, "Proveedor", "Alojamiento", "", 100.0, 0.0);
        reserva.agregarItem(hotel);
        reserva.agregarItem(sinCupo);

        assertThrows(Excepciones.class, this::confirmarReserva);

        assertEquals(20, hotel.getCupos());
        assertEquals(0, sinCupo.getCupos());
        assertInstanceOf(SolicitudPendiente.class, reserva.getEstado());
    }

    private void confirmarReserva() {
        confirmador.confirmar(reserva, pagoExitoso);
    }
}
