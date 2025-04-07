package DAO;

import fabricas.DAOFactory;
import modelo.dbConfig.ConexionDB;
import modelo.entidades.Estudiante;
import modelo.institucion.Programa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EstudianteDAO {
    private static final Logger logger = Logger.getLogger(EstudianteDAO.class.getName());
    private final PersonaDAO personaDAO;

    public EstudianteDAO() {
        personaDAO = DAOFactory.crearPersonaDAO();
    }

    public void insertar(Estudiante estudiante) {
        double idPersona = personaDAO.insertar(estudiante);

        String sql = "INSERT INTO estudiantes (id, codigo, activo, promedio, programa_id) VALUES (?, ?, ?, ?, ?)";
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql, idPersona, estudiante.getCodigo(), estudiante.isActivo(), estudiante.getPromedio(), estudiante.getPrograma().getID());
    }

    public void eliminar(Estudiante estudiante) {
        double codigoEstudiante = estudiante.getCodigo();
        String sql = "DELETE FROM estudiantes WHERE codigo = ?";
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql, codigoEstudiante);
    }

    public Estudiante buscarPorCodigo(double codigo) {
        String query = "SELECT e.id, p.nombres, p.apellidos, p.email, e.codigo, e.activo, e.promedio, pr.id AS programa_id, pr.nombre AS programa_nombre " +
                "FROM estudiantes e " +
                "JOIN personas p ON e.id = p.id " +
                "JOIN programas pr ON e.programa_id = pr.id " +
                "WHERE e.codigo = ?";

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Estudiante estudiante = new Estudiante(
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getDouble("codigo"),
                            rs.getBoolean("activo"),
                            rs.getDouble("promedio"),
                            new Programa(rs.getDouble("programa_id"), rs.getString("programa_nombre"))
                    );
                    estudiante.setID(rs.getDouble("id"));
                    return estudiante;
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener el estudiante por código", exception);
        }
        return null;
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String query = "SELECT e.id, p.nombres, p.apellidos, p.email, e.codigo, e.activo, e.promedio, pr.id AS programa_id, pr.nombre AS programa_nombre " +
                "FROM estudiantes e " +
                "JOIN personas p ON e.id = p.id " +
                "JOIN programas pr ON e.programa_id = pr.id";

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Estudiante estudiante = new Estudiante(
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getDouble("codigo"),
                        rs.getBoolean("activo"),
                        rs.getDouble("promedio"),
                        new Programa(rs.getDouble("programa_id"), rs.getString("programa_nombre"))
                );
                estudiante.setID(rs.getDouble("id"));
                estudiantes.add(estudiante);
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener todas las asignaciones", exception);
        }
        return estudiantes;
    }
}