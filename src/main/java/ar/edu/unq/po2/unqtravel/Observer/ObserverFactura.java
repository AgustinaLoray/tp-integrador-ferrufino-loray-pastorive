package ar.edu.unq.po2.unqtravel.Observer;

public class ObserverFactura {
    
    public void notificar(){
        this.generarFactura();
    }

    public void generarFactura(){
        System.out.println("Comprobante fiscal y voucher generados con exito para la reserva");
    }
}
