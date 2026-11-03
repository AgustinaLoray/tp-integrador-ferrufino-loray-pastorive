package ar.edu.unq.po2.unqtravel.Estados;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class Cancelada extends Estado {
    
    public Cancelada(Reserva reserva) {
        super(reserva);
    }

    @Override 
    public boolean debeNotificarClub() {
        return true;
    }
}