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

import static modelo.dbConfig.ConexionDB.ejecutarSentenciaParametrizada;

public class InscripcionDAO {
    private static final Logger logger = Logger.getLogger(InscripcionDAO.class.getName());

    public InscripcionDAO() {}

    public void insertarInscripcion(Inscripcion inscripcion) {
        String sql = "INSERT INTO inscripciones (id, curso_id, estudiante_id, anio, semestre) VALUES (?, ?, ?, ?, ?)";
        ejecutarSentenciaParametrizada(sql,
                inscripcion.getID(),
                inscripcion.getCurso().getID(),
                inscripcion.getEstudiante().getID(),
                inscripcion.getAnio(),
                inscripcion.getSemestre()
        );
    }

    public void actualizarInscripcion(Inscripcion inscripcion) {
        String sql = "UPDATE inscripciones SET curso_id = ?, estudiante_id = ?, anio = ?, semestre = ? WHERE id = ?";
        ejecutarSentenciaParametrizada(sql,
                inscripcion.getCurso().getID(),
                inscripcion.getEstudiante().getID(),
                inscripcion.getAnio(),
                inscripcion.getSemestre(),
                inscripcion.getID()
        );
    }

    public void eliminarInscripcion(Inscripcion inscripcion) {
        String sql = "DELETE FROM inscripciones WHERE id = ?";
        ejecutarSentenciaParametrizada(sql, inscripcion.getID());
    }

    public List<Inscripcion> obtenerTodasLasInscripciones() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = """
            SELECT i.id AS inscripcion_id, i.anio, i.semestre,
                   c.id AS curso_id, c.nombre AS curso_nombre,
                   e.id AS estudiante_id, e.codigo AS estudiante_codigo,
                   p.nombres AS estudiante_nombre, p.apellidos AS estudiante_apellido, p.email AS estudiante_email,
                   e.activo AS estudiante_activo, e.promedio AS estudiante_promedio
            FROM inscripciones i
            JOIN cursos c ON i.curso_id = c.id
            JOIN estudiantes e ON i.estudiante_id = e.id
            JOIN personas p ON e.id = p.id
        """;

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
                Estudiante estudiante = new Estudiante(
                        rs.getDouble("estudiante_id"),
                        rs.getString("estudiante_nombre"),
                        rs.getString("estudiante_apellido"),
                        rs.getString("estudiante_email"),
                        rs.getDouble("estudiante_codigo"),
                        rs.getBoolean("estudiante_activo"),
                        rs.getDouble("estudiante_promedio"),
                        null
                );
                Inscripcion inscripcion = new Inscripcion(
                        rs.getDouble("id"),
                        curso,
                        rs.getInt("anio"),
                        rs.getInt("semestre"),
                        estudiante
                );
                inscripciones.add(inscripcion);
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener todas las inscripciones", exception);
        }
        return inscripciones;
    }
}