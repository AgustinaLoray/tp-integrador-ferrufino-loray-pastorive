package ar.edu.unq.po2.unqtravel.Traslado;
import ar.edu.unq.po2.unqtravel.Reserva.*;

public class TrasladoVipAdapter implements MetodoTraslado {
    private TrasladoVip libreriaVip; 

    public TrasladoVipAdapter(TrasladoVip libreriaVip) {
        this.libreriaVip = libreriaVip;
    }

    
    public double calcularCosto(Reserva r) {
     
        float valorTotalReserva = r.getValorTotal();
        
      
        return this.libreriaVip.calcularCosto(valorTotalReserva);
    }
}