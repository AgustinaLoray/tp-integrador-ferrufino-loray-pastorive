package ar.edu.unq.po2.unqtravel.catalogo;

public interface Item {
    String getNombre();
    String getDescripcion();
    double getPrecioBase();
    double getPrecioFinal();
    void decrementarCupo();
    void incrementarCupo();
}