package ar.edu.unq.po2.unqtravel.Formato;

public class TextoPlano implements Formato {

	@Override
	public String visitarSMR(ServiciosMasReservados s) {
		String resultado = "--- REPORTE DE SERVICIOS MAS RESERVADOS ---\n";
	    
	    for (ItemRanking item : s.getRankingServicios()) {
	        resultado = resultado + "- " + item.getNombreServicio() 
	                    + " | Cantidad: " + item.getCantidadVendida() 
	                    + " | Promedio: $" + item.getPrecioPromedio() + "\n";
	    }
	    
	    return resultado;
		
	}

	@Override
	public String visitarCupoDisponible(CupoDisponible c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String visitarEPP(ExposicionPorProveedor e) {
		// TODO Auto-generated method stub
		
	}
	
}
