package ar.edu.unq.po2.unqtravel.catalogo;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico.PaqueteTuristico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaqueteTuristicoTest {
    private PaqueteTuristico paquete;

    @BeforeEach
    void setUp() {
        paquete = new PaqueteTuristico("Escapada", "Fin de semana", 0.10, "Paquete", 1);
    }

    @Test
    void retornaNombreYDescripcion() {
        assertEquals("Escapada", paquete.getNombre());
        assertEquals("Fin de semana", paquete.getDescripcion());
    }

    @Test
    void paqueteVacioTienePreciosEnCero() {
        assertEquals(0.0, paquete.getPrecioBase());
        assertEquals(0.0, paquete.getPrecioFinal());
    }

    @Test
    void sumaLosPreciosBaseYAplicaElDescuentoAlPrecioFinal() {
        paquete.agregarItem(new ItemDePrueba(100.0, 80.0));
        paquete.agregarItem(new ItemDePrueba(50.0, 40.0));

        assertEquals(150.0, paquete.getPrecioBase());
        assertEquals(108.0, paquete.getPrecioFinal(), 0.001);
    }

    @Test
    void agregarYQuitarItemsActualizaLosPrecios() {
        ItemDePrueba item = new ItemDePrueba(100.0, 80.0);
        paquete.agregarItem(item);

        assertEquals(100.0, paquete.getPrecioBase());
        assertEquals(72.0, paquete.getPrecioFinal(), 0.001);

        paquete.quitarItem(item);

        assertEquals(0.0, paquete.getPrecioBase());
        assertEquals(0.0, paquete.getPrecioFinal());
    }

    @Test
    void descuentoNuloSeInterpretaComoCero() {
        paquete.setDescuento(null);
        paquete.agregarItem(new ItemDePrueba(100.0, 80.0));

        assertEquals(0.0, paquete.getDescuento());
        assertEquals(80.0, paquete.getPrecioFinal());
    }

    @Test
    void rechazaDescuentosFueraDeRangoAlConstruirYAlActualizar() {
        assertThrows(IllegalArgumentException.class, () -> new PaqueteTuristico("Paquete", "Descripcion", -0.01, "Paquete", 1));
        assertThrows(IllegalArgumentException.class, () -> new PaqueteTuristico("Paquete", "Descripcion", 1.01, "Paquete", 1));
        assertThrows(IllegalArgumentException.class, () -> paquete.setDescuento(-0.01));
        assertThrows(IllegalArgumentException.class, () -> paquete.setDescuento(1.01));
    }

    @Test
    void laListaDeItemsNoPermiteModificarElEstadoInterno() {
        ItemDePrueba item = new ItemDePrueba(100.0, 80.0);
        paquete.agregarItem(item);

        assertThrows(UnsupportedOperationException.class, () -> paquete.getItems().clear());
        assertEquals(1, paquete.getItems().size());
        assertThrows(NullPointerException.class, () -> paquete.agregarItem(null));
    }

    private static class ItemDePrueba implements Item {
        private final double precioBase;
        private final double precioFinal;

        private ItemDePrueba(double precioBase, double precioFinal) {
            this.precioBase = precioBase;
            this.precioFinal = precioFinal;
        }

        @Override
        public String getNombre() {
            return "Item";
        }

        @Override
        public String getDescripcion() {
            return "Item de prueba";
        }

        @Override
        public double getPrecioBase() {
            return precioBase;
        }

        @Override
        public double getPrecioFinal() {
            return precioFinal;
        }

        @Override
        public void decrementarCupo() {
        }

        @Override
        public void incrementarCupo() {
        }

        @Override
        public int getCupos() {
            return 0;
        }

        @Override
        public String getOperador() {
            return "Operador";
        }

        @Override
        public String getCategoria() {
            return "Categoría";
        }

        @Override
        public boolean tieneCupoDisponible() {
            return true;
        }
    }
}
