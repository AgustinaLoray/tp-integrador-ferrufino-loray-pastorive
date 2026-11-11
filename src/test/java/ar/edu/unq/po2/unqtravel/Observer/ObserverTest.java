package ar.edu.unq.po2.unqtravel.Observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class ObserverTest {

    @Test
    void observadoresDeClubYFacturaEjecutanSusAcciones() {
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(salida));
            new ObserverClub().notificar();
            new ObserverFactura().notificar();
        } finally {
            System.setOut(salidaOriginal);
        }

        assertEquals(
                "Cupon de descuento del 5% enviado al cliente por cancelacion"
                        + System.lineSeparator()
                        + "Comprobante fiscal y voucher generados con exito para la reserva"
                        + System.lineSeparator(),
                salida.toString());
    }

    @Test
    void observadorDeEmailEnviaLaNotificacionConLosDatosConfigurados() {
        MailSender mailSender = mock(MailSender.class);

        new ObserverEmail(mailSender, "cliente@example.com").notificar();

        verify(mailSender).enviarMail(
                "cliente@example.com",
                "Actualizacion de reserva",
                "Su reserva cambio de estado",
                null);
    }
}
