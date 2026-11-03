package ar.edu.unq.po2.unqtravel.Observer;

import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Estados.Estado;

public class DependencyTransformerEmail implements ObserverReserva {

    private ObserverEmail email;

    public DependencyTransformerEmail(ObserverEmail email) {
        this.email = email;
    }

    @Override 
    public void notificar(Reserva r) {
        if (debeNotificar(r.getEstado())) {
            this.email.notificar();
        }
    }

    public boolean debeNotificar(Estado e) {
        return (e.debeNotificarEmail());
    }

}
