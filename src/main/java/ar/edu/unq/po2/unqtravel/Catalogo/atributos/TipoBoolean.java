package ar.edu.unq.po2.unqtravel.catalogo.atributos;

public class TipoBoolean implements TipoAtributo {
    @Override
    public boolean esValido(Object valor) {
        return valor instanceof Boolean;
    }
}