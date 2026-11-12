package ar.edu.unq.po2.unqtravel.pagos;

import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;

public class NotificacionPagoException extends Excepciones {
    public NotificacionPagoException(String mensaje, RuntimeException causa) {
        super(mensaje, causa);
    }
}
