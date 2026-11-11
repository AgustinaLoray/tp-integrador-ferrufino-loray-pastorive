package ar.edu.unq.po2.unqtravel.Traslado;
import ar.edu.unq.po2.unqtravel.Reserva.*;

public class TrasladoVipAdapter implements MetodoTraslado {
    private final ApiTrasladoVip api;

    public TrasladoVipAdapter(ApiTrasladoVip api) {
        this.api = api;
    }

    @Override
    public double calcularCosto(Reserva reserva) {
        return this.api.calcularCosto((float) reserva.getCostoServicios());
    }
}