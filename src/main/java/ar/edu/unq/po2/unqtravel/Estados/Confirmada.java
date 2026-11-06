package ar.edu.unq.po2.unqtravel.Estados;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class Confirmada extends Estado {

    @Override
    public void checkIn(Reserva reserva) {
        reserva.cambiarEstado(new CheckIn());
    }
    
    @Override
    public void cancelar(Reserva reserva) {
        reserva.liberarCupos();
        reserva.cambiarEstado(new Cancelada());
    }

    @Override 
    public boolean debeNotificarEmail() {
        return true;
    }
}