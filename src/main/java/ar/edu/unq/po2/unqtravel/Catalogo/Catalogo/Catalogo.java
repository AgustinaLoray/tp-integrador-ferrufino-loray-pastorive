package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class Catalogo {

    private List<Item> items;

    public Catalogo(List<Item> lista) {
        this.items = lista;
    }

    public Catalogo() { 
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return List.copyOf(this.items);
    }

    public void agregarItem(Item item) {
        this.items.add(item);
    }

    public List<Item> buscar(CriterioBusqueda criterio) {
        Stream<Item>  itemsFiltrados =  this.items.stream().filter(item -> criterio.esSatisfechoPor(item));
        return itemsFiltrados.toList();
    }

}
