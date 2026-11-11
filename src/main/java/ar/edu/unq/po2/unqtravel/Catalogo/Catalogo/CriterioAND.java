package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import java.util.ArrayList;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class CriterioAND extends CriterioCompuesto {


    public CriterioAND(){
        super();
        this.criterios = new ArrayList<>();
    }
    
    @Override 
    public boolean esSatisfechoPor(Item item){
        return this.criterios.stream().allMatch(criterio -> criterio.esSatisfechoPor(item));
    }
}
