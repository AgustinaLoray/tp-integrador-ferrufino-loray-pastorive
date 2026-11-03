package ar.edu.unq.po2.unqtravel.Observer;

public class ObserverClub {
    
    public void notificar() {
        this.enviarCuponDescuento();
    }
    
    public void enviarCuponDescuento(){
        System.out.println("Cupon de descuento del 5% enviado al cliente por cancelacion");
    }
}
