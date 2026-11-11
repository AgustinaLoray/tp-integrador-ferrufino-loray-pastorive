package ar.edu.unq.po2.unqtravel.Formato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class FormatosReporteTest {

    @Test
    void csvYHtmlExportanServiciosMasReservados() {
        ServiciosMasReservados reporte = new ServiciosMasReservados(
                List.of(new ItemRanking("Hotel", 3, 125.5)),
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12));

        assertEquals(
                "Servicio,Cantidad Vendida,Precio Promedio\nHotel,3,125.5\n",
                reporte.aceptar(new CSV()));
        assertEquals(
                "<h1>Servicios Más Reservados</h1><ul><li>Hotel - 3 unidades - $125.5</li></ul>",
                reporte.aceptar(new HTML()));
    }

    @Test
    void csvYHtmlExportanOcupacionYDisponibilidad() {
        CupoDisponible reporte = new CupoDisponible(
                List.of(new ItemCupo("Alojamiento", 10, 4)),
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12));
        assertEquals(LocalDate.of(2026, 11, 10), reporte.getFechaInicio());
        assertEquals(LocalDate.of(2026, 11, 12), reporte.getFechaFin());
        assertEquals(1, reporte.getListaCupos().size());

        assertEquals(
                "Categoria,Cupos Totales,Cupos Reservados,Cupos Libres\nAlojamiento,10,4,6\n",
                reporte.aceptar(new CSV()));
        assertEquals(
                "<h1>Reporte de Ocupación y Cupos</h1><ul>"
                        + "<li>Alojamiento | Totales: 10 | Reservados: 4 | Libres: 6</li></ul>",
                reporte.aceptar(new HTML()));
    }

    @Test
    void losFormatosExportanListasVaciasConSusEncabezados() {
        ServiciosMasReservados rankingVacio = new ServiciosMasReservados(
                List.of(),
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12));
        CupoDisponible cuposVacios = new CupoDisponible(
                List.of(),
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12));

        assertEquals("Servicio,Cantidad Vendida,Precio Promedio\n", rankingVacio.aceptar(new CSV()));
        assertTrue(rankingVacio.aceptar(new HTML()).endsWith("<ul></ul>"));
        assertEquals("Categoria,Cupos Totales,Cupos Reservados,Cupos Libres\n", cuposVacios.aceptar(new CSV()));
        assertTrue(cuposVacios.aceptar(new HTML()).endsWith("<ul></ul>"));
    }
}
