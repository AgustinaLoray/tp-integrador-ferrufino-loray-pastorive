package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;

public class Catalogo {

    private List<Item> items;

    public Catalogo(List<Item> lista) {
        if (lista == null) {
            throw new Excepciones("La lista de items no puede ser null");
        }
        this.items = lista;
    }

    public Catalogo() { 
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return List.copyOf(this.items);
    }

    public void agregarItem(Item item) {
        if (item == null) {
            throw new Excepciones("El item no puede ser null");
        }
        this.items.add(item);
    }

    public List<Item> buscar(CriterioBusqueda criterio) {
        if (criterio == null) {
            throw new Excepciones("El criterio de búsqueda no puede ser null");
        }
        Stream<Item>  itemsFiltrados =  this.items.stream().filter(item -> criterio.esSatisfechoPor(item));
        return itemsFiltrados.toList();
    }

}
