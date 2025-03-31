package DAO;

import modelo.dbConfig.ConexionDB;
import modelo.institucion.Curso;
import modelo.entidades.Profesor;
import modelo.relaciones.CursoProfesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CursoProfesorDAO {
    private static final Logger logger = Logger.getLogger(CursoProfesorDAO.class.getName());

    public CursoProfesorDAO() {}

    public void asignarProfesorACurso(CursoProfesor cursoProfesor) throws Exception {
        if (existeAsignacion(cursoProfesor.getCurso().getID(), cursoProfesor.getProfesor().getID())) {
            throw new Exception("Error: Este profesor ya está asignado a este curso.");
        }

        String sql = "INSERT INTO curso_profesor (curso_id, profesor_id, anio, semestre) VALUES (?,?,?,?)";

        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql, cursoProfesor.getCurso().getID(), cursoProfesor.getProfesor().getID(), cursoProfesor.getAnio(), cursoProfesor.getSemestre());
    }

    public void eliminarAsignacion(CursoProfesor cursoProfesor) {
        String sql = "DELETE FROM curso_profesor WHERE curso_id = ? AND profesor_id = ?";
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql, cursoProfesor.getCurso().getID(), cursoProfesor.getProfesor().getID());
    }

    public List<CursoProfesor> obtenerTodasLasAsignaciones() {
        List<CursoProfesor> asignaciones = new ArrayList<>();
        String sql = "SELECT c.id AS curso_id, c.nombre AS curso_nombre, " +
                "p.id AS profesor_id, p.nombres, p.apellidos, p.email, pr.tipoContrato, " +
                "cp.anio, cp.semestre " +
                "FROM curso_profesor cp " +
                "JOIN cursos c ON cp.curso_id = c.id " +
                "JOIN profesores pr ON cp.profesor_id = pr.id " +
                "JOIN personas p ON pr.id = p.id";

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Curso curso = new Curso(rs.getInt("curso_id"), rs.getString("curso_nombre"));
                Profesor profesor = new Profesor(
                        rs.getDouble("profesor_id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("tipoContrato")
                );
                int anio = rs.getInt("anio");
                int semestre = rs.getInt("semestre");

                asignaciones.add(new CursoProfesor(profesor, anio, semestre, curso));
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener todas las asignaciones", exception);
        }
        return asignaciones;
    }

    public boolean existeAsignacion(int cursoId, double profesorId) {
        String sql = "SELECT COUNT(*) FROM curso_profesor WHERE curso_id = ? AND profesor_id = ?";

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cursoId);
            pstmt.setDouble(2, profesorId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return false;
    }

}