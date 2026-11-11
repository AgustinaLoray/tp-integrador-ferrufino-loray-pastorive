package ar.edu.unq.po2.unqtravel.Traslado;
import ar.edu.unq.po2.unqtravel.Reserva.*;
public class PuntoEncuentro implements MetodoTraslado {
    private int cuposDeGuia;

    public PuntoEncuentro(int cuposDeGuia) {
        this.cuposDeGuia = cuposDeGuia;
    }

    public double calcularCosto(Reserva r) {
        return 0;
    }

    public boolean hayCuposDisponibles() {
        return this.cuposDeGuia > 0;
    }
}