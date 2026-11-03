package ar.edu.unq.po2.unqtravel.Observer;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Estados.Estado;

public class DependencyTransformerFactura implements ObserverReserva {
    
    private ObserverFactura factura;

    public DependencyTransformerFactura(ObserverFactura factura) {
        this.factura = factura;
    }

    @Override
    public void notificar(Reserva r) {
        if (this.debeNotificar(r.getEstado())) {
            this.factura.notificar();
        }
    }

    public boolean debeNotificar(Estado e) {
        return (e.debeNotificarFactura());
    }
    
}
