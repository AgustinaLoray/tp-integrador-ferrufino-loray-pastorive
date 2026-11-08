package ar.edu.unq.po2.unqtravel.Formato;

import java.time.LocalDate;
import java.util.List;

public class ServiciosMasReservados implements Reporte {
    private List<ItemRanking> rankingServicios; 
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public ServiciosMasReservados(List<ItemRanking> ranking, LocalDate inicio, LocalDate fin) {
        this.rankingServicios = ranking;
        this.fechaInicio = inicio;
        this.fechaFin = fin;
    }
	
	@Override
	public String aceptar(Formato f) {
		return f.visitarSMR(this);
	}
	
	// Getters para que el formato pueda leer los datos
    public List<ItemRanking> getRankingServicios() {
        return rankingServicios;
    }

}
