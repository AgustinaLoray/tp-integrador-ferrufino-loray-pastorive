package ar.edu.unq.po2.unqtravel.Traslado;
import ar.edu.unq.po2.unqtravel.Reserva.*;

public class TrasladoEstandarAdapter implements MetodoTraslado {
    private Direccion direccionDestino;
    private PlataformaTransporte plataforma;

    public TrasladoEstandarAdapter(Direccion direccionDestino, PlataformaTransporte plataforma) {
        this.direccionDestino = direccionDestino;
        this.plataforma = plataforma;
    }

  
    public double calcularCosto(Reserva r) {
        int pasajeros = r.getCantidadPasajeros();
        return plataforma.estimarTraslado(pasajeros, this.direccionDestino);
    }
}