package ar.edu.unq.po2.unqtravel.Observer.DependencyTransformer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import ar.edu.unq.po2.unqtravel.Estados.Confirmada;
import ar.edu.unq.po2.unqtravel.Estados.EnCurso;
import ar.edu.unq.po2.unqtravel.Estados.Finalizada;
import ar.edu.unq.po2.unqtravel.Observer.DependencyTransformerFactura;
import ar.edu.unq.po2.unqtravel.Observer.ObserverFactura;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

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
        reserva = new Reserva();
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
        reserva.cambiarEstado(new Confirmada());
        
        dtFacturacion.notificar(reserva);
        
        assertTrue(salidaCapturada.toString().isEmpty());
    }

    @Test
    public void noNotificarFacturacionEnEstadoEnCurso() { // no debe notificar
        reserva.cambiarEstado(new EnCurso());
        
        dtFacturacion.notificar(reserva);
        
        assertTrue(salidaCapturada.toString().isEmpty());
    }
}
