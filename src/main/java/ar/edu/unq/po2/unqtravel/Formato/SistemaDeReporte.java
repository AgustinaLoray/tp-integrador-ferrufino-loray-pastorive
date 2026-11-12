package ar.edu.unq.po2.unqtravel.Formato;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.edu.unq.po2.unqtravel.Catalogo.Catalogo.Catalogo;
import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Catalogo.PaqueteTuristico.PaqueteTuristico;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;
import ar.edu.unq.po2.unqtravel.Estados.Cancelada;
import ar.edu.unq.po2.unqtravel.Reserva.DetalleReserva;
import ar.edu.unq.po2.unqtravel.Reserva.Reserva;

public class SistemaDeReporte {
	private GestorDeReserva gestorDeReservas;
    private Catalogo catalogo; 

    
    public SistemaDeReporte(GestorDeReserva gestorDeReservas, Catalogo catalogo) {
        this.gestorDeReservas = gestorDeReservas;
        this.catalogo = catalogo;
    }
    
    //Seccion de reporte Servicios mas Reservados

    public String generarReporteServiciosMasReservados(LocalDate inicio, LocalDate fin, Formato formato) {
        List<Reserva> reservasDelPeriodo = gestorDeReservas.obtenerReservasEnPeriodo(inicio, fin);
        // Solo cuentan ventas efectivamente pagadas que no fueron canceladas.
        List<Reserva> reservasValidas = reservasDelPeriodo.stream()
                .filter(Reserva::tienePagoConfirmado)
                .filter(reserva -> !(reserva.getEstado() instanceof Cancelada))
                .toList();
        List<ItemRanking> listaDeFilas = new ArrayList<>();
        
        for (Item item : catalogo.getItems()) {
            listaDeFilas.add(armarFilaRanking(item, reservasValidas));
        }

        listaDeFilas.sort(Comparator.comparingInt(ItemRanking::getCantidadVendida)
                .reversed()
                .thenComparing(ItemRanking::getNombreServicio));

        ServiciosMasReservados serviciosMasReservados = new ServiciosMasReservados(listaDeFilas, inicio, fin);
        return serviciosMasReservados.aceptar(formato);
    }
    
    private ItemRanking armarFilaRanking(Item item, List<Reserva> reservas) {
        int unidadesVendidas = 0;
        double importeCobrado = 0.0;

        for (Reserva reserva : reservas) {
            for (DetalleReserva detalle : reserva.getDetallesReserva()) {
                if (detalle.getItem() == item) {
                    // El detalle representa el ítem comprado; un paquete se cuenta como paquete,
                    // sin volver a contar sus componentes.
                    unidadesVendidas += detalle.getCantidad();
                    importeCobrado += detalle.getImporteTotal();
                }
            }
        }

        // El promedio se pondera por unidades: importe cobrado total dividido por unidades vendidas.
        double precioPromedio = unidadesVendidas == 0 ? 0.0 : importeCobrado / unidadesVendidas;
        return new ItemRanking(item.getNombre(), unidadesVendidas, precioPromedio);
    }
    
    
    // Seccion de reporte cupos disponibles
    
    public String generarReporteCuposDisponibles(LocalDate inicio, LocalDate fin, Formato formato) {
        List<Reserva> reservasDelPeriodo = gestorDeReservas.obtenerReservasEnPeriodo(inicio, fin);
        List<ItemCupo> listaFilas = this.armarFilasCupos(reservasDelPeriodo);
        
        CupoDisponible cupoDisponible = new CupoDisponible(listaFilas, inicio, fin);
        return cupoDisponible.aceptar(formato);
    }
    
    private List<ItemCupo> armarFilasCupos(List<Reserva> reservasDelPeriodo) {
        Map<String, Integer> totalesPorCategoria = new LinkedHashMap<>();
        Map<String, Integer> reservadosPorCategoria = new LinkedHashMap<>();
        Set<Item> inventarioContado = Collections.newSetFromMap(new IdentityHashMap<>());

        for (Item item : catalogo.getItems()) {
            acumularTotales(item, totalesPorCategoria, inventarioContado, crearConjuntoPorIdentidad());
        }

        for (Reserva reserva : reservasDelPeriodo) {
            if (!reserva.tienePagoConfirmado()
                    || reserva.getEstado() instanceof Cancelada) {
                continue;
            }

            for (DetalleReserva detalle : reserva.getDetallesReserva()) {
                acumularReservados(
                        detalle.getItem(),
                        detalle.getCantidad(),
                        reservadosPorCategoria,
                        crearConjuntoPorIdentidad());
            }
        }

        return totalesPorCategoria.entrySet().stream()
                .map(entrada -> new ItemCupo(
                        entrada.getKey(),
                        entrada.getValue(),
                        reservadosPorCategoria.getOrDefault(entrada.getKey(), 0)))
                .toList();
    }

    private void acumularTotales(
            Item item,
            Map<String, Integer> totalesPorCategoria,
            Set<Item> inventarioContado,
            Set<Item> paquetesEnRecorrido) {
        if (item instanceof PaqueteTuristico paquete) {
            if (!paquetesEnRecorrido.add(paquete)) {
                throw new Excepciones("La composición de paquetes contiene un ciclo.");
            }
            try {
                for (Item componente : paquete.getItems()) {
                    acumularTotales(componente, totalesPorCategoria, inventarioContado, paquetesEnRecorrido);
                }
            } finally {
                paquetesEnRecorrido.remove(paquete);
            }
            return;
        }

        if (inventarioContado.add(item)) {
            totalesPorCategoria.merge(item.getCategoria(), item.getCuposTotales(), Integer::sum);
        }
    }

    private void acumularReservados(
            Item item,
            int cantidad,
            Map<String, Integer> reservadosPorCategoria,
            Set<Item> paquetesEnRecorrido) {
        if (item instanceof PaqueteTuristico paquete) {
            if (!paquetesEnRecorrido.add(paquete)) {
                throw new Excepciones("La composición de paquetes contiene un ciclo.");
            }
            try {
                for (Item componente : paquete.getItems()) {
                    acumularReservados(componente, cantidad, reservadosPorCategoria, paquetesEnRecorrido);
                }
            } finally {
                paquetesEnRecorrido.remove(paquete);
            }
            return;
        }

        reservadosPorCategoria.merge(
                item.getCategoria(),
                cantidad,
                Integer::sum);
    }

    private Set<Item> crearConjuntoPorIdentidad() {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }
    
    // Seccion de exposicion por proveedor

    private static final String PROVEEDOR_SIN_ASIGNAR = "Sin proveedor asignado";
    
    public String generarReporteExposicionProveedor(Formato formato) {
        List<Item> items = catalogo.getItems();
        List<ItemExposicion> listaFilas = this.armarFilasExposicion(items);
        ExposicionPorProveedor reporte = new ExposicionPorProveedor(listaFilas);
        return reporte.aceptar(formato);
    }
    
    private List<ItemExposicion> armarFilasExposicion(List<Item> items) {
        Map<String, Double> pesosPorOperador = new LinkedHashMap<>();

        for (Item item : items) {
            Set<String> operadores = new LinkedHashSet<>();
            recolectarOperadores(item, operadores, crearConjuntoPorIdentidad());
            if (operadores.isEmpty()) {
                operadores.add(PROVEEDOR_SIN_ASIGNAR);
            }

            // Cada entrada del catálogo pesa una unidad; los paquetes la distribuyen
            // equitativamente entre sus proveedores distintos.
            double pesoPorOperador = 1.0 / operadores.size();
            for (String operador : operadores) {
                pesosPorOperador.merge(operador, pesoPorOperador, Double::sum);
            }
        }

        double totalItems = items.size();
        return pesosPorOperador.entrySet().stream()
                .map(entrada -> new ItemExposicion(
                        entrada.getKey(),
                        entrada.getValue(),
                        (entrada.getValue() / totalItems) * 100.0))
                .toList();
    }

    private void recolectarOperadores(Item item, Set<String> operadores, Set<Item> paquetesEnRecorrido) {
        if (item instanceof PaqueteTuristico paquete) {
            if (!paquetesEnRecorrido.add(paquete)) {
                throw new Excepciones("La composición de paquetes contiene un ciclo.");
            }
            try {
                for (Item componente : paquete.getItems()) {
                    recolectarOperadores(componente, operadores, paquetesEnRecorrido);
                }
            } finally {
                paquetesEnRecorrido.remove(paquete);
            }
            return;
        }

        String proveedor = item.getOperador();
        if (proveedor != null && !proveedor.isBlank()) {
            operadores.add(proveedor);
        }
    }
}
