package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import java.util.ArrayList;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class CriterioOR extends CriterioCompuesto {

    public CriterioOR() {
        super();
        this.criterios = new ArrayList<>();
    }

    @Override 
    public boolean esSatisfechoPor(Item item){
        return this.criterios.stream().anyMatch(criterio -> criterio.esSatisfechoPor(item));
    }

}
