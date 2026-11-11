package ar.edu.unq.po2.unqtravel.Formato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.Catalogo;
import ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico.PaqueteTuristico;
import ar.edu.unq.po2.unqtravel.Catalogo.ServicioIndividual.ServicioIndividual;

class SistemaDeReporteTest {

    @Test
    void reporteAgrupaPorCategoriaYNoDuplicaCapacidadDeItemsDentroDePaquetes() {
        ServicioIndividual hotel = servicio(1, "Hotel Norte", 10, "Alojamiento");
        ServicioIndividual hosteria = servicio(2, "Hosteria Sur", 5, "Alojamiento");
        PaqueteTuristico paquete = new PaqueteTuristico(
                "Escapada", "Paquete de viaje", 0.1, "Paquete", 20);
        paquete.agregarItem(hotel);

        Catalogo catalogo = new Catalogo();
        catalogo.agregarItem(hotel);
        catalogo.agregarItem(hosteria);
        catalogo.agregarItem(paquete);

        String reporte = sistemaDeReporte(catalogo).generarReporteCuposDisponibles(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12),
                new TextoPlano());

        assertTrue(reporte.contains("- Alojamiento | Totales: 15 | Reservados: 0 | Libres: 15"));
        assertEquals(1, reporte.split("Alojamiento", -1).length - 1);
    }

    @Test
    void reporteRechazaComposicionDePaquetesConCiclos() {
        PaqueteTuristico paquete = new PaqueteTuristico(
                "Circular", "Paquete invalido", 0.0, "Paquete", 1);
        paquete.agregarItem(paquete);

        Catalogo catalogo = new Catalogo();
        catalogo.agregarItem(paquete);

        assertThrows(IllegalArgumentException.class,
                () -> sistemaDeReporte(catalogo).generarReporteCuposDisponibles(
                        LocalDate.of(2026, 11, 10),
                        LocalDate.of(2026, 11, 12),
                        new TextoPlano()));
    }

    private ServicioIndividual servicio(int id, String nombre, int cupos, String categoria) {
        return new ServicioIndividual(
                id, nombre, cupos, "Proveedor", categoria, "Descripcion", 100.0, 0.0);
    }

    private SistemaDeReporte sistemaDeReporte(Catalogo catalogo) {
        return new SistemaDeReporte(new GestorDeReserva(), catalogo);
    }
}
