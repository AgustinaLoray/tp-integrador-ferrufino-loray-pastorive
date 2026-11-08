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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String visitarEPP(ExposicionPorProveedor e) {
		// TODO Auto-generated method stub
		
	}

}
