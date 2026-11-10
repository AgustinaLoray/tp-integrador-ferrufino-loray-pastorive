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
	    String resultado = "--- REPORTE DE OCUPACION Y CUPOS DISPONIBLES ---\n";
	    
	    for (ItemCupo item : c.getListaCupos()) {
	        resultado = resultado + "- " + item.getCategoria() 
	                    + " | Totales: " + item.getCuposTotales() 
	                    + " | Reservados: " + item.getCuposReservados() 
	                    + " | Libres: " + item.getCuposLibres() + "\n";
	    }
	    
	    return resultado;
	}

	@Override
	public String visitarEPP(ExposicionPorProveedor e) {
	    String resultado = "--- REPORTE DE EXPOSICION POR PROVEEDOR ---\n";
	    
	    for (ItemExposicion item : e.getExposiciones()) {
	        resultado = resultado + "- " + item.getOperador() 
	                    + " | Cantidad: " + item.getCantidadItems() 
	                    + " | Porcentaje: " + item.getPorcentaje() + "%\n";
	    }
	    
	    return resultado;
	}
	
}
