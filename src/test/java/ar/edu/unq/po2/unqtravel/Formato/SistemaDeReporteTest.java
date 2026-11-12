package ar.edu.unq.po2.unqtravel.Formato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.Catalogo;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico.PaqueteTuristico;
import ar.edu.unq.po2.unqtravel.Catalogo.ServicioIndividual.ServicioIndividual;
import ar.edu.unq.po2.unqtravel.Estados.Cancelada;
import ar.edu.unq.po2.unqtravel.Estados.Confirmada;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import ar.edu.unq.po2.unqtravel.Reserva.DetalleReserva;
import ar.edu.unq.po2.unqtravel.Reserva.ConfirmadorReserva;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Traslado.PuntoEncuentro;
import ar.edu.unq.po2.unqtravel.pagos.MetodoDePago;

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

        assertThrows(Excepciones.class,
                () -> sistemaDeReporte(catalogo).generarReporteCuposDisponibles(
                        LocalDate.of(2026, 11, 10),
                        LocalDate.of(2026, 11, 12),
                        new TextoPlano()));
    }

    @Test
    void reporteDeCuposCuentaComponentesPagadosYExcluyePendientesOCanceladas() {
        ServicioIndividual hotel = servicio(9, "Hotel", 10, "Alojamiento");
        ServicioIndividual vuelo = servicio(10, "Vuelo", 10, "Vuelo");
        PaqueteTuristico paquete = new PaqueteTuristico(
                "Viaje", "Hotel y vuelo", 0.1, "Paquete", 5);
        paquete.agregarItem(hotel);
        paquete.agregarItem(vuelo);

        Reserva pagada = nuevaReserva();
        pagada.agregarItem(paquete);
        confirmar(pagada);

        Reserva cancelada = nuevaReserva();
        cancelada.agregarItem(hotel);
        confirmar(cancelada);
        cancelada.cancelar();

        Reserva pendiente = nuevaReserva();
        pendiente.agregarItem(vuelo);

        Catalogo catalogo = new Catalogo();
        catalogo.agregarItem(hotel);
        catalogo.agregarItem(vuelo);
        catalogo.agregarItem(paquete);
        GestorDeReserva gestor = new GestorDeReserva();
        gestor.agregarReserva(pagada);
        gestor.agregarReserva(cancelada);
        gestor.agregarReserva(pendiente);

        String reporte = new SistemaDeReporte(gestor, catalogo).generarReporteCuposDisponibles(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12),
                new TextoPlano());

        assertTrue(reporte.contains("- Alojamiento | Totales: 10 | Reservados: 1 | Libres: 9"));
        assertTrue(reporte.contains("- Vuelo | Totales: 10 | Reservados: 1 | Libres: 9"));
    }

    @Test
    void reporteDeCuposRechazaCiclosDentroDeLosDetallesDeUnaReserva() {
        PaqueteTuristico paqueteCircular = new PaqueteTuristico(
                "Circular", "Paquete circular", 0.0, "Paquete", 1);
        paqueteCircular.agregarItem(paqueteCircular);

        Reserva reservaPagada = mock(Reserva.class);
        when(reservaPagada.getFechaInicio()).thenReturn(LocalDate.of(2026, 11, 10));
        when(reservaPagada.getFechaFin()).thenReturn(LocalDate.of(2026, 11, 12));
        when(reservaPagada.tienePagoConfirmado()).thenReturn(true);
        when(reservaPagada.getEstado()).thenReturn(new Confirmada());
        when(reservaPagada.getDetallesReserva())
                .thenReturn(List.of(new DetalleReserva(paqueteCircular, 0.0, 1)));

        Catalogo catalogo = new Catalogo();
        catalogo.agregarItem(servicio(11, "Hotel", 10, "Alojamiento"));
        GestorDeReserva gestor = new GestorDeReserva();
        gestor.agregarReserva(reservaPagada);

        assertThrows(Excepciones.class,
                () -> new SistemaDeReporte(gestor, catalogo).generarReporteCuposDisponibles(
                        LocalDate.of(2026, 11, 10),
                        LocalDate.of(2026, 11, 12),
                        new TextoPlano()));
    }

    @Test
    void rankingCuentaUnidadesYPromediaElPrecioCobradoPonderadoPorCantidad() {
        Item zeta = itemDePrueba("Zeta");
        when(zeta.getPrecioFinal()).thenReturn(100.0, 300.0);
        Item alpha = itemDePrueba("Alpha");
        when(alpha.getPrecioFinal()).thenReturn(400.0);

        Reserva reserva = nuevaReserva();
        reserva.agregarItem(zeta);
        reserva.agregarItem(zeta);
        reserva.agregarItem(alpha);
        confirmar(reserva);

        Catalogo catalogo = new Catalogo();
        catalogo.agregarItem(alpha);
        catalogo.agregarItem(zeta);
        GestorDeReserva gestor = new GestorDeReserva();
        gestor.agregarReserva(reserva);

        String reporte = new SistemaDeReporte(gestor, catalogo).generarReporteServiciosMasReservados(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12),
                new TextoPlano());

        assertTrue(reporte.indexOf("- Zeta | Cantidad: 2 | Promedio: $200.0")
                < reporte.indexOf("- Alpha | Cantidad: 1 | Promedio: $400.0"));
    }

    @Test
    void rankingExcluyeReservasCanceladasAunqueSeHayanPagado() {
        Item item = itemDePrueba("Excursion");
        when(item.getPrecioFinal()).thenReturn(50.0, 50.0);

        Reserva cancelada = nuevaReserva();
        cancelada.agregarItem(item);
        confirmar(cancelada);
        cancelada.cancelar();
        assertTrue(cancelada.getEstado() instanceof Cancelada);

        Reserva vigente = nuevaReserva();
        vigente.agregarItem(item);
        confirmar(vigente);

        Catalogo catalogo = new Catalogo();
        catalogo.agregarItem(item);
        GestorDeReserva gestor = new GestorDeReserva();
        gestor.agregarReserva(cancelada);
        gestor.agregarReserva(vigente);

        String reporte = new SistemaDeReporte(gestor, catalogo).generarReporteServiciosMasReservados(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12),
                new TextoPlano());

        assertTrue(reporte.contains("- Excursion | Cantidad: 1 | Promedio: $50.0"));
    }

    @Test
    void rankingCuentaElPaqueteCompradoSinContarSusComponentesComoVentasSeparadas() {
        ServicioIndividual hotel = servicio(3, "Hotel", 10, "Alojamiento");
        PaqueteTuristico paquete = new PaqueteTuristico(
                "Paquete", "Paquete con hotel", 0.2, "Paquete", 5);
        paquete.agregarItem(hotel);

        Reserva reserva = nuevaReserva();
        reserva.agregarItem(paquete);
        confirmar(reserva);

        Catalogo catalogo = new Catalogo();
        catalogo.agregarItem(hotel);
        catalogo.agregarItem(paquete);
        GestorDeReserva gestor = new GestorDeReserva();
        gestor.agregarReserva(reserva);

        String reporte = new SistemaDeReporte(gestor, catalogo).generarReporteServiciosMasReservados(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12),
                new TextoPlano());

        assertTrue(reporte.contains("- Paquete | Cantidad: 1 | Promedio: $80.0"));
        assertTrue(reporte.contains("- Hotel | Cantidad: 0 | Promedio: $0.0"));
    }

    @Test
    void exposicionReparteElPesoDelPaqueteEntreProveedoresDistintos() {
        ServicioIndividual servicioAlfaUno = servicio(4, "Servicio Alfa 1", 10, "Alojamiento", "Alfa");
        ServicioIndividual servicioAlfaDos = servicio(5, "Servicio Alfa 2", 10, "Vuelo", "Alfa");
        ServicioIndividual servicioBeta = servicio(6, "Servicio Beta", 10, "Excursion", "Beta");
        PaqueteTuristico paqueteInterno = new PaqueteTuristico(
                "Paquete interno", "Dos servicios de Alfa", 0.0, "Paquete", 5);
        paqueteInterno.agregarItem(servicioAlfaUno);
        paqueteInterno.agregarItem(servicioAlfaDos);
        PaqueteTuristico paquete = new PaqueteTuristico(
                "Paquete", "Servicios de Alfa y Beta", 0.0, "Paquete", 5);
        paquete.agregarItem(paqueteInterno);
        paquete.agregarItem(servicioBeta);

        Catalogo catalogo = new Catalogo();
        catalogo.agregarItem(paquete);

        SistemaDeReporte reporte = sistemaDeReporte(catalogo);
        String texto = reporte.generarReporteExposicionProveedor(new TextoPlano());
        String csv = reporte.generarReporteExposicionProveedor(new CSV());
        String html = reporte.generarReporteExposicionProveedor(new HTML());

        assertTrue(texto.contains("- Alfa | Items equivalentes: 0.5 | Porcentaje: 50.0%"));
        assertTrue(texto.contains("- Beta | Items equivalentes: 0.5 | Porcentaje: 50.0%"));
        assertTrue(csv.contains("Operador,Items Equivalentes,Porcentaje"));
        assertTrue(csv.contains("Alfa,0.5,50.0%"));
        assertTrue(html.contains("Alfa | Items equivalentes: 0.5 | Porcentaje: 50.0%"));
        assertTrue(html.contains("Beta | Items equivalentes: 0.5 | Porcentaje: 50.0%"));
    }

    @Test
    void exposicionAgrupaServiciosIndividualesPorElProveedorInformado() {
        Catalogo catalogo = new Catalogo();
        catalogo.agregarItem(servicio(7, "Hotel", 10, "Alojamiento", "Proveedor Norte"));
        catalogo.agregarItem(servicio(8, "Vuelo", 10, "Vuelo", "Proveedor Sur"));

        String reporte = sistemaDeReporte(catalogo).generarReporteExposicionProveedor(new TextoPlano());

        assertTrue(reporte.contains("- Proveedor Norte | Items equivalentes: 1.0 | Porcentaje: 50.0%"));
        assertTrue(reporte.contains("- Proveedor Sur | Items equivalentes: 1.0 | Porcentaje: 50.0%"));
    }

    @Test
    void exposicionMuestraItemsSinProveedorSinFallaryElCatalogoVacioSigueVacio() {
        Item sinProveedor = itemDePrueba("Sin proveedor");
        Catalogo catalogo = new Catalogo();
        catalogo.agregarItem(sinProveedor);

        String reporteConItemSinProveedor =
                sistemaDeReporte(catalogo).generarReporteExposicionProveedor(new TextoPlano());
        String reporteSinItems =
                sistemaDeReporte(new Catalogo()).generarReporteExposicionProveedor(new TextoPlano());

        assertTrue(reporteConItemSinProveedor.contains(
                "- Sin proveedor asignado | Items equivalentes: 1.0 | Porcentaje: 100.0%"));
        assertEquals("--- REPORTE DE EXPOSICION POR PROVEEDOR ---\n", reporteSinItems);
    }

    private ServicioIndividual servicio(int id, String nombre, int cupos, String categoria) {
        return servicio(id, nombre, cupos, categoria, "Proveedor");
    }

    private ServicioIndividual servicio(
            int id,
            String nombre,
            int cupos,
            String categoria,
            String proveedor) {
        return new ServicioIndividual(
                id, nombre, cupos, proveedor, categoria, "Descripcion", 100.0, 0.0);
    }

    private Item itemDePrueba(String nombre) {
        Item item = mock(Item.class);
        when(item.getNombre()).thenReturn(nombre);
        return item;
    }

    private Reserva nuevaReserva() {
        return new Reserva(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12),
                1,
                new PuntoEncuentro(0));
    }

    private void confirmar(Reserva reserva) {
        new ConfirmadorReserva().confirmar(reserva, new PagoDePrueba());
    }

    private SistemaDeReporte sistemaDeReporte(Catalogo catalogo) {
        return new SistemaDeReporte(new GestorDeReserva(), catalogo);
    }

    private static class PagoDePrueba extends MetodoDePago {
        @Override
        protected void validarDatos(double monto) {}

        @Override
        protected void reservarFondos(double monto) {}

        @Override
        protected void ejecutarTransaccion(double monto) {}
    }
}
