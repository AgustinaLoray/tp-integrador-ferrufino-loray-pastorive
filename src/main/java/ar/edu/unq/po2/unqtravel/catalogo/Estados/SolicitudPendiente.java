import ar.edu.unq.po2.unqtravel.catalogo.Reserva;

public class SolicitudPendiente extends Estado {

    public SolicitudPendiente(Reserva reserva) { 
        super(reserva); 
    }

    @Override
    public void agregarItem(Item item) {
        this.reserva.add(item);
    }

    @Override
    public void quitarItem(Item item) {
        this.reserva.remove(item);
    }

    @Override
    public void confirmar() {
        reserva.bloquearCupoDisponible();
        reserva.cambiarEstado(new Confirmada(reserva));
    }

    @Override
    public void cancelar() {
        reserva.cambiarEstado(new Cancelada(reserva));
    }
}
