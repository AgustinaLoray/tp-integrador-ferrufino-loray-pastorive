package ar.edu.unq.po2.unqtravel.pagos.transferenciaDirecta;

import ar.edu.unq.po2.unqtravel.pagos.MetodoDePago;

public class TransferenciaDirecta extends MetodoDePago {
    private ApiTransferenciaDirecta api;
    private String alias;
    
    public TransferenciaDirecta(ApiTransferenciaDirecta api, String alias) {
        this.api = api;
        this.alias = alias;
    }

    @Override 
    public void validarDatos(double monto) {
        this.api.validar(this.alias);
    }

    @Override 
    public void reservarFondos(double monto){}

    @Override
    public void ejecutarTransaccion(double monto){
        this.api.ejecutarTransaccion();
    }

    @Override 
    public void notificarResultado(){
        Object comprobante = this.api.generarComprobante();
        this.api.registrarComprobante(comprobante);
    }
}
