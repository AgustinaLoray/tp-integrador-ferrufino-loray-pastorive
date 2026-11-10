package ar.edu.unq.po2.unqtravel.Formato;

public class HTML implements Formato {

	@Override
	public String visitarSMR(ServiciosMasReservados s) {
	    String resultado = "<h1>Servicios Más Reservados</h1>" +
	                       "<ul>";
	    
	    for (ItemRanking item : s.getRankingServicios()) {
	        resultado = resultado + "<li>" 
	                    + item.getNombreServicio() + " - " 
	                    + item.getCantidadVendida() + " unidades - $" 
	                    + item.getPrecioPromedio() 
	                    + "</li>";
	    }
	    
	    resultado = resultado + "</ul>";
	    
	    return resultado;
		
	}

	@Override
	public String visitarCupoDisponible(CupoDisponible c) {
	    String resultado = "<h1>Reporte de Ocupación y Cupos</h1>" +
	                       "<ul>";
	    
	    for (ItemCupo item : c.getListaCupos()) {
	        resultado = resultado + "<li>" 
	                    + item.getCategoria() + " | " 
	                    + "Totales: " + item.getCuposTotales() + " | "
	                    + "Reservados: " + item.getCuposReservados() + " | "
	                    + "Libres: " + item.getCuposLibres() 
	                    + "</li>";
	    }
	    
	    resultado = resultado + "</ul>";
	    return resultado;
	}
	
		
	

	@Override
	public String visitarEPP(ExposicionPorProveedor e)  {
	    String resultado = "<h1>Reporte de Exposición por Proveedor</h1>" +
	                       "<ul>";
	    
	    for (ItemExposicion item : e.getExposiciones()) {
	        resultado = resultado + "<li>" 
	                    + item.getOperador() + " | " 
	                    + "Cantidad: " + item.getCantidadItems() + " | "
	                    + "Porcentaje: " + item.getPorcentaje() + "%"
	                    + "</li>";
	    }
	    
	    resultado = resultado + "</ul>";
	    return resultado;
	}
}
