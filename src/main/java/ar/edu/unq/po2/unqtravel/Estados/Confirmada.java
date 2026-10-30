import ar.edu.unq.po2.unqtravel.catalogo.Reserva;

public class Confirmada extends Estado {

    public Confirmada(Reserva reserva) { 
        super(reserva); 
    }

    @Override
    public void checkIn() {
        reserva.cambiarEstado(new CheckIn(reserva));
    }

    @Override
    public void cancelar() {
        reserva.liberarCupos();
        reserva.cambiarEstado(new Cancelada(reserva));
    }
}