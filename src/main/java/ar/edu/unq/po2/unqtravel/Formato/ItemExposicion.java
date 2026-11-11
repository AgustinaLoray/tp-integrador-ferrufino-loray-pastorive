package ar.edu.unq.po2.unqtravel.Formato;

public class ItemExposicion {
	private String operador;
    private double cantidadItemsEquivalentes;
    private double porcentaje;

    public ItemExposicion(String operador, double cantidadItemsEquivalentes, double porcentaje) {
        this.operador = operador;
        this.cantidadItemsEquivalentes = cantidadItemsEquivalentes;
        this.porcentaje = porcentaje;
    }

    public String getOperador() { return operador; }
    public double getCantidadItemsEquivalentes() { return cantidadItemsEquivalentes; }
    public double getPorcentaje() { return porcentaje; }
}
