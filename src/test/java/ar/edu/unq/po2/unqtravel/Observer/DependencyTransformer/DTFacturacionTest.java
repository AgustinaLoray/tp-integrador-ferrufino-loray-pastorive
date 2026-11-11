package ar.edu.unq.po2.unqtravel.Observer.DependencyTransformer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Estados.EnCurso;
import ar.edu.unq.po2.unqtravel.Estados.Finalizada;
import ar.edu.unq.po2.unqtravel.Observer.DependencyTransformerFactura;
import ar.edu.unq.po2.unqtravel.Observer.ObserverFactura;
import ar.edu.unq.po2.unqtravel.Reserva.ConfirmadorReserva;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Traslado.PuntoEncuentro;
import ar.edu.unq.po2.unqtravel.pagos.MetodoDePago;

public class DTFacturacionTest {

    private DependencyTransformerFactura dtFacturacion;
    private Reserva reserva; 

    private final PrintStream salidaOriginal = System.out;
    private final ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();

    private static class ObserverFacturacionConcreto extends ObserverFactura{
        @Override
        public void notificar() {
            System.out.println("Comprobante fiscal y voucher generados con exito para la reserva");
        }
    }
    
    @BeforeEach 
    public void setUp() {
        reserva = new Reserva(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 11),
                1,
                new PuntoEncuentro(0));
        System.setOut(new PrintStream(salidaCapturada));
        dtFacturacion = new DependencyTransformerFactura(new ObserverFacturacionConcreto());
    }

    @AfterEach
    public void restaurarFlujo() {
        System.setOut(salidaOriginal);
    }

    @Test 
    public void notificarFacturacionEnEstadoFinalizada() { // debe notificar al finalizar
        reserva.cambiarEstado(new Finalizada());
        
        dtFacturacion.notificar(reserva);
        
        String textoImpreso = salidaCapturada.toString().trim();
        assertEquals("Comprobante fiscal y voucher generados con exito para la reserva", textoImpreso);
    }

    @Test
    public void noNotificarFacturacionEnEstadoConfirmada() { // no debe notificar
        confirmarReserva();
        
        dtFacturacion.notificar(reserva);
        
        assertTrue(salidaCapturada.toString().isEmpty());
    }

    @Test
    public void noNotificarFacturacionEnEstadoEnCurso() { // no debe notificar
        reserva.cambiarEstado(new EnCurso());
        
        dtFacturacion.notificar(reserva);
        
        assertTrue(salidaCapturada.toString().isEmpty());
    }

    private void confirmarReserva() {
        Item item = mock(Item.class);
        org.mockito.Mockito.when(item.getPrecioFinal()).thenReturn(0.0);
        reserva.agregarItem(item);
        MetodoDePago pagoExitoso = new MetodoDePago() {
            @Override
            protected void validarDatos(double monto) {}

            @Override
            protected void reservarFondos(double monto) {}

            @Override
            protected void ejecutarTransaccion(double monto) {}
        };
        new ConfirmadorReserva().confirmar(reserva, pagoExitoso);
    }
}
