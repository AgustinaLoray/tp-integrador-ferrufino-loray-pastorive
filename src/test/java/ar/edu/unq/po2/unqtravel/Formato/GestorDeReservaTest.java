package ar.edu.unq.po2.unqtravel.Formato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Traslado.PuntoEncuentro;

class GestorDeReservaTest {

    @Test
    void filtraReservasPorSolapamientoIncluyendoLosLimitesDelPeriodo() {
        GestorDeReserva gestor = new GestorDeReserva();
        Reserva antes = reserva("2026-11-01", "2026-11-09");
        Reserva solapaDesdeAntes = reserva("2026-11-09", "2026-11-11");
        Reserva dentro = reserva("2026-11-10", "2026-11-12");
        Reserva solapaHastaDespues = reserva("2026-11-12", "2026-11-15");
        Reserva despues = reserva("2026-11-13", "2026-11-15");
        gestor.agregarReserva(antes);
        gestor.agregarReserva(solapaDesdeAntes);
        gestor.agregarReserva(dentro);
        gestor.agregarReserva(solapaHastaDespues);
        gestor.agregarReserva(despues);

        List<Reserva> resultado = gestor.obtenerReservasEnPeriodo(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 12));

        assertEquals(List.of(solapaDesdeAntes, dentro, solapaHastaDespues), resultado);
    }

    @Test
    void filtraReservasPorEstadoYDevuelveVacioCuandoNoHayCoincidencias() {
        GestorDeReserva gestor = new GestorDeReserva();
        Reserva pendiente = reserva("2026-11-10", "2026-11-12");
        Reserva cancelada = reserva("2026-11-10", "2026-11-12");
        cancelada.cancelar();
        gestor.agregarReserva(pendiente);
        gestor.agregarReserva(cancelada);

        assertEquals(List.of(cancelada), gestor.obtenerReservasPorEstado(cancelada.getEstado()));
        assertTrue(gestor.obtenerReservasPorEstado(new Object()).isEmpty());
    }

    private Reserva reserva(String inicio, String fin) {
        return new Reserva(
                LocalDate.parse(inicio),
                LocalDate.parse(fin),
                1,
                new PuntoEncuentro(0));
    }
}
