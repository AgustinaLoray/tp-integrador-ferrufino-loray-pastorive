package ar.edu.unq.po2.unqtravel.Reserva;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;
import ar.edu.unq.po2.unqtravel.Excepciones.Excepciones;

public class DetalleReserva {
    private final Item item;
    private final double precioUnitarioCobrado;
    private final int cantidad;

    public DetalleReserva(Item item, double precioUnitarioCobrado, int cantidad) {
        if (item == null) {
            throw new Excepciones("El item no puede ser null");
        }
        if (!Double.isFinite(precioUnitarioCobrado) || precioUnitarioCobrado < 0) {
            throw new Excepciones("El precio unitario debe ser un número no negativo");
        }
        if (cantidad <= 0) {
            throw new Excepciones("La cantidad debe ser positiva");
        }
        this.item = item;
        this.precioUnitarioCobrado = precioUnitarioCobrado;
        this.cantidad = cantidad;
    }

    public Item getItem() {
        return item;
    }

    public double getPrecioUnitarioCobrado() {
        return precioUnitarioCobrado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getImporteTotal() {
        return precioUnitarioCobrado * cantidad;
    }
}
