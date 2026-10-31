package ar.edu.unq.po2.unqtravel.catalogo.Estados;

import ar.edu.unq.po2.unqtravel.catalogo.Item;
import ar.edu.unq.po2.unqtravel.catalogo.Reserva.Reserva;

public class SolicitudPendiente extends Estado {

    public SolicitudPendiente(Reserva reserva) {
        super(reserva);
    }

    @Override
    public void agregarItem(Item item) {
        this.reserva.agregarItemPendiente(item);
    }

    @Override
    public void quitarItem(Item item) {
        this.reserva.quitarItemPendiente(item);
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
