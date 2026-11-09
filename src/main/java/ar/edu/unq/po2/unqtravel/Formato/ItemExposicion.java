package ar.edu.unq.po2.unqtravel.Formato;

public class ItemExposicion {
	private String operador;
    private int cantidadItems;
    private double porcentaje;

    public ItemExposicion(String operador, int cantidadItems, double porcentaje) {
        this.operador = operador;
        this.cantidadItems = cantidadItems;
        this.porcentaje = porcentaje;
    }

    public String getOperador() { return operador; }
    public int getCantidadItems() { return cantidadItems; }
    public double getPorcentaje() { return porcentaje; }
}
