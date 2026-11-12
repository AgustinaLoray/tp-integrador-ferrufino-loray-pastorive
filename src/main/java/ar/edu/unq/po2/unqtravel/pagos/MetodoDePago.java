package ar.edu.unq.po2.unqtravel.pagos;

import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;

public abstract class MetodoDePago {
    public final void procesarPago(double monto){
        ejecutarPaso("validar los datos del pago", () -> this.validarDatos(monto));
        ejecutarPaso("reservar los fondos", () -> this.reservarFondos(monto));
        ejecutarPaso("ejecutar la transacción", () -> this.ejecutarTransaccion(monto));
        try {
            this.notificarResultado();
        } catch (RuntimeException error) {
            throw new NotificacionPagoException(
                    "El pago se ejecutó, pero no se pudo notificar el resultado.",
                    error);
        }
    }

    private void ejecutarPaso(String descripcion, Runnable paso) {
        try {
            paso.run();
        } catch (RuntimeException error) {
            throw new Excepciones("No se pudo " + descripcion + ".", error);
        }
    }

    protected abstract void validarDatos(double monto);
    protected abstract void reservarFondos(double monto);
    protected abstract void ejecutarTransaccion(double monto);
    
    protected void notificarResultado(){

    }
}
