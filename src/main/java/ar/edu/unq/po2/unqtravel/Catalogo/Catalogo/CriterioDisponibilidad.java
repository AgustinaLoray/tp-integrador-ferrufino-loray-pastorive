package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class CriterioDisponibilidad implements CriterioBusqueda {

    @Override 
    public boolean esSatisfechoPor(Item item){
        return item.tieneCupoDisponible();
    }
    
}
