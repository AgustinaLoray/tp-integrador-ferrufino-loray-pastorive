package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class CriterioCategoria implements CriterioBusqueda {
    private String categoria;

    public CriterioCategoria(String categoria){
        this.categoria = categoria;
    }

    @Override 
    public boolean esSatisfechoPor(Item item){
        return item.getCategoria() == this.categoria;
    }
}
