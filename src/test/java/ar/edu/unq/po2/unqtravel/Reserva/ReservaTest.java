package ar.edu.unq.po2.unqtravel.Reserva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Estados.Cancelada;
import ar.edu.unq.po2.unqtravel.Estados.Confirmada;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import ar.edu.unq.po2.unqtravel.Observer.ObserverReserva;
import ar.edu.unq.po2.unqtravel.Traslado.PuntoEncuentro;

class ReservaTest {

    @Test
    void validaFechasPasajerosYMetodoDeTrasladoAlConstruir() {
        LocalDate inicio = LocalDate.of(2026, 11, 10);
        LocalDate fin = LocalDate.of(2026, 11, 12);

        assertThrows(Excepciones.class, () -> new Reserva(null, fin, 1, new PuntoEncuentro(0)));
        assertThrows(Excepciones.class, () -> new Reserva(inicio, null, 1, new PuntoEncuentro(0)));
        assertThrows(Excepciones.class,
                () -> new Reserva(fin, inicio, 1, new PuntoEncuentro(0)));
        assertThrows(Excepciones.class,
                () -> new Reserva(inicio, fin, 0, new PuntoEncuentro(0)));
        assertThrows(Excepciones.class, () -> new Reserva(inicio, fin, 1, null));
    }

    @Test
    void notificaObservadoresRegistradosYPermiteQuitarlos() {
        Reserva reserva = nuevaReserva();
        ObserverReserva observer = mock(ObserverReserva.class);

        reserva.agregarObserver(null);
        reserva.agregarObserver(observer);
        reserva.notificarObservers();
        reserva.quitarObservador(observer);
        reserva.notificarObservers();

        verify(observer).notificar(reserva);
    }

    @Test
    void permiteCancelarUnaSolicitudPendienteYRechazaConfirmarSinPago() {
        Reserva reserva = nuevaReserva();
        Item item = mock(Item.class);

        assertThrows(Excepciones.class, () -> reserva.cambiarEstado(new Confirmada()));
        assertThrows(Excepciones.class, () -> reserva.cambiarEstado(null));
        assertThrows(Excepciones.class, () -> reserva.agregarItemDirecto(null));
        assertThrows(Excepciones.class, () -> reserva.agregarItemPendiente(null));
        reserva.agregarItemPendiente(item);
        reserva.quitarItemPendiente(item);
        reserva.cancelar();

        assertInstanceOf(Cancelada.class, reserva.getEstado());
        assertEquals(0, reserva.getItems().size());
        assertEquals(0.0, reserva.getReembolso());
    }

    @Test
    void liberarCuposRevierteLiberacionesParcialesSiUnItemFalla() {
        Reserva reserva = nuevaReserva();
        Item primerItem = mock(Item.class);
        Item conFallo = mock(Item.class);
        when(primerItem.getPrecioFinal()).thenReturn(10.0);
        org.mockito.Mockito.doThrow(new IllegalStateException("No reservado"))
                .when(conFallo).incrementarCupo();
        reserva.agregarItemDirecto(primerItem);
        reserva.agregarItemDirecto(conFallo);

        assertThrows(Excepciones.class, reserva::liberarCupos);

        verify(primerItem).incrementarCupo();
        verify(primerItem).decrementarCupo();
    }

    @Test
    void costoItemValidaArgumentosYRechazaItemsSinPrecioCobrado() {
        Reserva reserva = nuevaReserva();
        Item item = mock(Item.class);

        assertThrows(Excepciones.class, () -> reserva.getCostoItem(null));
        assertThrows(Excepciones.class, () -> reserva.getCostoItem(item));
        assertEquals(0.0, reserva.getCostoServicios());
        assertEquals(0.0f, reserva.getValorTotal());
        assertEquals(1, reserva.getCantidadPasajeros());
        verify(item, never()).getPrecioFinal();
    }

    @Test
    void noPermiteVolverACotizarNiRegistrarPagoEnUnaReservaConfirmada() {
        Reserva reserva = nuevaReserva();
        Item item = mock(Item.class);
        when(item.getPrecioFinal()).thenReturn(15.0);
        reserva.agregarItem(item);
        reserva.registrarPagoYConfirmar(List.of(new DetalleReserva(item, 15.0, 1)));

        assertThrows(Excepciones.class, reserva::prepararCotizacion);
        assertThrows(Excepciones.class, () -> reserva.registrarPagoYConfirmar(List.of()));
        assertEquals(15.0, reserva.getCostoItem(item));
        assertEquals(15.0, reserva.getCostoServicios());
    }

    private Reserva nuevaReserva() {
        return new Reserva(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12),
                1,
                new PuntoEncuentro(0));
    }
}
