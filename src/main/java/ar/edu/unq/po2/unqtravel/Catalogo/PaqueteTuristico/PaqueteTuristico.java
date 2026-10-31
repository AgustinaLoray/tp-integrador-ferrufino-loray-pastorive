package ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

import java.util.ArrayList; 
import java.util.List;

public class PaqueteTuristico implements Item {
    private String nombre;
    private String descripcion;
    private Double descuento;
    private List<Item> items;

    public PaqueteTuristico (String n, String des, Double d) {
        this.nombre = n;
        this.descripcion = des;
        this.descuento = d;
        this.items = new ArrayList<>();
    }

    public void agregarItem(Item item) {
        this.items.add(item);
    }

    public void quitarItem(Item item) {
        this.items.remove(item);
    }

    public List<Item> getItems() {
        return this.items;
    }

    public Double getDescuento() {
        return this.descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento != null ? descuento : 0.0;    
    }

    @Override
    public String getNombre() {
        return this.nombre;
    }

    @Override
    public String getDescripcion() {
        return this.descripcion;
    }

    @Override
    public double getPrecioBase(){
        return this.items.stream().mapToDouble(i -> i.getPrecioBase()).sum();
    }

    @Override
    public double getPrecioFinal(){
        double sumaPrecioFinalHijos = this.items.stream().mapToDouble(i -> i.getPrecioFinal()).sum();
        double descuentoFinal = 1.0 - (this.descuento != null ? this.descuento : 0.0);
        return sumaPrecioFinalHijos * descuentoFinal;
    }
}