package dao;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Conexion {
    private Conexion() {
    }

    public static Connection obtenerConexion() throws SQLException {
        Path archivo = Path.of(System.getProperty("piston.db.config", "db.properties"));
        Properties propiedades = new Properties();
        try (Reader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            propiedades.load(lector);
        } catch (IOException | IllegalArgumentException ex) {
            throw new SQLException("No se pudo leer la configuración de PostgreSQL en "
                    + archivo.toAbsolutePath() + ".", ex);
        }

        String url = propiedades.getProperty("db.url", "").strip();
        String usuario = propiedades.getProperty("db.usuario", "").strip();
        String password = propiedades.getProperty("db.password", "");
        if (!url.startsWith("jdbc:postgresql:") || usuario.isEmpty()) {
            throw new SQLException("Revisá db.url y db.usuario en " + archivo.toAbsolutePath() + ".");
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se encontró el driver JDBC de PostgreSQL.", ex);
        }
        return DriverManager.getConnection(url, usuario, password);
    }
}
