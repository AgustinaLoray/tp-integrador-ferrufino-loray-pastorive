package ar.edu.unq.po2.unqtravel.pagos.transferenciaDirecta;

public interface ApiTransferenciaDirecta {
    public void validar(String alias);
    public Object generarComprobante();
    public void registrarComprobante(Object comprobante);
    public void ejecutarTransaccion();
}
