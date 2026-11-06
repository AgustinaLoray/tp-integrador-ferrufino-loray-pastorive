package ar.edu.unq.po2.unqtravel.Estados;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class SolicitudPendiente extends Estado {

    @Override
    public void agregarItem(Reserva reserva, Item item) {
        reserva.agregarItemDirecto(item);
    }

    @Override
    public void quitarItem(Reserva reserva, Item item) {
        reserva.quitarItemDirecto(item);
    }

    @Override
    public void confirmar(Reserva reserva) {
        reserva.bloquearCupoDisponible();
        reserva.cambiarEstado(new Confirmada());
    }

    @Override
    public void cancelar(Reserva reserva) {
        reserva.cambiarEstado(new Cancelada());
    }
}
