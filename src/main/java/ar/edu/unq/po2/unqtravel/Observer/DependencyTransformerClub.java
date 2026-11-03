package ar.edu.unq.po2.unqtravel.Observer;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Estados.Estado;

public class DependencyTransformerClub implements ObserverReserva {
    
    private ObserverClub club;

    public DependencyTransformerClub(ObserverClub club) {
        this.club = club;
    }

    @Override 
    public void notificar(Reserva r) {
        if (this.debeNotificar(r.getEstado())) {
            club.notificar();
        }
    }

    public boolean debeNotificar(Estado e) {
        return (e.debeNotificarClub());
    }
}
