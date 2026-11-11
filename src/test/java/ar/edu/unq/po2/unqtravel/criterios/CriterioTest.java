package ar.edu.unq.po2.unqtravel.criterios;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.Catalogo;
import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.CriterioBusqueda;
import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.CriterioCategoria;
import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.CriterioDisponibilidad;
import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.CriterioNombre;
import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.CriterioPrecioMaximo;
import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.CriterioAND;
import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.CriterioOR;
import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.CriterioNOT;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Catalogo.ServicioIndividual.ServicioIndividual;

public class CriterioTest {
    private Item item;

    @BeforeEach 
    public void setUp() {
        item = new ServicioIndividual(2, "Vuelo Directo a Bariloche", 2, "Aerolíneas Argentinas", "Vuelo",
                                        "Clase turista ida y vuelta", 250000.0, 0.0);
    }

    @Test 
    public void testCriterioPorNombre() {
        CriterioBusqueda criterio = new CriterioNombre("DIRECTO A");
        assertTrue(criterio.esSatisfechoPor(item));
        assertFalse(new CriterioNombre("hotel").esSatisfechoPor(item));
    }

    @Test 
    public void testCriterioPrecioMaximo() {
        assertTrue(new CriterioPrecioMaximo(250000.0).esSatisfechoPor(item));
        assertFalse(new CriterioPrecioMaximo(249999.0).esSatisfechoPor(item));
    }

    @Test 
    public void testCriterioCategoria() {
        CriterioCategoria criterio = new CriterioCategoria("VUELO");
        assertTrue(criterio.esSatisfechoPor(item));
        assertFalse(new CriterioCategoria("Alojamiento").esSatisfechoPor(item));
    }

    @Test 
    public void testCriterioDisponibilidad() {
        CriterioBusqueda criterio = new CriterioDisponibilidad();
        assertTrue(criterio.esSatisfechoPor(item));
        item.decrementarCupo();
        item.decrementarCupo();
        assertFalse(criterio.esSatisfechoPor(item));
    }

    @Test
    public void catalogoBuscaConUnCriterioYDevuelveSoloLosItemsCoincidentes() {
        Catalogo catalogo = new Catalogo();
        Item alojamiento = new ServicioIndividual(
                3, "Hotel en Bariloche", 5, "Hotel Sur", "Alojamiento",
                "Habitación doble", 180000.0, 0.0);
        catalogo.agregarItem(item);
        catalogo.agregarItem(alojamiento);

        CriterioAND criterio = new CriterioAND();
        criterio.agregarCriterio(new CriterioCategoria("Vuelo"));
        criterio.agregarCriterio(new CriterioPrecioMaximo(250000.0));

        assertEquals(List.of(item), catalogo.buscar(criterio));
    }


    @Test
    public void testCriterioAND() {
        CriterioBusqueda porCategoria = new CriterioCategoria("Vuelo");
        CriterioBusqueda porNombre = new CriterioNombre("Vuelo Directo a Bariloche");

        CriterioAND criterioAnd = new CriterioAND();
        criterioAnd.agregarCriterio(porCategoria);
        criterioAnd.agregarCriterio(porNombre);

        assertTrue(criterioAnd.esSatisfechoPor(item));
    }

    @Test
    public void testCriterioANDFalla() {
        CriterioBusqueda porCategoria = new CriterioCategoria("Vuelo"); // true
        CriterioBusqueda precioBajo = new CriterioPrecioMaximo(300.0);    // false

        CriterioAND criterioAnd = new CriterioAND();
        criterioAnd.agregarCriterio(porCategoria);
        criterioAnd.agregarCriterio(precioBajo);

        assertFalse(criterioAnd.esSatisfechoPor(item));
    }

    @Test
    public void testCriterioOR() {
        CriterioBusqueda precioBajo = new CriterioPrecioMaximo(300.0); // false
        CriterioBusqueda porCategoria = new CriterioCategoria("Vuelo"); // true

        CriterioOR criterioOr = new CriterioOR();
        criterioOr.agregarCriterio(precioBajo);
        criterioOr.agregarCriterio(porCategoria);

        assertTrue(criterioOr.esSatisfechoPor(item));
    }

    @Test
    public void testCriterioOrFalla() {
        CriterioBusqueda precioBajo = new CriterioPrecioMaximo(300.0);        // false
        CriterioBusqueda porAlojamiento = new CriterioCategoria("Alojamiento"); // false

        CriterioOR criterioOr = new CriterioOR();
        criterioOr.agregarCriterio(precioBajo);
        criterioOr.agregarCriterio(porAlojamiento);

        assertFalse(criterioOr.esSatisfechoPor(item));
    }

    @Test
    public void testCriterioNOT() {
        CriterioNOT noAlojamiento = new CriterioNOT();
        noAlojamiento.agregarCriterio(new CriterioCategoria("Alojamiento"));
        assertTrue(noAlojamiento.esSatisfechoPor(item));

        CriterioNOT noVuelo = new CriterioNOT();
        noVuelo.agregarCriterio(new CriterioCategoria("Vuelo"));
        assertFalse(noVuelo.esSatisfechoPor(item));
    }

    @Test
    public void testConsultaAnidada() {

        CriterioOR categoriasValidas = new CriterioOR();
        categoriasValidas.agregarCriterio(new CriterioCategoria("Vuelo"));
        categoriasValidas.agregarCriterio(new CriterioCategoria("Alojamiento"));

        CriterioNOT precioNoEconomico = new CriterioNOT();
        precioNoEconomico.agregarCriterio(new CriterioPrecioMaximo(300.0));

        CriterioAND consultaCompleja = new CriterioAND();
        consultaCompleja.agregarCriterio(categoriasValidas);
        consultaCompleja.agregarCriterio(precioNoEconomico);

        assertTrue(consultaCompleja.esSatisfechoPor(item));
    }

}   