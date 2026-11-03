package ar.edu.unq.po2.unqtravel.catalogo;

import ar.edu.unq.po2.unqtravel.Catalogo.ServicioIndividual.ServicioIndividual;
import ar.edu.unq.po2.unqtravel.Catalogo.atributos.AtributoDinamico;
import ar.edu.unq.po2.unqtravel.Catalogo.atributos.TipoString;
import ar.edu.unq.po2.unqtravel.Catalogo.atributos.ValorAtributo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicioIndividualTest {
    @Test
    void obtieneDatosYCalculaPrecioFinalConDescuento() {
        ServicioIndividual hotel = crearHotel(0.10);

        assertEquals("Hotel", hotel.getNombre());
        assertEquals("Habitacion doble", hotel.getDescripcion());
        assertEquals(100.0, hotel.getPrecioBase());
        assertEquals(90.0, hotel.getPrecioFinal());
    }

    @Test
    void aceptaServicioSinAtributosDinamicos() {
        assertTrue(crearHotel(0.0).esValido());
    }

    @Test
    void esValidoSoloCuandoTodosLosAtributosSonValidos() {
        ServicioIndividual hotel = crearHotel(0.0);
        AtributoDinamico requerido = new AtributoDinamico("piso", true, new TipoString());

        hotel.agregarAtributo(new ValorAtributo("3", requerido));
        assertTrue(hotel.esValido());

        hotel.agregarAtributo(new ValorAtributo(3, requerido));
        assertFalse(hotel.esValido());
    }

    @Test
    void reservarYLiberarCupoActualizaDisponibilidad() {
        ServicioIndividual hotel = crearHotel(0.0);

        hotel.decrementarCupo();
        assertEquals(1, hotel.getCuposDisponibles());

        hotel.incrementarCupo();
        assertEquals(2, hotel.getCuposDisponibles());
    }

    @Test
    void rechazaPrecioNuloONegativo() {
        assertThrows(IllegalArgumentException.class, () -> crearHotelConPrecio(null, 0.0));
        assertThrows(IllegalArgumentException.class, () -> crearHotelConPrecio(-1.0, 0.0));
    }

    @Test
    void aceptaDescuentoNuloComoCeroYRechazaValoresFueraDeRango() {
        assertEquals(100.0, crearHotelConPrecio(100.0, null).getPrecioFinal());
        assertThrows(IllegalArgumentException.class, () -> crearHotelConPrecio(100.0, -0.01));
        assertThrows(IllegalArgumentException.class, () -> crearHotelConPrecio(100.0, 1.01));
    }

    @Test
    void laListaDeAtributosNoPermiteModificarElEstadoInterno() {
        ServicioIndividual hotel = crearHotel(0.0);

        assertThrows(UnsupportedOperationException.class, () -> hotel.getAtributos().clear());
        assertThrows(NullPointerException.class, () -> hotel.agregarAtributo(null));
        assertTrue(hotel.getAtributos().isEmpty());
    }

    private ServicioIndividual crearHotel(double descuento) {
        return crearHotelConPrecio(100.0, descuento);
    }

    private ServicioIndividual crearHotelConPrecio(Double precioBase, Double descuento) {
        return new ServicioIndividual(
                1,
                "Hotel",
                2,
                "Operador sur",
                "Alojamiento",
                "Habitacion doble",
                precioBase,
                descuento
        );
    }
}
