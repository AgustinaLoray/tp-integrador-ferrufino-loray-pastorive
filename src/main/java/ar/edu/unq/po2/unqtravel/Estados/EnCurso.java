package ar.edu.unq.po2.unqtravel.Estados;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class EnCurso extends Estado {

    @Override
    public void finalizar(Reserva reserva) {
        reserva.cambiarEstado(new Finalizada());
    }

     @Override
    public void cancelar(Reserva reserva) {
        reserva.liberarCupos();
        double reembolso = reserva.getCostoServicios();
        reserva.generarVoucherDeReembolso(reembolso);
        reserva.cambiarEstado(new Cancelada());
    }

    @Override 
    public boolean debeNotificarEmail() {
        return true;
    }
}
