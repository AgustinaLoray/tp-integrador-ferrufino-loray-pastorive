package ar.edu.unq.po2.unqtravel.Catalogo.atributos;

public class AtributoDinamico {
    private String nombre;
    private boolean obligatorio;
    private TipoAtributo tipo;

    public AtributoDinamico(String nombre, boolean obligatorio, TipoAtributo tipo) {
        this.nombre = nombre;
        this.obligatorio = obligatorio;
        this.tipo = tipo;
    }

    public boolean esValido(Object valor) {
        return valor == null
                ? !this.obligatorio
                : this.tipo != null && this.tipo.esValido(valor);
    }

    public boolean esObligatorio() {
        return this.obligatorio;
    }

    public String getNombre() {
        return this.nombre;
    }

    public TipoAtributo getTipo() {
        return this.tipo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public void setTipo(TipoAtributo tipo) {
        this.tipo = tipo;
    }
}