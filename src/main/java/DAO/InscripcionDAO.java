package DAO;

import modelo.dbConfig.ConexionDB;
import modelo.entidades.Estudiante;
import modelo.institucion.Curso;
import modelo.institucion.Inscripcion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InscripcionDAO {
    private static final Logger logger = Logger.getLogger(InscripcionDAO.class.getName());

    public InscripcionDAO() {}

    public void insertarInscripcion(Inscripcion inscripcion) {
        String sql = "INSERT INTO inscripciones (curso_id, estudiante_id, anio, periodo) VALUES (?, ?, ?, ?)";
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql,
                inscripcion.getCurso().getID(),
                inscripcion.getEstudiante().getID(),
                inscripcion.getAnio(),
                inscripcion.getPeriodo()
        );
    }

    public List<Inscripcion> obtenerTodasLasInscripciones() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = """
        SELECT i.id AS inscripcion_id, i.anio, i.PERIODO,
               c.id AS curso_id, c.nombre AS curso_nombre,
               e.id AS estudiante_id, e.codigo AS estudiante_codigo,
               p.nombres AS estudiante_nombre, p.apellidos AS estudiante_apellido, p.email AS estudiante_email,
               e.activo AS estudiante_activo, e.promedio AS estudiante_promedio
        FROM inscripciones i
        JOIN cursos c ON i.curso_id = c.id
        JOIN estudiantes e ON i.estudiante_id = e.id
        JOIN personas p ON e.id = p.id
    """;

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getInt("curso_id"),
                        rs.getString("curso_nombre")
                );

                Estudiante estudiante = new Estudiante(
                        rs.getString("estudiante_nombre"),
                        rs.getString("estudiante_apellido"),
                        rs.getString("estudiante_email"),
                        rs.getDouble("estudiante_codigo"),
                        rs.getBoolean("estudiante_activo"),
                        rs.getDouble("estudiante_promedio"),
                        null
                );
                estudiante.setID(rs.getDouble("estudiante_id"));

                Inscripcion inscripcion = new Inscripcion(
                        rs.getDouble("inscripcion_id"),
                        curso,
                        rs.getInt("anio"),
                        rs.getInt("periodo"),
                        estudiante
                );

                inscripciones.add(inscripcion);
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener todas las inscripciones", exception);
        }
        return inscripciones;
    }

    public List<Inscripcion> obtenerPorEstudiante(Estudiante estudiante) {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = """
                    SELECT i.id AS inscripcion_id, i.anio, i.periodo,
                           c.id AS curso_id, c.nombre AS curso_nombre
                    FROM inscripciones i
                    JOIN cursos c ON i.curso_id = c.id
                    JOIN estudiantes e ON i.estudiante_id = e.id
                    WHERE e.codigo = ?
                    """;

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, estudiante.getCodigo());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getInt("curso_id"),
                        rs.getString("curso_nombre")
                );

                Inscripcion inscripcion = new Inscripcion(
                        rs.getDouble("inscripcion_id"),
                        curso,
                        rs.getInt("anio"),
                        rs.getInt("periodo"),
                        estudiante
                );

                inscripciones.add(inscripcion);
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener inscripciones por código de estudiante", exception);
        }
        return inscripciones;
    }
}