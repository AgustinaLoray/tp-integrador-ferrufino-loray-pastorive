package ar.edu.unq.po2.unqtravel.Reserva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

class DetalleReservaTest {

    @Test
    void conservaItemPrecioUnitarioCantidadEImporteTotal() {
        Item item = mock(Item.class);
        DetalleReserva detalle = new DetalleReserva(item, 12.5, 4);

        assertEquals(item, detalle.getItem());
        assertEquals(12.5, detalle.getPrecioUnitarioCobrado());
        assertEquals(4, detalle.getCantidad());
        assertEquals(50.0, detalle.getImporteTotal());
    }

    @Test
    void validaItemPrecioYCantidad() {
        Item item = mock(Item.class);

        assertThrows(NullPointerException.class, () -> new DetalleReserva(null, 10.0, 1));
        assertThrows(IllegalArgumentException.class, () -> new DetalleReserva(item, -1.0, 1));
        assertThrows(IllegalArgumentException.class, () -> new DetalleReserva(item, Double.NaN, 1));
        assertThrows(IllegalArgumentException.class, () -> new DetalleReserva(item, 10.0, 0));
    }
}
