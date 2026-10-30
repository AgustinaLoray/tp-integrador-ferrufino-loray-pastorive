import ar.edu.unq.po2.unqtravel.catalogo.Reserva;

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
        // generar reembolso de los servicios no disfrutados 
        reserva.cambiarEstado(new Cancelada(reserva));
    }
}
