package ar.edu.unq.po2.unqtravel.Catalogo.ServicioIndividual;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Catalogo.atributos.ValorAtributo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            throw new IllegalArgumentException("Se alcanzo el limite de cupos");
        }

        if (precioBase == null) {
            throw new IllegalArgumentException("El precio base no puede ser null");
        }
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio base no puede ser negativo");
        }

        double descuentoValidado = descuento != null ? descuento : 0.0;
        if (descuentoValidado < 0 || descuentoValidado > 1) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 1");
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

    public void agregarAtributo(ValorAtributo atributo) {
        this.atributos.add(Objects.requireNonNull(atributo, "Se debe ingresar un atributo"));
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

    public String getCategoria() {
        return categoria;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public int getCuposDisponibles() {
        return cuposDisponibles;
    }

    public List<ValorAtributo> getAtributos() {
        return List.copyOf(this.atributos);
    }

    @Override
    public String getNombre() {
        return nombre;
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
            throw new IllegalStateException("No hay cupos disponibles para " + this.nombre);
        }
        this.cuposDisponibles--;
    }

    @Override
    public void incrementarCupo() {
        if (this.cuposDisponibles >= this.cupoMaximo) {
            throw new IllegalStateException("No hay cupos reservados para liberar en " + this.nombre);
        }
        this.cuposDisponibles++;
    }
}
