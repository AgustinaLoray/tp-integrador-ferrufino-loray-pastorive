package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class CriterioPrecioMaximo implements CriterioBusqueda{
    private double precioMaximo;

    public CriterioPrecioMaximo(double precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    @Override
    public boolean esSatisfechoPor(Item item) {
        return item.getPrecioFinal() <= this.precioMaximo;
    }
}
