package DAO;

import modelo.dbConfig.ConexionDB;
import modelo.institucion.Curso;
import modelo.institucion.Programa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CursoDAO {
    private static final Logger logger = Logger.getLogger(CursoDAO.class.getName());

    public CursoDAO() {}

    public void insertarCurso(Curso curso) {
        String sql = "INSERT INTO cursos (codigo, nombre, activo, programa_id) VALUES (?, ?, ?, ?)";
        System.out.println("insert from cursos");
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql, curso.getCodigo(), curso.getNombre(), curso.isActivo(), curso.getPrograma().getID());
    }

    public void eliminarCurso(Curso curso) {
        double codigoCurso = curso.getCodigo();
        String sql = "DELETE FROM cursos WHERE codigo = ?";
        System.out.println("delete from h2 cursos");
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql, codigoCurso);
    }

    public boolean tieneInscripciones(double codigoCurso) {
        String sql = "SELECT COUNT(*) FROM inscripciones WHERE curso_id = (SELECT id FROM cursos WHERE codigo = ?)";

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, codigoCurso);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al verificar inscripciones del curso con código: " + codigoCurso, exception);
        }
        return false;
    }

    public Curso buscarPorCodigo(double codigo) {
        String sql = "SELECT c.id, c.codigo, c.nombre, c.activo, p.id AS programa_id, p.nombre AS nombre_programa " +
                "FROM cursos c " +
                "JOIN programas p ON c.programa_id = p.id " +
                "WHERE c.codigo = ?";

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Programa programa = new Programa(rs.getDouble("programa_id"), rs.getString("nombre_programa"));
                    Curso curso = new Curso(rs.getDouble("codigo"),rs.getString("nombre"), programa, rs.getBoolean("activo")
                    );

                    curso.setID(rs.getInt("id"));
                    return curso;
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener el curso con código: " + codigo, exception);
        }
        return null;
    }

    public List<Curso> obtenerTodosLosCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT c.id, c.codigo, c.nombre, c.activo, p.id AS programa_id, p.nombre AS nombre_programa " +
                "FROM cursos c " +
                "JOIN programas p ON c.programa_id = p.id";

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Programa programa = new Programa(rs.getDouble("programa_id"), rs.getString("nombre_programa"));
                cursos.add(new Curso(rs.getDouble("codigo"), rs.getString("nombre"), programa, rs.getBoolean("activo")));
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener todos los cursos", exception);
        }
        return cursos;
    }
}