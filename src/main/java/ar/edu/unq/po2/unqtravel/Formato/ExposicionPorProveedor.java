package ar.edu.unq.po2.unqtravel.Formato;

import java.util.List;

public class ExposicionPorProveedor implements Reporte {
	private List<ItemExposicion> exposiciones;

    public ExposicionPorProveedor(List<ItemExposicion> exposiciones) {
        this.exposiciones = exposiciones;
    }

    public List<ItemExposicion> getExposiciones() {
        return exposiciones;
    }
	@Override
	public String aceptar(Formato f) {
		return f.visitarEPP(this); 
	}
}
