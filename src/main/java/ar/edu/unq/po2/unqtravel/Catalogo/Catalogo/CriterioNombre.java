package ar.edu.unq.po2.unqtravel.Catalogo.Catalogo;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class CriterioNombre implements CriterioBusqueda {
    private String textoBuscado;

    public CriterioNombre(String textoBuscado) {
        this.textoBuscado = textoBuscado.toLowerCase();
    }

    @Override
    public boolean esSatisfechoPor(Item item) {
        return item.getNombre().toLowerCase().contains(textoBuscado);
    }

}