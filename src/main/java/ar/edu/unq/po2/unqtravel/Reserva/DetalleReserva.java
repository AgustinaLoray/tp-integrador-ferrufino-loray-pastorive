package ar.edu.unq.po2.unqtravel.Reserva;

import java.util.Objects;

import ar.edu.unq.po2.unqtravel.Catalogo.Item.Item;

public class DetalleReserva {
    private final Item item;
    private final double precioUnitarioCobrado;
    private final int cantidad;

    public DetalleReserva(Item item, double precioUnitarioCobrado, int cantidad) {
        this.item = Objects.requireNonNull(item, "El item no puede ser null");
        if (!Double.isFinite(precioUnitarioCobrado) || precioUnitarioCobrado < 0) {
            throw new IllegalArgumentException("El precio unitario debe ser un número no negativo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
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
