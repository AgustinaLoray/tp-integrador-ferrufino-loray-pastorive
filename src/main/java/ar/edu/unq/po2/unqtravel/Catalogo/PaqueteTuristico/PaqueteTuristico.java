package ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaqueteTuristico implements Item {
    private String nombre;
    private String descripcion;
    private Double descuento;
    private List<Item> items;
    private String categoria;
    private int cuposDisponibles;

    public PaqueteTuristico(String n, String des, Double d, String categoria, int cuposDisponibles) {
        this.nombre = n;
        this.descripcion = des;
        this.descuento = validarDescuento(d);
        this.items = new ArrayList<>();
        this.categoria = categoria;
        this.cuposDisponibles = cuposDisponibles;
    }

    public String getCategoria(){
        return this.categoria;
    }

    public int getCuposDisponibles() {
        return cuposDisponibles;
    }

    public boolean tieneCupoDisponible(){
        return this.cuposDisponibles > 0;
    }

    public void agregarItem(Item item) {
        this.items.add(Objects.requireNonNull(item, "El item no puede ser null"));
    }

    public void quitarItem(Item item) {
        this.items.remove(item);
    }

    @Override
    public void decrementarCupo() {
        List<Item> reservados = new ArrayList<>();
        try {
            for (Item item : this.items) {
                item.decrementarCupo();
                reservados.add(item);
            }
        } catch (RuntimeException error) {
            for (int i = reservados.size() - 1; i >= 0; i--) {
                reservados.get(i).incrementarCupo();
            }
            throw error;
        }
    }

    @Override
    public void incrementarCupo() {
        List<Item> liberados = new ArrayList<>();
        try {
            for (Item item : this.items) {
                item.incrementarCupo();
                liberados.add(item);
            }
        } catch (RuntimeException error) {
            for (int i = liberados.size() - 1; i >= 0; i--) {
                liberados.get(i).decrementarCupo();
            }
            throw error;
        }
    }

    public List<Item> getItems() {
        return List.copyOf(this.items);
    }

    public Double getDescuento() {
        return this.descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = validarDescuento(descuento);
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
    public double getPrecioBase() {
        return this.items.stream().mapToDouble(i -> i.getPrecioBase()).sum();
    }

    @Override
    public double getPrecioFinal() {
        double sumaPrecioFinalHijos = this.items.stream().mapToDouble(i -> i.getPrecioFinal()).sum();
        double descuentoFinal = 1.0 - (this.descuento != null ? this.descuento : 0.0);
        return sumaPrecioFinalHijos * descuentoFinal;
    }

    private double validarDescuento(Double descuento) {
        double descuentoValidado = descuento != null ? descuento : 0.0;
        if (descuentoValidado < 0 || descuentoValidado > 1) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 1");
        }
        return descuentoValidado;
    }
}