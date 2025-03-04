package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {
    private static final String URL = "jdbc:h2:./database/institucion";
    private static final String USUARIO = "sa";
    private static final String PASSWORD = "";

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
        String[] sentenciasSQL = {
                """
                CREATE TABLE IF NOT EXISTS personas (
                    id DOUBLE PRIMARY KEY,
                    nombres VARCHAR(100) NOT NULL,
                    apellidos VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL
                )
                """,
                """
                CREATE TABLE IF NOT EXISTS facultades (
                    id DOUBLE PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    decano_id DOUBLE NOT NULL,
                    FOREIGN KEY (decano_id) REFERENCES personas(id) ON DELETE RESTRICT
                )
                """,
                """
                CREATE TABLE IF NOT EXISTS programas (
                    id DOUBLE PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    duracion DOUBLE NOT NULL,
                    registro VARCHAR(50) NOT NULL,
                    facultad_id DOUBLE NOT NULL,
                    FOREIGN KEY (facultad_id) REFERENCES facultades(id) ON DELETE RESTRICT
                )
                """,
                """
                CREATE TABLE IF NOT EXISTS profesores (
                    id DOUBLE PRIMARY KEY,
                    tipoContrato VARCHAR(100) NOT NULL,
                    FOREIGN KEY (id) REFERENCES personas(id) ON DELETE CASCADE
                )
                """,
                """
                CREATE TABLE IF NOT EXISTS estudiantes (
                    id DOUBLE PRIMARY KEY,
                    codigo DOUBLE NOT NULL,
                    activo BOOLEAN NOT NULL,
                    promedio DOUBLE NOT NULL,
                    programa_id DOUBLE NOT NULL,
                    FOREIGN KEY (id) REFERENCES personas(id) ON DELETE CASCADE,
                    FOREIGN KEY (programa_id) REFERENCES programas(id) ON DELETE RESTRICT
                )
                """,
                """
                CREATE TABLE IF NOT EXISTS cursos (
                    id INT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    activo BOOLEAN NOT NULL,
                    programa_id DOUBLE NOT NULL,
                    FOREIGN KEY (programa_id) REFERENCES programas(id) ON DELETE RESTRICT
                )
                """,
                """
                CREATE TABLE IF NOT EXISTS inscripciones (
                    id DOUBLE PRIMARY KEY,
                    curso_id INT NOT NULL,
                    estudiante_id DOUBLE NOT NULL,
                    anio INT NOT NULL,
                    semestre INT NOT NULL,
                    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE RESTRICT,
                    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE
                )
                """
        };

        try (Connection conn = getConexion(); Statement stmt = conn.createStatement()) {
            for (String sql : sentenciasSQL) {
                stmt.execute(sql);
            }
            System.out.println("Base de datos inicializada correctamente.");
        } catch (SQLException exception) {
            System.err.println("Error al inicializar la base de datos: " + exception.getMessage());
        }
    }
}