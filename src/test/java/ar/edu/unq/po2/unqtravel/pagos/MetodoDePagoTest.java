package ar.edu.unq.po2.unqtravel.pagos;

import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class MetodoDePagoTest {

    @Test
    void ejecutaLosPasosEnOrdenYTransfiereElMonto() {
        List<String> pasos = new ArrayList<>();
        MetodoDePago metodo = new MetodoDePagoRegistrado(pasos, null);

        metodo.procesarPago(120.50);

        assertEquals(
                List.of("validar:120.5", "reservar:120.5", "ejecutar:120.5", "notificar"),
                pasos);
    }

    @Test
    void noContinuaCuandoFallaLaValidacion() {
        assertFallaYDetieneEn("validar",
                List.of("validar:50.0"));
    }

    @Test
    void noContinuaCuandoFallaLaReserva() {
        assertFallaYDetieneEn("reservar",
                List.of("validar:50.0", "reservar:50.0"));
    }

    @Test
    void noContinuaCuandoFallaLaTransaccion() {
        assertFallaYDetieneEn("ejecutar",
                List.of("validar:50.0", "reservar:50.0", "ejecutar:50.0"));
    }

    @Test
    void usaElHookVacioCuandoNoSeSobrescribe() {
        List<String> pasos = new ArrayList<>();
        MetodoDePago metodo = new MetodoSinNotificacion(pasos);

        metodo.procesarPago(25);

        assertEquals(List.of("validar", "reservar", "ejecutar"), pasos);
    }

    private void assertFallaYDetieneEn(String pasoQueFalla, List<String> pasosEsperados) {
        List<String> pasos = new ArrayList<>();
        MetodoDePago metodo = new MetodoDePagoRegistrado(pasos, pasoQueFalla);

        Excepciones excepcion = assertThrows(
                Excepciones.class,
                () -> metodo.procesarPago(50));

        assertEquals("No se pudo " + descripcionDelPaso(pasoQueFalla) + ".", excepcion.getMessage());
        assertEquals(pasoQueFalla, excepcion.getCause().getMessage());
        assertEquals(pasosEsperados, pasos);
    }

    private String descripcionDelPaso(String paso) {
        return switch (paso) {
            case "validar" -> "validar los datos del pago";
            case "reservar" -> "reservar los fondos";
            default -> "ejecutar la transacción";
        };
    }

    private static class MetodoDePagoRegistrado extends MetodoDePago {
        private final List<String> pasos;
        private final String pasoQueFalla;

        private MetodoDePagoRegistrado(List<String> pasos, String pasoQueFalla) {
            this.pasos = pasos;
            this.pasoQueFalla = pasoQueFalla;
        }

        @Override
        protected void validarDatos(double monto) {
            registrarPaso("validar", monto);
        }

        @Override
        protected void reservarFondos(double monto) {
            registrarPaso("reservar", monto);
        }

        @Override
        protected void ejecutarTransaccion(double monto) {
            registrarPaso("ejecutar", monto);
        }

        @Override
        protected void notificarResultado() {
            pasos.add("notificar");
            lanzarSiCorresponde("notificar");
        }

        private void registrarPaso(String paso, double monto) {
            pasos.add(paso + ":" + monto);
            lanzarSiCorresponde(paso);
        }

        private void lanzarSiCorresponde(String paso) {
            if (paso.equals(pasoQueFalla)) {
                throw new IllegalStateException(paso);
            }
        }
    }

    private static class MetodoSinNotificacion extends MetodoDePago {
        private final List<String> pasos;

        private MetodoSinNotificacion(List<String> pasos) {
            this.pasos = pasos;
        }

        @Override
        protected void validarDatos(double monto) {
            pasos.add("validar");
        }

        @Override
        protected void reservarFondos(double monto) {
            pasos.add("reservar");
        }

        @Override
        protected void ejecutarTransaccion(double monto) {
            pasos.add("ejecutar");
        }
    }
}
