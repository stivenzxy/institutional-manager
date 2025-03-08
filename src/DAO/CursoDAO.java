package DAO;

import modelo.dbConfig.ConexionDB;
import modelo.institucion.Curso;
import modelo.institucion.Programa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static modelo.dbConfig.ConexionDB.ejecutarSentenciaParametrizada;

public class CursoDAO {
    private static final Logger logger = Logger.getLogger(CursoDAO.class.getName());

    public CursoDAO() {}

    public void insertarCurso(Curso curso) {
        String sql = "INSERT INTO cursos (id, nombre, activo, programa_id) VALUES (?, ?, ?, ?)";
        ejecutarSentenciaParametrizada(sql, curso.getID(), curso.getNombre(), curso.isActivo(), curso.getPrograma().getID());
    }

    public void actualizarCurso(Curso curso) {
        String sql = "UPDATE cursos SET nombre = ?, activo = ?, programa_id = ? WHERE id = ?";
        ejecutarSentenciaParametrizada(sql, curso.getNombre(), curso.isActivo(), curso.getPrograma().getID(), curso.getID());
    }

    public void eliminarCurso(int id) {
        String sql = "DELETE FROM cursos WHERE id = ?";
        ejecutarSentenciaParametrizada(sql, id);
    }

    public List<Curso> obtenerCursosPorPrograma(double programaId) {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT c.id, c.nombre, c.activo, p.id AS programa_id, p.nombre AS nombre_programa " +
                "FROM cursos c " +
                "JOIN programas p ON c.programa_id = p.id " +
                "WHERE p.id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, programaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Programa programa = new Programa(rs.getDouble("programa_id"), rs.getString("nombre_programa"));
                    cursos.add(new Curso(rs.getInt("id"), rs.getString("nombre"), programa, rs.getBoolean("activo")));
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener cursos por programa", exception);
        }
        return cursos;
    }

    public List<Curso> obtenerTodosLosCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT c.id, c.nombre, c.activo, p.id AS programa_id, p.nombre AS nombre_programa " +
                "FROM cursos c " +
                "JOIN programas p ON c.programa_id = p.id";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Programa programa = new Programa(rs.getDouble("programa_id"), rs.getString("nombre_programa"));
                cursos.add(new Curso(rs.getInt("id"), rs.getString("nombre"), programa, rs.getBoolean("activo")));
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener todos los cursos", exception);
        }
        return cursos;
    }
}
