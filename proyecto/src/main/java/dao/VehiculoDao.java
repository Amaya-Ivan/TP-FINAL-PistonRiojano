package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import modelo.Auto;
import modelo.Cliente;
import modelo.Moto;
import modelo.TipoCombustible;
import modelo.Vehiculo;

public class VehiculoDao {
    private static final String CONSULTA_VEHICULOS = """
            SELECT v.patente, v.marca, v.modelo, v.anio, v.tipo, v.cantidad_puertas,
                   v.combustible, v.cilindrada, c.dni, c.nombre, c.apellido, c.telefono, c.email
            FROM public.vehiculo v
            JOIN public.cliente c ON c.dni = v.cliente_dni
            """;

    public boolean crear(Vehiculo vehiculo) throws SQLException {
        String sql = """
                INSERT INTO public.vehiculo
                    (patente, cliente_dni, marca, modelo, anio, tipo,
                     cantidad_puertas, combustible, cilindrada)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, vehiculo.getPatente());
            sentencia.setString(2, vehiculo.getCliente().getDni());
            sentencia.setString(3, vehiculo.getMarca());
            sentencia.setString(4, vehiculo.getModelo());
            sentencia.setInt(5, vehiculo.getAnio());
            if (vehiculo instanceof Auto auto) {
                sentencia.setString(6, "AUTO");
                sentencia.setInt(7, auto.getCantidadPuertas());
                sentencia.setString(8, auto.getCombustible().name());
                sentencia.setNull(9, Types.INTEGER);
            } else if (vehiculo instanceof Moto moto) {
                sentencia.setString(6, "MOTO");
                sentencia.setNull(7, Types.INTEGER);
                sentencia.setNull(8, Types.VARCHAR);
                sentencia.setInt(9, moto.getCilindrada());
            } else {
                throw new IllegalArgumentException("El vehículo debe ser un auto o una moto.");
            }
            return sentencia.executeUpdate() == 1;
        }
    }

    public List<Vehiculo> listar() throws SQLException {
        return consultar(null);
    }

    public List<Vehiculo> listarPorCliente(String dni) throws SQLException {
        return consultar(Objects.requireNonNull(dni, "El DNI del cliente es obligatorio."));
    }

    private List<Vehiculo> consultar(String dni) throws SQLException {
        String sql = CONSULTA_VEHICULOS + (dni == null ? "" : "WHERE v.cliente_dni = ? ")
                + "ORDER BY v.patente";
        List<Vehiculo> vehiculos = new ArrayList<>();
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (dni != null) {
                sentencia.setString(1, dni);
            }
            try (ResultSet filas = sentencia.executeQuery()) {
                while (filas.next()) {
                    vehiculos.add(leerVehiculo(filas));
                }
            }
        }
        return vehiculos;
    }

    static Vehiculo leerVehiculo(ResultSet fila) throws SQLException {
        Cliente cliente = ClienteDao.leerCliente(fila);
        String patente = fila.getString("patente");
        String marca = fila.getString("marca");
        String modelo = fila.getString("modelo");
        int anio = fila.getInt("anio");
        return switch (fila.getString("tipo")) {
            case "AUTO" -> new Auto(patente, marca, modelo, anio, cliente,
                    fila.getInt("cantidad_puertas"), TipoCombustible.valueOf(fila.getString("combustible")));
            case "MOTO" -> new Moto(patente, marca, modelo, anio, cliente, fila.getInt("cilindrada"));
            default -> throw new SQLException("Tipo de vehículo desconocido para la patente " + patente + ".");
        };
    }
}
