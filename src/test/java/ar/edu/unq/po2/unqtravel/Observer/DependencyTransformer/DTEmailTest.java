package ar.edu.unq.po2.unqtravel.Observer.DependencyTransformer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import ar.edu.unq.po2.unqtravel.Estados.Cancelada;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Estados.EnCurso;
import ar.edu.unq.po2.unqtravel.Estados.Finalizada;
import ar.edu.unq.po2.unqtravel.Observer.DependencyTransformerEmail;
import ar.edu.unq.po2.unqtravel.Observer.MailSender;
import ar.edu.unq.po2.unqtravel.Observer.ObserverEmail;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Reserva.ConfirmadorReserva;
import ar.edu.unq.po2.unqtravel.Traslado.PuntoEncuentro;
import ar.edu.unq.po2.unqtravel.pagos.MetodoDePago;

public class DTEmailTest {

    private DependencyTransformerEmail dtEmail;
    private Reserva reserva; 

    private final PrintStream salidaOriginal = System.out;
    private final ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();

    private static class ObserverEmailConcreto extends ObserverEmail {
        public ObserverEmailConcreto(MailSender mailSender, String emailCliente) {
            super(mailSender, emailCliente);
        }

        @Override
        public void notificar() {
            System.out.println("Email de notificacion enviado al cliente");
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
        dtEmail = new DependencyTransformerEmail(new ObserverEmailConcreto(null, null));
    }

    @AfterEach
    public void restaurarFlujo() {
        System.setOut(salidaOriginal);
    }

    @Test 
    public void notificarEmailEnEstadoConfirmada() { //debe notificar porque pasa a estado confirmada
        confirmarReserva();
        
        dtEmail.notificar(reserva);
        
        String textoImpreso = salidaCapturada.toString().trim();
        assertEquals("Email de notificacion enviado al cliente", textoImpreso);
    }

    @Test 
    public void notificarEmailEnEstadoEnCurso() { //debe notificar porque pasa a estado en curso
        reserva.cambiarEstado(new EnCurso());
        
        dtEmail.notificar(reserva);
        
        String textoImpreso = salidaCapturada.toString().trim();
        assertEquals("Email de notificacion enviado al cliente", textoImpreso);
    }

    @Test 
    public void notificarEmailEnEstadoFinalizada() { //debe notificar porque pasa a estado finalizada
        reserva.cambiarEstado(new Finalizada());
        
        dtEmail.notificar(reserva);
        
        String textoImpreso = salidaCapturada.toString().trim();
        assertEquals("Email de notificacion enviado al cliente", textoImpreso);
    }

    @Test
    public void noNotificarEmailEnEstadoCancelada() { //no debe notificar
        reserva.cambiarEstado(new Cancelada());
        
        dtEmail.notificar(reserva);
        
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
