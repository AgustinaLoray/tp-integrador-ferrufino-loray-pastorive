package ar.edu.unq.po2.unqtravel.Formato;

import java.time.LocalDate;
import java.util.List;

public class CupoDisponible implements Reporte{
	
	private List<ItemCupo> listaCupos;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public CupoDisponible(List<ItemCupo> listaCupos, LocalDate fechaInicio, LocalDate fechaFin) {
        this.listaCupos = listaCupos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public List<ItemCupo> getListaCupos() {
        return listaCupos;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
	@Override
	public String aceptar(Formato f) {
		return f.visitarCupoDisponible(this);
	}
}
