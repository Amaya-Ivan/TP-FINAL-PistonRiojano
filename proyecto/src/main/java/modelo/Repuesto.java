package modelo;

import java.math.BigDecimal;

public final class Repuesto {
    private final String codigo;
    private final String descripcion;
    private final BigDecimal precioUnitario;
    private final int stock;

    public Repuesto(String codigo, String descripcion, BigDecimal precioUnitario, int stock) {
        if (codigo == null || codigo.isBlank() || descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("El código y la descripción son obligatorios.");
        }
        if (precioUnitario == null || precioUnitario.signum() < 0) {
            throw new IllegalArgumentException("El precio unitario debe ser cero o mayor.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock debe ser cero o mayor.");
        }
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public int getStock() {
        return stock;
    }

    public void verificarStock(int cantidad) throws StockInsuficienteException {
        verificarStock((long) cantidad);
    }

    // La cantidad acumulada de un borrador puede superar el rango de un int.
    void verificarStock(long cantidad) throws StockInsuficienteException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (cantidad > stock) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para " + codigo + ". Disponible: " + stock
                            + ", solicitado: " + cantidad + ".");
        }
    }
}
