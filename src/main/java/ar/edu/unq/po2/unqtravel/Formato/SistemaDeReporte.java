package ar.edu.unq.po2.unqtravel.Formato;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.Catalogo;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico.PaqueteTuristico;
import ar.edu.unq.po2.unqtravel.Estados.Cancelada;
import ar.edu.unq.po2.unqtravel.Reserva.DetalleReserva;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class SistemaDeReporte {
	private GestorDeReserva gestorDeReservas;
    private Catalogo catalogo; 

    
    public SistemaDeReporte(GestorDeReserva gestorDeReservas, Catalogo catalogo) {
        this.gestorDeReservas = gestorDeReservas;
        this.catalogo = catalogo;
    }
    
    //Seccion de reporte Servicios mas Reservados

    public String generarReporteServiciosMasReservados(LocalDate inicio, LocalDate fin, Formato formato) {
        List<Reserva> reservasDelPeriodo = gestorDeReservas.obtenerReservasEnPeriodo(inicio, fin);
        List<ItemRanking> listaDeFilas = new ArrayList<>();
        
        for (Item s : catalogo.getItems()) {
            int cantidad = this.calcularCantidad(s, reservasDelPeriodo);
            double promedio = this.calcularPrecioPromedio(s, reservasDelPeriodo);
            
            listaDeFilas.add(new ItemRanking(s.getNombre(), cantidad, promedio));
        }
        
        ServiciosMasReservados serviciosMasReservados = new ServiciosMasReservados(listaDeFilas, inicio, fin);
        return serviciosMasReservados.aceptar(formato);
    }
    
    private List<Reserva> reservasConItem(Item item, List<Reserva> reservas) {
        return reservas.stream()
                       .filter(Reserva::tienePagoConfirmado)
                       .filter(r -> r.getItems().contains(item))
                       .toList();
    }
    
    private int calcularCantidad(Item item, List<Reserva> reservas) {
        return (int) this.reservasConItem(item, reservas).stream().count();
    }
    
    private double calcularPrecioPromedio(Item item, List<Reserva> reservas) {
        List<Reserva> reservasDelItem = this.reservasConItem(item, reservas);
        
        return reservasDelItem.stream()
                              .mapToDouble(reserva -> reserva.getCostoItem(item))
                              .average()
                              .orElse(0.0);
    }
    
    
    // Seccion de reporte cupos disponibles
    
    public String generarReporteCuposDisponibles(LocalDate inicio, LocalDate fin, Formato formato) {
        List<Reserva> reservasDelPeriodo = gestorDeReservas.obtenerReservasEnPeriodo(inicio, fin);
        List<ItemCupo> listaFilas = this.armarFilasCupos(reservasDelPeriodo);
        
        CupoDisponible cupoDisponible = new CupoDisponible(listaFilas, inicio, fin);
        return cupoDisponible.aceptar(formato);
    }
    
    private List<ItemCupo> armarFilasCupos(List<Reserva> reservasDelPeriodo) {
        Map<String, Integer> totalesPorCategoria = new LinkedHashMap<>();
        Map<String, Integer> reservadosPorCategoria = new LinkedHashMap<>();
        Set<Item> inventarioContado = Collections.newSetFromMap(new IdentityHashMap<>());

        for (Item item : catalogo.getItems()) {
            acumularTotales(item, totalesPorCategoria, inventarioContado, crearConjuntoPorIdentidad());
        }

        for (Reserva reserva : reservasDelPeriodo) {
            if (!reserva.tienePagoConfirmado()
                    || reserva.getEstado() instanceof Cancelada) {
                continue;
            }

            for (DetalleReserva detalle : reserva.getDetallesReserva()) {
                acumularReservados(
                        detalle.getItem(),
                        detalle.getCantidad(),
                        reservadosPorCategoria,
                        crearConjuntoPorIdentidad());
            }
        }

        return totalesPorCategoria.entrySet().stream()
                .map(entrada -> new ItemCupo(
                        entrada.getKey(),
                        entrada.getValue(),
                        reservadosPorCategoria.getOrDefault(entrada.getKey(), 0)))
                .toList();
    }

    private void acumularTotales(
            Item item,
            Map<String, Integer> totalesPorCategoria,
            Set<Item> inventarioContado,
            Set<Item> paquetesEnRecorrido) {
        if (item instanceof PaqueteTuristico paquete) {
            if (!paquetesEnRecorrido.add(paquete)) {
                throw new IllegalArgumentException("La composición de paquetes contiene un ciclo.");
            }
            try {
                for (Item componente : paquete.getItems()) {
                    acumularTotales(componente, totalesPorCategoria, inventarioContado, paquetesEnRecorrido);
                }
            } finally {
                paquetesEnRecorrido.remove(paquete);
            }
            return;
        }

        if (inventarioContado.add(item)) {
            totalesPorCategoria.merge(item.getCategoria(), item.getCuposTotales(), Integer::sum);
        }
    }

    private void acumularReservados(
            Item item,
            int cantidad,
            Map<String, Integer> reservadosPorCategoria,
            Set<Item> paquetesEnRecorrido) {
        if (item instanceof PaqueteTuristico paquete) {
            if (!paquetesEnRecorrido.add(paquete)) {
                throw new IllegalArgumentException("La composición de paquetes contiene un ciclo.");
            }
            try {
                for (Item componente : paquete.getItems()) {
                    acumularReservados(componente, cantidad, reservadosPorCategoria, paquetesEnRecorrido);
                }
            } finally {
                paquetesEnRecorrido.remove(paquete);
            }
            return;
        }

        reservadosPorCategoria.merge(
                item.getCategoria(),
                cantidad,
                Integer::sum);
    }

    private Set<Item> crearConjuntoPorIdentidad() {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }
    
    // Seccion de exposicion por proveedor
    
    public String generarReporteExposicionProveedor(Formato formato) {
        List<Item> items = catalogo.getItems();
        double totalItems = items.size();
        
        if (totalItems == 0) {
            return new ExposicionPorProveedor(new ArrayList<>()).aceptar(formato);
        }

        List<ItemExposicion> listaFilas = this.armarFilasExposicion(items, totalItems);

        ExposicionPorProveedor reporte = new ExposicionPorProveedor(listaFilas);
        return reporte.aceptar(formato);
    }
    
    
    private List<ItemExposicion> armarFilasExposicion(List<Item> items, double totalItems) {
        List<ItemExposicion> listaFilas = new ArrayList<>();
        
        List<String> operadoresUnicos = items.stream()
            .map(Item::getOperador)
            .distinct()
            .toList();
        
        for (String operador : operadoresUnicos) {
            int cantidad = this.contarItemsDeOperador(operador, items);
            double porcentaje = (cantidad / totalItems) * 100.0;
            
            listaFilas.add(new ItemExposicion(operador, cantidad, porcentaje));
        }
        
        return listaFilas;
    }
    
    
    private int contarItemsDeOperador(String operador, List<Item> items) {
        return (int) items.stream()
                          .filter(item -> item.getOperador().equals(operador))
                          .count();
    }
}
