package ar.edu.unq.po2.unqtravel.Formato;

public class ExposicionPorProveedor implements Reporte {
	@Override
	public String aceptar(Formato f) {
		return f.visitarEPP(this);
	}
}
