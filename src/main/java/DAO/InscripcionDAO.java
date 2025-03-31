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
        String sql = "INSERT INTO inscripciones (curso_id, estudiante_id, anio, semestre) VALUES (?, ?, ?, ?)";
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql,
                inscripcion.getCurso().getID(),
                inscripcion.getEstudiante().getID(),
                inscripcion.getAnio(),
                inscripcion.getSemestre()
        );
    }

    public void actualizarInscripcion(Inscripcion inscripcion) {
        String sql = "UPDATE inscripciones SET curso_id = ?, estudiante_id = ?, anio = ?, semestre = ? WHERE id = ?";
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql,
                inscripcion.getCurso().getID(),
                inscripcion.getEstudiante().getID(),
                inscripcion.getAnio(),
                inscripcion.getSemestre(),
                inscripcion.getID()
        );
    }

    public void eliminarInscripcion(Inscripcion inscripcion) {
        String sql = "DELETE FROM inscripciones WHERE id = ?";
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql, inscripcion.getID());
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