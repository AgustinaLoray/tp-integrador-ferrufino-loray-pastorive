package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import java.util.ArrayList;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class CriterioNOT extends CriterioCompuesto {

    public CriterioNOT() {
        super();
        this.criterios = new ArrayList<>();
    }

    @Override 
    public boolean esSatisfechoPor(Item item){
        return !this.criterios.stream().anyMatch(criterio -> criterio.esSatisfechoPor(item));
    }
}
