package ar.edu.unq.po2.unqtravel.catalogo.atributos;

public class TipoDate implements TipoAtributo {
    @Override
    public boolean esValido(Object valor) {
        return valor instanceof java.util.Date;
    }
}