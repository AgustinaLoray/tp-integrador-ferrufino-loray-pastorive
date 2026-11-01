package ar.edu.unq.po2.unqtravel.Catalogo.atributos;

public class ValorAtributo {
    private Object valor;
    private AtributoDinamico atributo;

    public ValorAtributo(Object valor, AtributoDinamico atributo) {
        this.valor = valor;
        this.atributo = atributo;
    }

    public boolean esValido() {
        return this.atributo != null && this.atributo.esValido(this.valor);
    }

    public Object getValor() {
        return this.valor;
    }

    public AtributoDinamico getAtributo() {
        return this.atributo;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
}