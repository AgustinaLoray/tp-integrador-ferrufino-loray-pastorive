package ar.edu.unq.po2.unqtravel.Catalogo.atributos;

public class TipoInteger implements TipoAtributo {
    @Override
    public boolean esValido(Object valor) {
        return valor instanceof Integer;
    }
}