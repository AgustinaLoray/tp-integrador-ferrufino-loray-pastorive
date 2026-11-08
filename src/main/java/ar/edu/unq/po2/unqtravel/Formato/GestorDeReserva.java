package ar.edu.unq.po2.unqtravel.Formato;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class GestorDeReserva {
	private List<Reserva> reservas;
	
	public GestorDeReserva () {
		reservas = new ArrayList<>();
	}
	
	public void agregarReserva(Reserva r) {
        this.reservas.add(r);
    }

	
	
    public List<Reserva> obtenerReservasEnPeriodo(LocalDate inicio, LocalDate fin) {
        List<Reserva> reservasFiltradas = new ArrayList<>();
        
        for (Reserva r : reservas) {
            LocalDate fechaReserva = r.getFechaInicio();
            
            if (fechaReservasSeSolapan(fechaReserva, r.getFechaFin(), inicio, fin)) {
                reservasFiltradas.add(r);
            }
        }
        
        return reservasFiltradas;
    }


    private boolean fechaReservasSeSolapan(LocalDate inicioReserva, LocalDate finReserva, LocalDate inicioPeriodo, LocalDate finPeriodo) {
        return !inicioReserva.isAfter(finPeriodo) && !finReserva.isBefore(inicioPeriodo);
    }

    public List<Reserva> obtenerReservasPorEstado(Object estado) {
        List<Reserva> reservasPorEstado = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getEstado().equals(estado)) {
                reservasPorEstado.add(r);
            }
        }
        return reservasPorEstado;
    }

}
