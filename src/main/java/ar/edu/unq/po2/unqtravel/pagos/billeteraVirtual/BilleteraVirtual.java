package ar.edu.unq.po2.unqtravel.pagos.billeteraVirtual;

import ar.edu.unq.po2.unqtravel.pagos.MetodoDePago;

public class BilleteraVirtual extends MetodoDePago {
    private ApiBilleteraVirtual api;

    public BilleteraVirtual(ApiBilleteraVirtual api) {
        this.api = api;
    }

    @Override 
    public void validarDatos(double monto) {
        this.api.validar(monto);
    }

    @Override 
    public void reservarFondos(double monto){
        this.api.bloquearFondos(monto);
    }

    @Override
    public void ejecutarTransaccion(double monto){
        this.api.ejecutarTransaccion(monto);
    }

    @Override 
    public void notificarResultado(){
        this.api.notificarAlUsuario();
    }
}
