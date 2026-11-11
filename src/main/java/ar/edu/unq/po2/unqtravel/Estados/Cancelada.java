package ar.edu.unq.po2.unqtravel.Estados;

public class Cancelada extends Estado {

    @Override 
    public boolean debeNotificarClub() {
        return true;
    }
}