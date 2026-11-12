package ar.edu.unq.po2.unqtravel.Excepciones;

public class Excepciones extends RuntimeException {

    public Excepciones(String mensaje){
        super(mensaje);
    }

    public Excepciones(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
}
