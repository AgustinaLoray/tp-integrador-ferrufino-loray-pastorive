package ar.edu.unq.po2.unqtravel.Traslado;
import ar.edu.unq.po2.unqtravel.Reserva.*;
public class PuntoEncuentro implements MetodoTraslado {
    private Double costoFijo;

    public PuntoEncuentro(Double costoFijo) {
        this.costoFijo = costoFijo;
    }

    public double calcularCosto(Reserva r) {
        return this.costoFijo;
    }
}