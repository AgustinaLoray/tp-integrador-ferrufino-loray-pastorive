package ar.edu.unq.po2.unqtravel.Observer.DependencyTransformer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import ar.edu.unq.po2.unqtravel.Estados.Cancelada;
import ar.edu.unq.po2.unqtravel.Estados.Confirmada;
import ar.edu.unq.po2.unqtravel.Observer.DependencyTransformerClub;
import ar.edu.unq.po2.unqtravel.Observer.ObserverClub;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

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
        reserva = new Reserva();
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
        reserva.cambiarEstado(new Confirmada());
        dtClub.notificar(reserva);
        assertTrue(salidaCapturada.toString().isEmpty());
    }

}
