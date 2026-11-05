package ar.edu.unq.po2.unqtravel.Formato;

public class CupoDisponible implements Reporte{
	@Override
	public String aceptar(Formato f) {
		return f.visitarCupoDisponible(this);
	}
}
