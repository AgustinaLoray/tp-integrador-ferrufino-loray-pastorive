package ar.edu.unq.po2.unqtravel.pagos.transferenciaDirecta;

import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class TransferenciaDirectaTest {

    @Test
    void validaEjecutaYRegistraElComprobanteSinReservarFondos() {
        ApiTransferenciaDirecta api = mock(ApiTransferenciaDirecta.class);
        Object comprobante = new Object();
        when(api.generarComprobante()).thenReturn(comprobante);
        TransferenciaDirecta transferencia = new TransferenciaDirecta(api, "viaje.unq");

        transferencia.procesarPago(80);

        InOrder orden = inOrder(api);
        orden.verify(api).validar("viaje.unq");
        orden.verify(api).ejecutarTransaccion();
        orden.verify(api).generarComprobante();
        orden.verify(api).registrarComprobante(comprobante);
        verifyNoMoreInteractions(api);
    }

    @Test
    void siFallaLaValidacionNoEjecutaNiGeneraComprobante() {
        ApiTransferenciaDirecta api = mock(ApiTransferenciaDirecta.class);
        doThrow(new IllegalArgumentException("alias inválido"))
                .when(api).validar("viaje.unq");
        TransferenciaDirecta transferencia = new TransferenciaDirecta(api, "viaje.unq");

        assertThrows(Excepciones.class, () -> transferencia.procesarPago(80));

        verify(api).validar("viaje.unq");
        verifyNoMoreInteractions(api);
    }

    @Test
    void siFallaLaTransferenciaNoGeneraNiRegistraComprobante() {
        ApiTransferenciaDirecta api = mock(ApiTransferenciaDirecta.class);
        doThrow(new IllegalStateException("transferencia rechazada"))
                .when(api).ejecutarTransaccion();
        TransferenciaDirecta transferencia = new TransferenciaDirecta(api, "viaje.unq");

        assertThrows(Excepciones.class, () -> transferencia.procesarPago(80));

        InOrder orden = inOrder(api);
        orden.verify(api).validar("viaje.unq");
        orden.verify(api).ejecutarTransaccion();
        verifyNoMoreInteractions(api);
    }
}
