package ar.edu.unq.po2.unqtravel.catalogo;

public interface Item {
    public String getNombre();
    public String getDescripcion();
    public double getPrecioBase();
    public double getPrecioFinal();

    //public void decrementarCupos(); 
    //si el estado de la reserva pasa a confirmada 
}