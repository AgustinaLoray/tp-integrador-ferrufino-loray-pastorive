package ar.edu.unq.po2.unqtravel.Formato;

public class CSV implements Formato{

	@Override
	public String visitarSMR(ServiciosMasReservados s) {
	    String resultado = "Servicio,Cantidad Vendida,Precio Promedio\n";
	    
	    for (ItemRanking item : s.getRankingServicios()) {
	        resultado = resultado + item.getNombreServicio() + "," 
	                    + item.getCantidadVendida() + "," 
	                    + item.getPrecioPromedio() + "\n";
	    }
	    
	    return resultado; 
	}
	

	@Override
	public String visitarCupoDisponible(CupoDisponible c) {
		String resultado = "Categoria,Cupos Totales,Cupos Reservados,Cupos Libres\n";
	    
	    for (ItemCupo item : c.getListaCupos()) {
	        resultado = resultado + item.getCategoria() + ","
	                    + item.getCuposTotales() + ","
	                    + item.getCuposReservados() + ","
	                    + item.getCuposLibres() + "\n";
	    }
	    
	    return resultado;
		
	}

	@Override
	public String visitarEPP(ExposicionPorProveedor e) {
	    String resultado = "Operador,Cantidad de Items,Porcentaje\n";
	    
	    for (ItemExposicion item : e.getExposiciones()) {
	        resultado = resultado + item.getOperador() + ","
	                    + item.getCantidadItems() + ","
	                    + item.getPorcentaje() + "%\n";
	    }
	    
	    return resultado;
	}

}
