package ar.edu.unq.po2.unqtravel.catalogo.ServicioIndividual;

public class ServicioIndividual implements Item {
    private int id;
    private String nombre;
    private String proveedor;
    private String categoria;
    private String descripcion;
    private Double precioBase;
    private Double descuento;
    private int cupoMaximo; // agregar al UML
    private List<ValorAtributo> atributos;

    public ServicioIndividual(int id, String nombre, int cupoMaximo, String proveedor, String categoria, String descripcion, Double precioBase, Double descuento) {
        this.id = id;
        this.nombre = nombre;
        this.cupoMaximo = cupoMaximo;
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.descuento = descuento;
        this.atributos = new ArrayList<>();
    }

    public void agregarAtributo(ValorAtributo atributo) {
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

    public String getCategoria() {
        return categoria;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }


}
