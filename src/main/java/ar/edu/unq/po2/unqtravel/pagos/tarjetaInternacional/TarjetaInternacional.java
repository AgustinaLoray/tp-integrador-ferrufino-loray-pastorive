package ar.edu.unq.po2.unqtravel.pagos.tarjetaInternacional;

import ar.edu.unq.po2.unqtravel.pagos.MetodoDePago;

public class TarjetaInternacional extends MetodoDePago {
    private ApiTarjetaInternacional api;
    private int numero;
    private int CVV;
    private java.util.Date fechaVencimiento;
    
    public TarjetaInternacional(ApiTarjetaInternacional api, int numero, int CVV, java.util.Date fechaVencimiento) {
        this.api = api;
        this.numero = numero;
        this.CVV = CVV;
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override 
    public void validarDatos(double monto) {
        this.api.validar(this.numero, this.CVV, this.fechaVencimiento);
    }

    @Override 
    public void reservarFondos(double monto){
        this.api.solicitarPreAutorizacion(monto);
    }

    @Override
    public void ejecutarTransaccion(double monto){
        this.api.ejecutarTransaccion(monto);
    }

    @Override 
    public void notificarResultado(){
        Object voucher = this.api.generarVoucher();
        this.api.registrarVoucher(voucher);
    }
}
