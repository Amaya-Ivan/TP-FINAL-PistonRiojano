package modelo;

import java.math.BigDecimal;
import java.util.Objects;

public final class DetalleRepuesto {
    private final Repuesto repuesto;
    private final int cantidad;
    private final BigDecimal precioUnitarioAplicado;

    public DetalleRepuesto(Repuesto repuesto, int cantidad, BigDecimal precioUnitarioAplicado) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (precioUnitarioAplicado == null || precioUnitarioAplicado.signum() < 0) {
            throw new IllegalArgumentException("El precio aplicado debe ser cero o mayor.");
        }
        this.repuesto = Objects.requireNonNull(repuesto, "El repuesto es obligatorio.");
        this.cantidad = cantidad;
        this.precioUnitarioAplicado = precioUnitarioAplicado;
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitarioAplicado() {
        return precioUnitarioAplicado;
    }

    public BigDecimal calcularSubtotal() {
        return precioUnitarioAplicado.multiply(BigDecimal.valueOf(cantidad));
    }
}
