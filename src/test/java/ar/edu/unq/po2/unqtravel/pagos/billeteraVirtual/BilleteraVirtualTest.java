package ar.edu.unq.po2.unqtravel.pagos.billeteraVirtual;

import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class BilleteraVirtualTest {

    @Test
    void validaBloqueaEjecutaYNotificaEnOrden() {
        ApiBilleteraVirtual api = mock(ApiBilleteraVirtual.class);
        BilleteraVirtual billetera = new BilleteraVirtual(api);

        billetera.procesarPago(45.75);

        InOrder orden = inOrder(api);
        orden.verify(api).validar(45.75);
        orden.verify(api).bloquearFondos(45.75);
        orden.verify(api).ejecutarTransaccion(45.75);
        orden.verify(api).notificarAlUsuario();
        verifyNoMoreInteractions(api);
    }

    @Test
    void siFallaLaValidacionNoBloqueaNiEjecutaNiNotifica() {
        ApiBilleteraVirtual api = mock(ApiBilleteraVirtual.class);
        doThrow(new IllegalArgumentException("monto inválido")).when(api).validar(45.75);
        BilleteraVirtual billetera = new BilleteraVirtual(api);

        assertThrows(Excepciones.class, () -> billetera.procesarPago(45.75));

        verify(api).validar(45.75);
        verifyNoMoreInteractions(api);
    }

    @Test
    void siFallaElBloqueoNoEjecutaNiNotifica() {
        ApiBilleteraVirtual api = mock(ApiBilleteraVirtual.class);
        doThrow(new IllegalStateException("no se pudo bloquear"))
                .when(api).bloquearFondos(45.75);
        BilleteraVirtual billetera = new BilleteraVirtual(api);

        assertThrows(Excepciones.class, () -> billetera.procesarPago(45.75));

        InOrder orden = inOrder(api);
        orden.verify(api).validar(45.75);
        orden.verify(api).bloquearFondos(45.75);
        verifyNoMoreInteractions(api);
    }

    @Test
    void siFallaLaTransaccionNoNotifica() {
        ApiBilleteraVirtual api = mock(ApiBilleteraVirtual.class);
        doThrow(new IllegalStateException("pago rechazado"))
                .when(api).ejecutarTransaccion(45.75);
        BilleteraVirtual billetera = new BilleteraVirtual(api);

        assertThrows(Excepciones.class, () -> billetera.procesarPago(45.75));

        InOrder orden = inOrder(api);
        orden.verify(api).validar(45.75);
        orden.verify(api).bloquearFondos(45.75);
        orden.verify(api).ejecutarTransaccion(45.75);
        verifyNoMoreInteractions(api);
    }
}
