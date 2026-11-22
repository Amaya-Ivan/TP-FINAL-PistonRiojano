package modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class OrdenTrabajo {
    private final long numero;
    private final LocalDate fechaIngreso;
    private final String descripcionProblema;
    private final BigDecimal horasManoDeObra;
    private final Vehiculo vehiculo;
    private final Mecanico mecanico;
    private final Map<String, DetalleRepuesto> detalles = new LinkedHashMap<>();
    private EstadoOrden estado;

    /** Crea una orden pendiente. PostgreSQL asignará el número al guardarla. */
    public OrdenTrabajo(Vehiculo vehiculo, Mecanico mecanico, LocalDate fechaIngreso,
                        String descripcionProblema, BigDecimal horasManoDeObra) {
        this(0, vehiculo, mecanico, fechaIngreso, descripcionProblema, horasManoDeObra,
                EstadoOrden.PENDIENTE, List.of());
    }

    /** Reconstruye una orden con su detalle y los precios que ya quedaron registrados. */
    public OrdenTrabajo(long numero, Vehiculo vehiculo, Mecanico mecanico, LocalDate fechaIngreso,
                        String descripcionProblema, BigDecimal horasManoDeObra,
                        EstadoOrden estado, List<DetalleRepuesto> detalles) {
        if (numero < 0) {
            throw new IllegalArgumentException("El número de orden no puede ser negativo.");
        }
        if (descripcionProblema == null || descripcionProblema.isBlank()) {
            throw new IllegalArgumentException("La descripción del problema es obligatoria.");
        }
        if (horasManoDeObra == null || horasManoDeObra.signum() < 0) {
            throw new IllegalArgumentException("Las horas de mano de obra deben ser cero o más.");
        }
        this.numero = numero;
        this.vehiculo = Objects.requireNonNull(vehiculo, "El vehículo es obligatorio.");
        this.mecanico = Objects.requireNonNull(mecanico, "El mecánico es obligatorio.");
        this.fechaIngreso = Objects.requireNonNull(fechaIngreso, "La fecha de ingreso es obligatoria.");
        this.descripcionProblema = descripcionProblema;
        this.horasManoDeObra = horasManoDeObra;
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio.");
        for (DetalleRepuesto detalle : detalles) {
            String codigo = detalle.getRepuesto().getCodigo();
            if (this.detalles.putIfAbsent(codigo, detalle) != null) {
                throw new IllegalArgumentException("La orden contiene el repuesto duplicado " + codigo + ".");
            }
        }
    }

    public long getNumero() {
        return numero;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public BigDecimal getHorasManoDeObra() {
        return horasManoDeObra;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Mecanico getMecanico() {
        return mecanico;
    }

    public List<DetalleRepuesto> getDetalles() {
        return List.copyOf(detalles.values());
    }

    /** Valida la cantidad del borrador. El descuento de stock se hará al guardar la orden. */
    public void agregarRepuesto(Repuesto item, int cantidad) throws StockInsuficienteException {
        Objects.requireNonNull(item, "El repuesto es obligatorio.");
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        String codigo = item.getCodigo();
        DetalleRepuesto previo = detalles.get(codigo);
        long cantidadTotal = cantidad;
        BigDecimal precioAplicado = item.getPrecioUnitario();
        if (previo != null) {
            cantidadTotal += previo.getCantidad();
            precioAplicado = previo.getPrecioUnitarioAplicado();
        }
        item.verificarStock(cantidadTotal);
        detalles.put(codigo, new DetalleRepuesto(item, (int) cantidadTotal, precioAplicado));
    }

    public BigDecimal calcularManoDeObra() {
        return horasManoDeObra.multiply(mecanico.getValorHora())
                .multiply(vehiculo.coeficienteManoDeObra())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularTotal() {
        BigDecimal total = calcularManoDeObra();
        for (DetalleRepuesto detalle : detalles.values()) {
            total = total.add(detalle.calcularSubtotal());
        }
        return total;
    }

    public void cambiarEstado(EstadoOrden nuevoEstado) {
        if (!estado.puedePasarA(nuevoEstado)) {
            throw new IllegalStateException("Cambio de estado inválido: " + estado + " → " + nuevoEstado);
        }
        estado = nuevoEstado;
    }
}
