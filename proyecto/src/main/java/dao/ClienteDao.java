package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

public class ClienteDao {
    public boolean crear(Cliente cliente) throws SQLException {
        String sql = """
                INSERT INTO public.cliente (dni, nombre, apellido, telefono, email)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            cargarCampos(sentencia, cliente);
            return sentencia.executeUpdate() == 1;
        }
    }

    public boolean modificar(String dniOriginal, Cliente cliente) throws SQLException {
        String sql = """
                UPDATE public.cliente
                SET dni = ?, nombre = ?, apellido = ?, telefono = ?, email = ?
                WHERE dni = ?
                """;
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            cargarCampos(sentencia, cliente);
            sentencia.setString(6, dniOriginal);
            return sentencia.executeUpdate() == 1;
        }
    }

    public boolean eliminar(String dni) throws SQLException {
        String sql = "DELETE FROM public.cliente WHERE dni = ?";
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, dni);
            return sentencia.executeUpdate() == 1;
        } catch (SQLException ex) {
            // PostgreSQL puede informar RESTRICT o una violación de FK según su versión.
            if ("23001".equals(ex.getSQLState()) || "23503".equals(ex.getSQLState())) {
                throw new SQLException("No se puede eliminar un cliente que tiene vehículos.",
                        ex.getSQLState(), ex.getErrorCode(), ex);
            }
            throw ex;
        }
    }

    public Cliente extraer(String dni) throws SQLException {
        String sql = """
                SELECT dni, nombre, apellido, telefono, email
                FROM public.cliente
                WHERE dni = ?
                """;
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, dni);
            try (ResultSet filas = sentencia.executeQuery()) {
                return filas.next() ? leerCliente(filas) : null;
            }
        }
    }

    public List<Cliente> listar() throws SQLException {
        String sql = """
                SELECT dni, nombre, apellido, telefono, email
                FROM public.cliente
                ORDER BY apellido, nombre, dni
                """;
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet filas = sentencia.executeQuery()) {
            while (filas.next()) {
                clientes.add(leerCliente(filas));
            }
        }
        return clientes;
    }

    private static void cargarCampos(PreparedStatement sentencia, Cliente cliente) throws SQLException {
        sentencia.setString(1, cliente.getDni());
        sentencia.setString(2, cliente.getNombre());
        sentencia.setString(3, cliente.getApellido());
        sentencia.setString(4, cliente.getTelefono());
        sentencia.setString(5, cliente.getEmail());
    }

    static Cliente leerCliente(ResultSet fila) throws SQLException {
        return new Cliente(fila.getString("dni"), fila.getString("nombre"),
                fila.getString("apellido"), fila.getString("telefono"), fila.getString("email"));
    }
}
