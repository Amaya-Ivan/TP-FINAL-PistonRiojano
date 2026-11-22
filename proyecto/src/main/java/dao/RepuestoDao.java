package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Repuesto;

public class RepuestoDao {
    public List<Repuesto> listar() throws SQLException {
        String sql = """
                SELECT codigo AS repuesto_codigo, descripcion AS repuesto_descripcion, precio_unitario, stock
                FROM public.repuesto
                ORDER BY codigo
                """;
        List<Repuesto> repuestos = new ArrayList<>();
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet filas = sentencia.executeQuery()) {
            while (filas.next()) {
                repuestos.add(leerRepuesto(filas));
            }
        }
        return repuestos;
    }

    public boolean modificar(Repuesto repuesto) throws SQLException {
        String sql = "UPDATE public.repuesto SET precio_unitario = ?, stock = ? WHERE codigo = ?";
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setBigDecimal(1, repuesto.getPrecioUnitario());
            sentencia.setInt(2, repuesto.getStock());
            sentencia.setString(3, repuesto.getCodigo());
            return sentencia.executeUpdate() == 1;
        }
    }

    static Repuesto leerRepuesto(ResultSet fila) throws SQLException {
        return new Repuesto(fila.getString("repuesto_codigo"), fila.getString("repuesto_descripcion"),
                fila.getBigDecimal("precio_unitario"), fila.getInt("stock"));
    }
}
