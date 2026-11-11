package ar.edu.unq.po2.unqtravel.pagos;

public abstract class MetodoDePago {
    public final void procesarPago(double monto){
        this.validarDatos(monto);
        this.reservarFondos(monto);
        this.ejecutarTransaccion(monto);
        this.notificarResultado();
    }

    protected abstract void validarDatos(double monto);
    protected abstract void reservarFondos(double monto);
    protected abstract void ejecutarTransaccion(double monto);
    
    protected void notificarResultado(){

    }
}
