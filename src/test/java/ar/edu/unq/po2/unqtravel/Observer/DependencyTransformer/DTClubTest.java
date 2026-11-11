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
import ar.edu.unq.po2.unqtravel.Reserva.ConfirmadorReserva;
import ar.edu.unq.po2.unqtravel.Observer.DependencyTransformerClub;
import ar.edu.unq.po2.unqtravel.Observer.ObserverClub;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Traslado.PuntoEncuentro;
import ar.edu.unq.po2.unqtravel.pagos.MetodoDePago;

public class DTClubTest {
    private DependencyTransformerClub dtClub;
    private Reserva reserva; 

    private final PrintStream salidaOriginal = System.out;
    private final ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();

    private static class ObserverClubConcreto extends ObserverClub {
        @Override
        public void notificar() {
            System.out.println("Cupon de descuento del 5% enviado al cliente por cancelacion");
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
        dtClub = new DependencyTransformerClub(new ObserverClubConcreto());
    }

    @AfterEach
    public void restaurarFlujo() {
        System.setOut(salidaOriginal);
    }

    @Test 
    public void notificarAlClub() { //estado cancelada asi que si deberia notificar al club
        this.reserva.cambiarEstado(new Cancelada());
        dtClub.notificar(reserva);
        String textoImpreso = salidaCapturada.toString().trim();
        assertEquals("Cupon de descuento del 5% enviado al cliente por cancelacion", textoImpreso);
    }

    @Test
    public void estadoConfirmada() { //estado confirmada asi que no deberia notificar al club
        confirmarReserva();
        dtClub.notificar(reserva);
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
