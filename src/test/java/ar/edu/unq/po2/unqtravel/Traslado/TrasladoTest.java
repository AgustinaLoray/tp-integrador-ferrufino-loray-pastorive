package ar.edu.unq.po2.unqtravel.Traslado;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

class TrasladoTest {
    @Test
    void reservaRechazaCantidadDePasajerosNoPositivaOMetodoNulo() {
        LocalDate inicio = LocalDate.of(2026, 11, 10);
        LocalDate fin = LocalDate.of(2026, 11, 12);

        assertThrows(Excepciones.class,
                () -> new Reserva(inicio, fin, 0, new PuntoEncuentro(0)));
        assertThrows(Excepciones.class,
                () -> new Reserva(inicio, fin, -1, new PuntoEncuentro(0)));
        assertThrows(Excepciones.class,
                () -> new Reserva(inicio, fin, 1, null));
    }

    @Test
    void puntoDeEncuentroNoTieneCostoYExponeDisponibilidadDeGuias() {
        Reserva reserva = mock(Reserva.class);
        PuntoEncuentro conGuias = new PuntoEncuentro(2);
        PuntoEncuentro sinGuias = new PuntoEncuentro(0);

        assertEquals(0.0, conGuias.calcularCosto(reserva));
        assertTrue(conGuias.hayCuposDisponibles());
        assertFalse(sinGuias.hayCuposDisponibles());
    }

    @Test
    void trasladoVipDelegaElSubtotalDeServiciosYDevuelveLaRespuestaDeLaApi() {
        ApiTrasladoVip api = mock(ApiTrasladoVip.class);
        when(api.calcularCosto(1000.0f)).thenReturn(150.0f);
        TrasladoVipAdapter trasladoVip = new TrasladoVipAdapter(api);
        Reserva reserva = mock(Reserva.class);
        when(reserva.getCostoServicios()).thenReturn(1000.0);

        double costoFinal = trasladoVip.calcularCosto(reserva);

        verify(api).calcularCosto(1000.0f);
        assertEquals(150.0, costoFinal, 0.01);
    }

    @Test
    void trasladoEstandarDelegaPasajerosYDestinoYDevuelveLaRespuestaDeLaApi() {
        PlataformaTransporte plataforma = mock(PlataformaTransporte.class);
        Direccion destino = new Direccion();
        when(plataforma.estimarTraslado(2, destino)).thenReturn(450.0f);
        TrasladoEstandarAdapter trasladoEstandar =
                new TrasladoEstandarAdapter(destino, plataforma);
        Reserva reserva = mock(Reserva.class);
        when(reserva.getCantidadPasajeros()).thenReturn(2);

        double costoFinal = trasladoEstandar.calcularCosto(reserva);

        verify(plataforma).estimarTraslado(2, destino);
        assertEquals(450.0, costoFinal, 0.01);
    }

    @Test
    void costoTasasYValorTotalVipNoEntranEnRecursion() {
        ApiTrasladoVip api = mock(ApiTrasladoVip.class);
        when(api.calcularCosto(1000.0f)).thenReturn(150.0f);
        Reserva reserva = new Reserva(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12),
                2,
                new TrasladoVipAdapter(api));
        Item item = mock(Item.class);
        when(item.getPrecioFinal()).thenReturn(1000.0);
        reserva.agregarItem(item);

        assertEquals(150.0, reserva.getCostoTasas(), 0.01);
        assertEquals(1150.0, reserva.getValorTotal(), 0.01);
    }
}
