package ar.edu.unq.po2.unqtravel.Catalogo.ServicioIndividual;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Catalogo.atributos.ValorAtributo;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import java.util.ArrayList;
import java.util.List;

public class ServicioIndividual implements Item {
    private int id;
    private String nombre;
    private String proveedor;
    private String categoria;
    private String descripcion;
    private Double precioBase;
    private Double descuento;
    private int cupoMaximo;
    private int cuposDisponibles;
    private List<ValorAtributo> atributos;

    public ServicioIndividual(int id, String nombre, int cupoMaximo, String proveedor, String categoria, String descripcion, Double precioBase, Double descuento) {
        if (cupoMaximo < 0) {
            throw new Excepciones("Se alcanzo el limite de cupos");
        }

        if (precioBase == null) {
            throw new Excepciones("El precio base no puede ser null");
        }
        if (precioBase < 0) {
            throw new Excepciones("El precio base no puede ser negativo");
        }

        double descuentoValidado = descuento != null ? descuento : 0.0;
        if (descuentoValidado < 0 || descuentoValidado > 1) {
            throw new Excepciones("El descuento debe estar entre 0 y 1");
        }
        
        this.id = id;
        this.nombre = nombre;
        this.cupoMaximo = cupoMaximo;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.descuento = descuentoValidado;
        this.cuposDisponibles = cupoMaximo;
        this.atributos = new ArrayList<>();
    }

    public int getCuposDisponibles() {
        return cuposDisponibles;
    }
    
    public int getCuposTotales() {
        return cupoMaximo;
    }

    public void agregarAtributo(ValorAtributo atributo) {
        if (atributo == null) {
            throw new Excepciones("Se debe ingresar un atributo");
        }
        this.atributos.add(atributo);
    }

    public boolean esValido() {
        return this.atributos.stream().allMatch(atributo -> atributo.esValido());
    }

    public int getId() {
        return id;
    }

    public String getProveedor() {
        return proveedor;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public boolean tieneCupoDisponible(){
        return this.cuposDisponibles > 0;
    }

    public List<ValorAtributo> getAtributos() {
        return List.copyOf(this.atributos);
    }

    @Override
    public String getNombre() {
        return this.nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public double getPrecioBase() {
        return this.precioBase;
    }

    @Override
    public double getPrecioFinal() {
        double porcentajeDescuento = this.descuento != null ? this.descuento : 0.0;
        return this.precioBase * (1.0 - porcentajeDescuento);
    }

    @Override
    public void decrementarCupo() {
        if (this.cuposDisponibles == 0) {
            throw new Excepciones("No hay cupos disponibles para " + this.nombre);
        }
        this.cuposDisponibles--;
    }

    @Override
    public void incrementarCupo() {
        if (this.cuposDisponibles >= this.cupoMaximo) {
            throw new Excepciones("No hay cupos reservados para liberar en " + this.nombre);
        }
        this.cuposDisponibles++;
    }
    
    
    // Metodos para formato
    
    public String getOperador() {
        return proveedor;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public int getCupos() {
        return cuposDisponibles;
    }

    
}
