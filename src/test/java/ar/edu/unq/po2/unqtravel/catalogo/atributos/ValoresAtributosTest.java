package ar.edu.unq.po2.unqtravel.catalogo.atributos;

import ar.edu.unq.po2.unqtravel.Catalogo.atributos.AtributoDinamico;
import ar.edu.unq.po2.unqtravel.Catalogo.atributos.TipoBoolean;
import ar.edu.unq.po2.unqtravel.Catalogo.atributos.TipoDate;
import ar.edu.unq.po2.unqtravel.Catalogo.atributos.TipoInteger;
import ar.edu.unq.po2.unqtravel.Catalogo.atributos.TipoString;
import ar.edu.unq.po2.unqtravel.Catalogo.atributos.ValorAtributo;
import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValoresAtributosTest {
    @Test
    void cadaTipoValidaSoloValoresDeSuTipo() {
        assertTrue(new TipoString().esValido("texto"));
        assertFalse(new TipoString().esValido(1));
        assertTrue(new TipoInteger().esValido(1));
        assertFalse(new TipoInteger().esValido(1.0));
        assertTrue(new TipoBoolean().esValido(true));
        assertFalse(new TipoBoolean().esValido("true"));
        assertTrue(new TipoDate().esValido(new Date()));
        assertFalse(new TipoDate().esValido("2026-10-31"));
    }

    @Test
    void atributoRequeridoExigeUnValorDelTipoCorrecto() {
        AtributoDinamico atributo = new AtributoDinamico("nombre", true, new TipoString());

        assertTrue(atributo.esValido("Viaje"));
        assertFalse(atributo.esValido(null));
        assertFalse(atributo.esValido(42));
    }

    @Test
    void atributoOpcionalAceptaNullPeroValidaElTipoCuandoHayValor() {
        AtributoDinamico atributo = new AtributoDinamico("comentario", false, new TipoString());

        assertTrue(atributo.esValido(null));
        assertTrue(atributo.esValido("sin equipaje"));
        assertFalse(atributo.esValido(42));
    }

    @Test
    void valorAtributoDelegaLaValidacionYPermiteCambiarElValor() {
        AtributoDinamico atributo = new AtributoDinamico("nombre", true, new TipoString());
        ValorAtributo valor = new ValorAtributo("UNQ", atributo);

        assertTrue(valor.esValido());
        valor.setValor(10);
        assertFalse(valor.esValido());
    }
}
