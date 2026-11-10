package ar.edu.unq.po2.unqtravel.Formato;

public class ItemRanking {
	private String nombreServicio;
    private int cantidadVendida;
    private double precioPromedio;

    public ItemRanking(String nombreServicio, int cantidadVendida, double precioPromedio) {
        this.nombreServicio = nombreServicio;
        this.cantidadVendida = cantidadVendida;
        this.precioPromedio = precioPromedio;
    }

    public String getNombreServicio() { 
    	return nombreServicio; 
   }
   
    public int getCantidadVendida() { 
    	return cantidadVendida; 
    	 }
    
    public double getPrecioPromedio() { 
    	return precioPromedio; 
    	}
}
