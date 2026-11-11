package ar.edu.unq.po2.unqtravel.pagos.billeteraVirtual;

public interface ApiBilleteraVirtual {
    public void validar(double monto);
    public void bloquearFondos(double monto);
    public void ejecutarTransaccion(double monto);
    public void notificarAlUsuario();
}
