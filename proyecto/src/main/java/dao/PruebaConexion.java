package dao;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import modelo.Cliente;

public final class PruebaConexion {
    private PruebaConexion() {
    }

    public static void main(String[] args) {
        PrintWriter salida = new PrintWriter(System.out, true, StandardCharsets.UTF_8);
        try (Connection conexion = Conexion.obtenerConexion()) {
            String base = conexion.getCatalog();
            List<Cliente> clientes = new ClienteDao().listar();
            salida.println("Conexión JDBC correcta. Base: " + base);
            salida.println("Clientes encontrados: " + clientes.size());
            salida.println();
            salida.printf("%-12s | %-16s | %-16s | %-14s | %s%n",
                    "DNI", "Nombre", "Apellido", "Teléfono", "Correo");
            for (Cliente cliente : clientes) {
                String correo = cliente.getEmail() == null ? "(sin correo)" : cliente.getEmail();
                salida.printf("%-12s | %-16s | %-16s | %-14s | %s%n", cliente.getDni(),
                        cliente.getNombre(), cliente.getApellido(), cliente.getTelefono(), correo);
            }
        } catch (SQLException ex) {
            System.err.println("La prueba JDBC fallo. SQLState: " + ex.getSQLState());
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}
