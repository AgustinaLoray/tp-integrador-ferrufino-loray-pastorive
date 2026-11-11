package ar.edu.unq.po2.unqtravel.Reserva;

import java.util.List;
import java.util.Objects;

import ar.edu.unq.po2.unqtravel.pagos.MetodoDePago;
import ar.edu.unq.po2.unqtravel.pagos.NotificacionPagoException;

public class ConfirmadorReserva {

    public void confirmar(Reserva reserva, MetodoDePago metodoDePago) {
        Objects.requireNonNull(reserva, "La reserva no puede ser null");
        Objects.requireNonNull(metodoDePago, "El método de pago no puede ser null");

        List<DetalleReserva> cotizacion = reserva.prepararCotizacion();
        double monto = reserva.calcularTotalCotizado(cotizacion);
        reserva.bloquearCupoDisponible();

        try {
            metodoDePago.procesarPago(monto);
        } catch (NotificacionPagoException error) {
            confirmarPagoConNotificacionFallida(reserva, cotizacion, error);
            throw error;
        } catch (RuntimeException error) {
            liberarCuposTrasFallo(reserva, error);
            throw error;
        }

        reserva.registrarPagoYConfirmar(cotizacion);
    }

    private void confirmarPagoConNotificacionFallida(
            Reserva reserva,
            List<DetalleReserva> cotizacion,
            NotificacionPagoException errorNotificacion) {
        try {
            reserva.registrarPagoYConfirmar(cotizacion);
        } catch (RuntimeException errorConfirmacion) {
            errorNotificacion.addSuppressed(errorConfirmacion);
        }
    }

    private void liberarCuposTrasFallo(Reserva reserva, RuntimeException errorPago) {
        try {
            reserva.liberarCupos();
        } catch (RuntimeException errorLiberacion) {
            errorPago.addSuppressed(errorLiberacion);
        }
    }
}
