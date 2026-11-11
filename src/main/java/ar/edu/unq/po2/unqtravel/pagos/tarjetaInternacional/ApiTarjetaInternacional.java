package ar.edu.unq.po2.unqtravel.pagos.tarjetaInternacional;

public interface ApiTarjetaInternacional {
    public void validar(int numero, int cvv, java.util.Date fechaVencimiento);
    public Object generarVoucher();
    public void registrarVoucher(Object voucher);
    public void solicitarPreAutorizacion(double monto);
    public void ejecutarTransaccion(double monto);
}
