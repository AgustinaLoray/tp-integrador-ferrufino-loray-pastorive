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

    public String generarReporteServiciosMasReservados(LocalDate inicio, LocalDate fin, Formato formato) {
        List<Reserva> reservasDelPeriodo = gestorDeReservas.obtenerReservasEnPeriodo(inicio, fin);
        List<ItemRanking> listaDeFilas = new ArrayList<>();
        
  
         for (Item s : catalogo.getItems()) {
             int cantidad = this.calcularCantidad(s, reservasDelPeriodo);
             double promedio = calcularPrecioPromedio(s, reservasDelPeriodo);
             listaDeFilas.add(new ItemRanking(s.getNombre(), cantidad, promedio));
         }
        
        ServiciosMasReservados serviciosMasReservados = new ServiciosMasReservados(listaDeFilas, inicio, fin);
        
        return serviciosMasReservados.aceptar(formato);
    }
    
    private int calcularCantidad(Item item, List<Reserva> reservas) {
        int cantidad = 0;
        for (Reserva r : reservas) {
            if (r.getItems().contains(item)) {
                cantidad++;
            }
        }
        return cantidad;
    }
    
    private double calcularPrecioPromedio(Item item, List<Reserva> reservas) {
        int cantidad = 0;
        double acumuladorPrecios = 0;
        for (Reserva r : reservas) {
            if (r.getItems().contains(item)) {
                cantidad++;
                acumuladorPrecios = acumuladorPrecios + r.getCostoItem();
            }
        }
        if (cantidad == 0) {
            return 0.0;
        }
        return acumuladorPrecios / cantidad;
    }
    
    
}
