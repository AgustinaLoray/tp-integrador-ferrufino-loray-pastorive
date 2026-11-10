package ar.edu.unq.po2.unqtravel.Estados;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class CheckIn extends Estado {

    @Override
    public void iniciarViaje(Reserva reserva) {
        reserva.cambiarEstado(new EnCurso());
    }

    @Override
    public void cancelar(Reserva reserva) {
        reserva.liberarCupos();
        double reembolso = reserva.getCostoServicios() + reserva.getCostoTasas();
        reserva.generarVoucherDeReembolso(reembolso);
        reserva.cambiarEstado(new Cancelada());
    }
}
