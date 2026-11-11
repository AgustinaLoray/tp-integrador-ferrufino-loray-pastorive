package ar.edu.unq.po2.unqtravel.pagos;

public abstract class MetodoDePago {
    public final void procesarPago(double monto){
        this.validarDatos(monto);
        this.reservarFondos(monto);
        this.ejecutarTransaccion(monto);
        try {
            this.notificarResultado();
        } catch (RuntimeException error) {
            throw new NotificacionPagoException(
                    "El pago se ejecutó, pero no se pudo notificar el resultado.",
                    error);
        }
    }

    protected abstract void validarDatos(double monto);
    protected abstract void reservarFondos(double monto);
    protected abstract void ejecutarTransaccion(double monto);
    
    protected void notificarResultado(){

    }
}
