package ar.edu.unq.po2.unqtravel.EstadoYReserva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Estados.Confirmada;
import ar.edu.unq.po2.unqtravel.Estados.SolicitudPendiente;
import ar.edu.unq.po2.unqtravel.Reserva.ConfirmadorReserva;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Traslado.PuntoEncuentro;
import ar.edu.unq.po2.unqtravel.pagos.MetodoDePago;
import ar.edu.unq.po2.unqtravel.pagos.NotificacionPagoException;

class ConfirmadorReservaTest {

    @Test
    void guardaElPrecioCotizadoYConfirmaDespuesDelPago() {
        Item item = mock(Item.class);
        when(item.getPrecioFinal()).thenReturn(100.0, 250.0);
        Reserva reserva = nuevaReserva();
        reserva.agregarItem(item);

        PagoDePrueba pago = new PagoDePrueba();
        new ConfirmadorReserva().confirmar(reserva, pago);

        assertEquals(100.0, pago.getMontoProcesado());
        assertEquals(100.0, reserva.getCostoItem(item));
        assertEquals(100.0, reserva.getCostoServicios());
        assertInstanceOf(Confirmada.class, reserva.getEstado());
    }

    @Test
    void siElPagoFallaLiberaLosCuposYDejaLaReservaPendiente() {
        Item item = mock(Item.class);
        when(item.getPrecioFinal()).thenReturn(100.0);
        Reserva reserva = nuevaReserva();
        reserva.agregarItem(item);
        MetodoDePago pagoFallido = new PagoDePrueba() {
            @Override
            protected void ejecutarTransaccion(double monto) {
                throw new IllegalStateException("Pago rechazado");
            }
        };

        assertThrows(IllegalStateException.class,
                () -> new ConfirmadorReserva().confirmar(reserva, pagoFallido));

        org.mockito.Mockito.verify(item).decrementarCupo();
        org.mockito.Mockito.verify(item).incrementarCupo();
        assertInstanceOf(SolicitudPendiente.class, reserva.getEstado());
        assertEquals(0, reserva.getDetallesReserva().size());
    }

    @Test
    void siFallaLaNotificacionMantieneLaReservaConfirmadaPorqueElPagoSeEjecuto() {
        Item item = mock(Item.class);
        when(item.getPrecioFinal()).thenReturn(100.0);
        Reserva reserva = nuevaReserva();
        reserva.agregarItem(item);
        MetodoDePago pagoConNotificacionFallida = new PagoDePrueba() {
            @Override
            protected void notificarResultado() {
                throw new IllegalStateException("No se pudo registrar el comprobante");
            }
        };

        assertThrows(NotificacionPagoException.class,
                () -> new ConfirmadorReserva().confirmar(reserva, pagoConNotificacionFallida));

        assertInstanceOf(Confirmada.class, reserva.getEstado());
        assertEquals(100.0, reserva.getCostoItem(item));
        org.mockito.Mockito.verify(item).decrementarCupo();
        org.mockito.Mockito.verify(item, org.mockito.Mockito.never()).incrementarCupo();
    }

    private Reserva nuevaReserva() {
        return new Reserva(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12),
                1,
                new PuntoEncuentro(0));
    }

    private static class PagoDePrueba extends MetodoDePago {
        private double montoProcesado;

        @Override
        protected void validarDatos(double monto) {
            this.montoProcesado = monto;
        }

        @Override
        protected void reservarFondos(double monto) {}

        @Override
        protected void ejecutarTransaccion(double monto) {}

        double getMontoProcesado() {
            return this.montoProcesado;
        }
    }
}
