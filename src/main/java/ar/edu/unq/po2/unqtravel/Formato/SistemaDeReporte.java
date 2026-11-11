package ar.edu.unq.po2.unqtravel.Formato;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.Catalogo;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
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
                       .filter(r -> r.getItems().contains(item))
                       .toList();
    }
    
    private int calcularCantidad(Item item, List<Reserva> reservas) {
        return (int) this.reservasConItem(item, reservas).stream().count();
    }
    
    private double calcularPrecioPromedio(Item item, List<Reserva> reservas) {
        List<Reserva> reservasDelItem = this.reservasConItem(item, reservas);
        
        return reservasDelItem.stream()
                              .mapToDouble(Reserva::getCostoItem)
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
    
    private int calcularReservados(Item item, List<Reserva> reservas) {
        return (int) reservas.stream()
                             .filter(r -> r.getItems().contains(item))
                             .count();
    }
    
    private List<ItemCupo> armarFilasCupos(List<Reserva> reservasDelPeriodo) {
        return catalogo.getItems().stream()
        		.map(item -> new ItemCupo(item.getNombre(),
                item.getCupos(), this.calcularReservados(item, reservasDelPeriodo)))
        		.toList();
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
