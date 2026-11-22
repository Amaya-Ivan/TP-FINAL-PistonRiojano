package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Especialidad;
import modelo.Mecanico;

public class MecanicoDao {
    public List<Mecanico> listar() throws SQLException {
        String sql = """
                SELECT legajo, nombre AS mecanico_nombre, apellido AS mecanico_apellido,
                       especialidad, valor_hora
                FROM public.mecanico
                ORDER BY apellido, nombre, legajo
                """;
        List<Mecanico> mecanicos = new ArrayList<>();
        try (Connection conexion = Conexion.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet filas = sentencia.executeQuery()) {
            while (filas.next()) {
                mecanicos.add(leerMecanico(filas));
            }
        }
        return mecanicos;
    }

    static Mecanico leerMecanico(ResultSet fila) throws SQLException {
        return new Mecanico(fila.getInt("legajo"), fila.getString("mecanico_nombre"),
                fila.getString("mecanico_apellido"),
                Especialidad.valueOf(fila.getString("especialidad")), fila.getBigDecimal("valor_hora"));
    }
}
