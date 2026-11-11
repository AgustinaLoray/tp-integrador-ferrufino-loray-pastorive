package ar.edu.unq.po2.unqtravel.Estados;

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