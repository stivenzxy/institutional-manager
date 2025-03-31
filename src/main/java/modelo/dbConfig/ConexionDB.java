package modelo.dbConfig;

import main.utils.Configuracion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionDB {
    private Connection conexion;
    private static ConexionDB instancia;

    private static final String URL = Configuracion.getPropiedad("URL");
    private static final String USUARIO = Configuracion.getPropiedad("USUARIO");
    private static final String PASSWORD = Configuracion.getPropiedad("PASSWORD");
    private static final Logger logger = Logger.getLogger(ConexionDB.class.getName());

    private ConexionDB() {
        this.getConexion();
    }

    public static ConexionDB obtenerInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener conexión: " + e.getMessage());
        }
        return conexion;
    }

    public void cerrarConexion() {
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
                instancia = null;
            }
        }
    }

    public void inicializarBaseDeDatos() {
        try {
            Path scriptPath = Path.of("database/schema.sql");
            System.out.println("Cargando script desde: " + scriptPath.toAbsolutePath());

            String scriptSql = Files.readString(scriptPath, StandardCharsets.UTF_8);
            ejecutarScriptSQL(scriptSql);
        } catch (IOException e) {
            System.err.println("Error al cargar el script de la base de datos: " + e.getMessage());
        }
    }

    @SuppressWarnings("SqlSourceToSinkFlow")
    public void ejecutarScriptSQL(String scriptSql) {
        try (Statement stmt = getConexion().createStatement()) {
            for (String sql : scriptSql.split(";")) {
                if (!sql.trim().isEmpty()) {
                    stmt.execute(sql);
                }
            }
            System.out.println("Script ejecutado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error ejecutando el script: " + e.getMessage());
        }
    }

    @SuppressWarnings("SqlSourceToSinkFlow")
    public void ejecutarSentenciaParametrizada(String sql, Object... valores) {
        Connection conn = getConexion();
        PreparedStatement pstmt = null;

        try {
            conn.setAutoCommit(false); // Los cambios solo se confirman cuando finalice la operación
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < valores.length; i++) {
                pstmt.setObject(i + 1, valores[i]);
            }

            pstmt.executeUpdate();
            conn.commit(); // Se confirman los cambios cuando las instrucciones se ejecutan correctamente

        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al ejecutar SQL con parámetros: " + sql, exception);
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Error al hacer rollback", rollbackEx);
            }
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true);
                cerrarConexion();
            } catch (SQLException closeEx) {
                logger.log(Level.SEVERE, "Error al cerrar recursos", closeEx);
            }
        }
    }
}