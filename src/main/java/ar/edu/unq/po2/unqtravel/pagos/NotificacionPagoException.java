package ar.edu.unq.po2.unqtravel.pagos;

public class NotificacionPagoException extends RuntimeException {
    public NotificacionPagoException(String mensaje, RuntimeException causa) {
        super(mensaje, causa);
    }
}
