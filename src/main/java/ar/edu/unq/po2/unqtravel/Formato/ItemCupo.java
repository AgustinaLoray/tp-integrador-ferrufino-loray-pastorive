package ar.edu.unq.po2.unqtravel.Formato;

public class ItemCupo {
	private String categoria;
    private int cuposTotales;
    private int cuposReservados;

    public ItemCupo(String categoria, int cuposTotales, int cuposReservados) {
        this.categoria = categoria;
        this.cuposTotales = cuposTotales;
        this.cuposReservados = cuposReservados;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getCuposTotales() {
        return cuposTotales;
    }

    public int getCuposReservados() {
        return cuposReservados;
    }

    // Los cupos libres se calculan restando los totales menos los reservados
    public int getCuposLibres() {
        return cuposTotales - cuposReservados;
    }
}
