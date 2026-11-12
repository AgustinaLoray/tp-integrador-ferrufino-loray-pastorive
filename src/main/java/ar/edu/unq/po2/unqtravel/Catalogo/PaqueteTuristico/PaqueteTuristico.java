package ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import java.util.ArrayList;
import java.util.List;

public class PaqueteTuristico implements Item {
    private String nombre;
    private String descripcion;
    private Double descuento;
    private List<Item> items;
    private String categoria;
    private int cupos;
    private int cuposTotales;
    private String operador;

    public PaqueteTuristico(String n, String des, Double d, String categoria, int cuposTotales) {
        this.nombre = n;
        this.descripcion = des;
        this.descuento = validarDescuento(d);
        this.items = new ArrayList<>();
        this.categoria = categoria;
        this.cupos = cuposTotales;
        this.cuposTotales = cuposTotales;
    }

    public int getCuposDisponibles() {
        return cupos;
    }

    public int getCuposTotales() {
        return cuposTotales;
    }

    public boolean tieneCupoDisponible(){
        return this.cupos > 0;
    }

    public void agregarItem(Item item) {
        if (item == null) {
            throw new Excepciones("El item no puede ser null");
        }
        this.items.add(item);
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
                this.cupos--;
                reservados.add(item);
            }
        } catch (RuntimeException error) {
            for (int i = reservados.size() - 1; i >= 0; i--) {
                reservados.get(i).incrementarCupo();
                this.cupos++;
            }
            throw new Excepciones("No se pudo reservar el cupo de todos los items del paquete.", error);
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
            throw new Excepciones("No se pudo liberar el cupo de todos los items del paquete.", error);
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
            throw new Excepciones("El descuento debe estar entre 0 y 1");
        }
        return descuentoValidado;
    }
    
    // metodos para formatos
    
    public int getCupos() {
    	return cupos;
    }
    
    public String getCategoria() {
    	return categoria;
    }
    
    public String getOperador() {
    	return operador;
    }
}