package ar.edu.unq.po2.unqtravel.Estados;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class CheckIn extends Estado {

    public CheckIn(Reserva reserva) {
        super(reserva);
    }

    @Override
    public void iniciarViaje() {
        reserva.cambiarEstado(new EnCurso(reserva));
    }

    @Override
    public void cancelar() {
        reserva.liberarCupos();
        // reembolso = servicios + tasas
        reserva.cambiarEstado(new Cancelada(reserva));
    }
}
