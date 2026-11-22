package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import modelo.DetalleRepuesto;
import modelo.EstadoOrden;
import modelo.Mecanico;
import modelo.OrdenTrabajo;
import modelo.StockInsuficienteException;
import modelo.Vehiculo;

public class OrdenTrabajoDao {
    private static final String CONSULTA_ORDENES = """
            SELECT o.numero, o.fecha_ingreso, o.descripcion_problema, o.horas_mano_obra, o.estado,
                   v.patente, v.marca, v.modelo, v.anio, v.tipo, v.cantidad_puertas, v.combustible, v.cilindrada,
                   c.dni, c.nombre, c.apellido, c.telefono, c.email,
                   m.legajo, m.nombre AS mecanico_nombre, m.apellido AS mecanico_apellido,
                   m.especialidad, m.valor_hora,
                   r.codigo AS repuesto_codigo, r.descripcion AS repuesto_descripcion, r.precio_unitario, r.stock,
                   d.cantidad, d.precio_unitario_aplicado
            FROM public.orden_trabajo o
            JOIN public.vehiculo v ON v.patente = o.vehiculo_patente
            JOIN public.cliente c ON c.dni = v.cliente_dni
            JOIN public.mecanico m ON m.legajo = o.mecanico_legajo
            LEFT JOIN public.detalle_repuesto d ON d.orden_numero = o.numero
            LEFT JOIN public.repuesto r ON r.codigo = d.repuesto_codigo
            """;

    /** Guarda la orden nueva y devuelve el número asignado por PostgreSQL. */
    public long crear(OrdenTrabajo orden) throws SQLException, StockInsuficienteException {
        if (orden.getNumero() != 0 || orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalArgumentException("El alta requiere una orden nueva en estado pendiente.");
        }
        List<DetalleRepuesto> detalles = new ArrayList<>(orden.getDetalles());
        // Bloquear los repuestos en el mismo orden reduce conflictos entre altas simultáneas.
        detalles.sort(Comparator.comparing(detalle -> detalle.getRepuesto().getCodigo()));

        try (Connection conexion = Conexion.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                long numero = insertarOrden(conexion, orden);
                guardarDetalles(conexion, numero, detalles);
                conexion.commit();
                return numero;
            } catch (SQLException | StockInsuficienteException | RuntimeException ex) {
                try {
                    conexion.rollback();
                } catch (SQLException errorRollback) {
                    ex.addSuppressed(errorRollback);
                }
                throw ex;
            }
        }
    }

    public List<OrdenTrabajo> listar() throws SQLException {
        return listar(null);
    }

    public List<OrdenTrabajo> listar(EstadoOrden estado) throws SQLException {
        String sql = CONSULTA_ORDENES + (estado == null ? "" : "WHERE o.estado = ? ")
                + "ORDER BY o.numero DESC, d.repuesto_codigo";
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (estado != null) {
                sentencia.setString(1, estado.name());
            }
            try (ResultSet filas = sentencia.executeQuery()) {
                return leerOrdenes(filas);
            }
        }
    }

    public boolean cambiarEstado(long numero, EstadoOrden actual, EstadoOrden nuevo) throws SQLException {
        Objects.requireNonNull(actual, "El estado actual es obligatorio.");
        if (!actual.puedePasarA(nuevo)) {
            throw new IllegalArgumentException("El cambio de estado no está permitido.");
        }
        String sql = "UPDATE public.orden_trabajo SET estado = ? WHERE numero = ? AND estado = ?";
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, nuevo.name());
            sentencia.setLong(2, numero);
            sentencia.setString(3, actual.name());
            return sentencia.executeUpdate() == 1;
        }
    }

    private long insertarOrden(Connection conexion, OrdenTrabajo orden) throws SQLException {
        String sql = """
                INSERT INTO public.orden_trabajo
                    (vehiculo_patente, mecanico_legajo, fecha_ingreso, descripcion_problema, horas_mano_obra)
                VALUES (?, ?, ?, ?, ?)
                RETURNING numero
                """;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, orden.getVehiculo().getPatente());
            sentencia.setInt(2, orden.getMecanico().getLegajo());
            sentencia.setObject(3, orden.getFechaIngreso());
            sentencia.setString(4, orden.getDescripcionProblema());
            sentencia.setBigDecimal(5, orden.getHorasManoDeObra());
            try (ResultSet fila = sentencia.executeQuery()) {
                if (!fila.next()) {
                    throw new SQLException("PostgreSQL no devolvió el número de la orden.");
                }
                return fila.getLong("numero");
            }
        }
    }

    private void guardarDetalles(Connection conexion, long numero, List<DetalleRepuesto> detalles)
            throws SQLException, StockInsuficienteException {
        String sqlStock = "UPDATE public.repuesto SET stock = stock - ? WHERE codigo = ? AND stock >= ?";
        String sqlDetalle = """
                INSERT INTO public.detalle_repuesto
                    (orden_numero, repuesto_codigo, cantidad, precio_unitario_aplicado)
                VALUES (?, ?, ?, ?)
                """;
        try (PreparedStatement actualizarStock = conexion.prepareStatement(sqlStock);
                PreparedStatement insertarDetalle = conexion.prepareStatement(sqlDetalle)) {
            for (DetalleRepuesto detalle : detalles) {
                String codigo = detalle.getRepuesto().getCodigo();
                int cantidad = detalle.getCantidad();
                actualizarStock.setInt(1, cantidad);
                actualizarStock.setString(2, codigo);
                actualizarStock.setInt(3, cantidad);
                if (actualizarStock.executeUpdate() != 1) {
                    throw new StockInsuficienteException(
                            "Stock insuficiente para " + codigo + ". Cantidad solicitada: " + cantidad + ".");
                }
                insertarDetalle.setLong(1, numero);
                insertarDetalle.setString(2, codigo);
                insertarDetalle.setInt(3, cantidad);
                insertarDetalle.setBigDecimal(4, detalle.getPrecioUnitarioAplicado());
                insertarDetalle.executeUpdate();
            }
        }
    }

    private List<OrdenTrabajo> leerOrdenes(ResultSet filas) throws SQLException {
        List<OrdenTrabajo> ordenes = new ArrayList<>();
        boolean hayFila = filas.next();
        while (hayFila) {
            long numero = filas.getLong("numero");
            LocalDate fecha = filas.getObject("fecha_ingreso", LocalDate.class);
            String problema = filas.getString("descripcion_problema");
            BigDecimal horas = filas.getBigDecimal("horas_mano_obra");
            EstadoOrden estado = EstadoOrden.valueOf(filas.getString("estado"));
            Vehiculo vehiculo = VehiculoDao.leerVehiculo(filas);
            Mecanico mecanico = MecanicoDao.leerMecanico(filas);
            List<DetalleRepuesto> detalles = new ArrayList<>();

            // El JOIN devuelve una fila por repuesto. Todas las filas de una orden son consecutivas.
            do {
                if (filas.getString("repuesto_codigo") != null) {
                    detalles.add(new DetalleRepuesto(RepuestoDao.leerRepuesto(filas),
                            filas.getInt("cantidad"), filas.getBigDecimal("precio_unitario_aplicado")));
                }
                hayFila = filas.next();
            } while (hayFila && filas.getLong("numero") == numero);

            ordenes.add(new OrdenTrabajo(numero, vehiculo, mecanico, fecha, problema, horas, estado, detalles));
        }
        return ordenes;
    }
}
