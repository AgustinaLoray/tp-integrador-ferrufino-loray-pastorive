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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String visitarEPP(ExposicionPorProveedor e) {
		// TODO Auto-generated method stub
		
	}
	
}
