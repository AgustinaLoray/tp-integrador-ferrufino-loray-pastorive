package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import java.util.ArrayList;
import java.util.List;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class CriterioAND extends CriterioCompuesto {

    private List<CriterioBusqueda> criterios;

    public CriterioAND(){
        this.criterios = new ArrayList<>();
    }
    
    @Override 
    public boolean esSatisfechoPor(Item item){
        return this.criterios.stream().allMatch(criterio -> criterio.esSatisfechoPor(item));
    }
}
