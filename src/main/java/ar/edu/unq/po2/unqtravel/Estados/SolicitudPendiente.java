package ar.edu.unq.po2.unqtravel.Estados;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class SolicitudPendiente extends Estado {

    public SolicitudPendiente(Reserva reserva) {
        super(reserva);
    }

    @Override
    public void agregarItem(Item item) {
        this.reserva.agregarItemDirecto(item);
    }

    @Override
    public void quitarItem(Item item) {
        this.reserva.quitarItemDirecto(item);
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
