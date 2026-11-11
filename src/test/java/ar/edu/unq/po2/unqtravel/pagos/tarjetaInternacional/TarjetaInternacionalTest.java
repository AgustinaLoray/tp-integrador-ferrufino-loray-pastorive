package ar.edu.unq.po2.unqtravel.pagos.tarjetaInternacional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class TarjetaInternacionalTest {

    @Test
    void procesaElPagoEnOrdenYRegistraElVoucher() {
        ApiTarjetaInternacional api = mock(ApiTarjetaInternacional.class);
        Date vencimiento = new Date();
        Object voucher = new Object();
        when(api.generarVoucher()).thenReturn(voucher);
        TarjetaInternacional tarjeta =
                new TarjetaInternacional(api, 123456, 321, vencimiento);

        tarjeta.procesarPago(150.25);

        InOrder orden = inOrder(api);
        orden.verify(api).validar(123456, 321, vencimiento);
        orden.verify(api).solicitarPreAutorizacion(150.25);
        orden.verify(api).ejecutarTransaccion(150.25);
        orden.verify(api).generarVoucher();
        orden.verify(api).registrarVoucher(voucher);
        verifyNoMoreInteractions(api);
    }

    @Test
    void siFallaLaValidacionNoReservaNiEjecutaNiNotifica() {
        ApiTarjetaInternacional api = mock(ApiTarjetaInternacional.class);
        Date vencimiento = new Date();
        doThrow(new IllegalArgumentException("datos inválidos"))
                .when(api).validar(123456, 321, vencimiento);
        TarjetaInternacional tarjeta =
                new TarjetaInternacional(api, 123456, 321, vencimiento);

        assertThrows(IllegalArgumentException.class, () -> tarjeta.procesarPago(150));

        verify(api).validar(123456, 321, vencimiento);
        verifyNoMoreInteractions(api);
    }

    @Test
    void siFallaLaReservaNoEjecutaNiNotifica() {
        ApiTarjetaInternacional api = mock(ApiTarjetaInternacional.class);
        Date vencimiento = new Date();
        doThrow(new IllegalStateException("fondos no disponibles"))
                .when(api).solicitarPreAutorizacion(150);
        TarjetaInternacional tarjeta =
                new TarjetaInternacional(api, 123456, 321, vencimiento);

        assertThrows(IllegalStateException.class, () -> tarjeta.procesarPago(150));

        InOrder orden = inOrder(api);
        orden.verify(api).validar(123456, 321, vencimiento);
        orden.verify(api).solicitarPreAutorizacion(150);
        verifyNoMoreInteractions(api);
    }
}
