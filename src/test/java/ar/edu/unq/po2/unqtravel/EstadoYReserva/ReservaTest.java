package ar.edu.unq.po2.unqtravel.EstadoYReserva;

import ar.edu.unq.po2.unqtravel.Reserva.Reserva;
import ar.edu.unq.po2.unqtravel.Catalogo.ServicioIndividual.ServicioIndividual;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservaTest {
    @Test
    void confirmaLaReservaYLiberaCuposAlCancelar() {
        ServicioIndividual servicio = crearServicio(1);
        Reserva reserva = new Reserva();
        reserva.agregarItem(servicio);

        reserva.confirmar();
        assertEquals(0, servicio.getCuposDisponibles());

        reserva.cancelar();
        assertEquals(1, servicio.getCuposDisponibles());
    }

    @Test
    void fallaLaConfirmacionSinCuposYRevierteLosCuposYaBloqueados() {
        ServicioIndividual disponible = crearServicio(1);
        ServicioIndividual agotado = crearServicio(0);
        Reserva reserva = new Reserva();
        reserva.agregarItem(disponible);
        reserva.agregarItem(agotado);

        assertThrows(IllegalStateException.class, reserva::confirmar);
        assertEquals(1, disponible.getCuposDisponibles());
        assertEquals(0, agotado.getCuposDisponibles());
    }

    @Test
    void noPermiteConfirmarUnaReservaQueYaFueCancelada() {
        Reserva reserva = new Reserva();
        reserva.cancelar();

        assertThrows(IllegalStateException.class, reserva::confirmar);
    }

    private ServicioIndividual crearServicio(int cupos) {
        return new ServicioIndividual(
                1,
                "Excursion",
                cupos,
                "Operador",
                "Turismo",
                "Excursion guiada",
                100.0,
                0.0
        );
    }
}
