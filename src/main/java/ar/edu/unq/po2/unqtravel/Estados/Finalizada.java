package ar.edu.unq.po2.unqtravel.Estados;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class Finalizada extends Estado {

    @Override 
    public boolean debeNotificarEmail() {
        return true;
    }

    @Override 
    public boolean debeNotificarFactura(){
        return true;
    }
}