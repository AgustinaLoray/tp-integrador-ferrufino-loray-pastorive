package ar.edu.unq.po2.unqtravel.Catalogo.Item;

public interface Item {
    String getNombre();
    String getDescripcion();
    double getPrecioBase();
    double getPrecioFinal();
    void decrementarCupo();
    void incrementarCupo();
}