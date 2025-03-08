package modelo.dbConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionDB {
    private static final String URL = "jdbc:h2:./database/institucion";
    private static final String USUARIO = "sa";
    private static final String PASSWORD = "";
    private static final Logger logger = Logger.getLogger(ConexionDB.class.getName());

    private static Connection conexion;

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión exitosa a la base de datos!");
            }
        } catch (SQLException exception) {
            System.err.println("Error al conectar con la base de datos: " + exception.getMessage());
        }
        return conexion;
    }

    public static void finalizarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    System.out.println("Conexión cerrada correctamente.");
                }
            } catch (SQLException exception) {
                System.err.println("Error al cerrar la conexión: " + exception.getMessage());
            } finally {
                conexion = null;
            }
        }
    }

    public static void inicializarBaseDeDatos() {
        try {
            Path scriptPath = Path.of("database/schema.sql");
            System.out.println("Cargando script desde: " + scriptPath.toAbsolutePath());

            String scriptSql = Files.readString(scriptPath);
            System.out.println("Contenido del script:");
            System.out.println(scriptSql);

            ejecutarScriptSQL(scriptSql);
        } catch (IOException e) {
            System.err.println("Error al cargar el script de la base de datos: " + e.getMessage());
        }
    }

    @SuppressWarnings("SqlSourceToSinkFlow")
    public static void ejecutarScriptSQL(String scriptSql) {
        try (Connection conn = getConexion();
             Statement stmt = conn.createStatement()) {

            for (String sql : scriptSql.split(";")) {
                if (!sql.trim().isEmpty()) {
                    System.out.println("Ejecutando: " + sql);
                    stmt.execute(sql);
                }
            }
            System.out.println("Script ejecutado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error ejecutando el script: " + e.getMessage());
        }
    }


    @SuppressWarnings("SqlSourceToSinkFlow")
    public static void ejecutarSentenciaParametrizada(String sql, Object... valores) {
        try (Connection conn = getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (int i = 0; i < valores.length; i++) {
                pstmt.setObject(i + 1, valores[i]);
            }

            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al ejecutar SQL con parámetros: " + sql, exception);
        }
    }
}