package ar.edu.unq.po2.unqtravel.Traslado;
import ar.edu.unq.po2.unqtravel.Reserva.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Traslado.Direccion;
import ar.edu.unq.po2.unqtravel.Traslado.PuntoEncuentro;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

class TrasladoTest {
	TrasladoEstandarAdapter trasladoEstandar;
	TrasladoVipAdapter trasladoVip;
	PuntoEncuentro puntoDeEncuentro;
	TrasladoVip libreriaTrasladoVip;
	Direccion brasil;
	Reserva unaReserva;
	
	@BeforeEach   
	public void setup() {
		
		puntoDeEncuentro = new PuntoEncuentro(3000.0);
	}
	
	@Test
    public void testPuntoEncuentroSiempreEsCero() {
        
        double costo = puntoDeEncuentro.calcularCosto(unaReserva);
        
        assertEquals(0.0, costo, 0.01);
    }

    @Test
    public void testTrasladoVipAdapterConMockito() {
  
    	
        TrasladoVip libreriaMock = mock(TrasladoVip.class);
        when(libreriaMock.calcularCosto(anyFloat())).thenReturn(150.0);

        TrasladoVipAdapter trasladoVip = new TrasladoVipAdapter(libreriaMock);
        
        Reserva reservaMock = mock(Reserva.class);
        when(libreriaMock.calcularCosto(anyFloat())).thenReturn(150.0);

        double costoFinal = trasladoVip.calcularCosto(reservaMock);

        assertEquals(150.0, costoFinal, 0.01);
    }

    @Test
    public void testTrasladoEstandarAdapterConMockito() {
        
        Reserva reservaMock = mock(Reserva.class);
        when(reservaMock.getCantidadPasajeros()).thenReturn(2);


        double costoFinal = trasladoEstandar.calcularCosto(reservaMock);

        
        assertEquals(450.0, costoFinal, 0.01);
    }

	
}


