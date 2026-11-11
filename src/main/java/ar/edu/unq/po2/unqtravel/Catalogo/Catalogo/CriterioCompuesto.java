package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import java.util.List;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public abstract class CriterioCompuesto {
    
    protected List<CriterioBusqueda> criterios;

    public void borrarCriterio(CriterioBusqueda c) {
        this.criterios.remove(c);
    }

    public void agregarCriterio(CriterioBusqueda c) {
        this.criterios.add(c);
    }

    public abstract boolean esSatisfechoPor(Item item);
}
