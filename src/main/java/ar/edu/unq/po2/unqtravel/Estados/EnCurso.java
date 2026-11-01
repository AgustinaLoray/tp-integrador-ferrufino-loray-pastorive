package ar.edu.unq.po2.unqtravel.Estados;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class EnCurso extends Estado {

    public EnCurso(Reserva reserva) {
        super(reserva);
    }

    @Override
    public void finalizar() {
        reserva.cambiarEstado(new Finalizada(reserva));
    }

    @Override
    public void cancelar() {
        this.reserva.liberarCupos(); 
        double reembolso = this.reserva.getCostoServicios();
        this.reserva.generarVoucherDeReembolso(reembolso);
        reserva.cambiarEstado(new Cancelada(reserva));
    }
}
