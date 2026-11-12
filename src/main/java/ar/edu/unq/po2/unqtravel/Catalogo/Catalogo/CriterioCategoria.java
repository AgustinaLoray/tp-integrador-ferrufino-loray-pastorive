package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;

public class CriterioCategoria implements CriterioBusqueda {
    private String categoria;

    public CriterioCategoria(String categoria){
        if (categoria == null) {
            throw new Excepciones("La categoría no puede ser null");
        }
        this.categoria = categoria;
    }

    @Override 
    public boolean esSatisfechoPor(Item item){
        return item.getCategoria().equalsIgnoreCase(this.categoria);
    }
}
