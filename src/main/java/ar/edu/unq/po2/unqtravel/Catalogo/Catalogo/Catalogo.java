package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.CriterioBusqueda;


public class Catalogo {

    private List<Item> items = new ArrayList<>();
    private CriterioBusqueda criterio;

    public List<Item> getItems() {
        return List.copyOf(this.items);
    }

    public void agregarItem(Item item) {
        this.items.add(item);
    }

    public List<Item> buscar(CriterioBusqueda criterio) {
        // return items.stream().filter(criterio :: esSatisfechoPor())
    }

}
