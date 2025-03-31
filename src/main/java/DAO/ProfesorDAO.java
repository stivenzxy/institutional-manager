package DAO;

import modelo.dbConfig.ConexionDB;
import modelo.entidades.Estudiante;
import modelo.entidades.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfesorDAO {
    private static final Logger logger = Logger.getLogger(ProfesorDAO.class.getName());
    PersonaDAO personaDAO;

    public ProfesorDAO() {
        personaDAO = new PersonaDAO();
    }

    public void insertar(Profesor profesor) {
        double idPersona = personaDAO.insertar(profesor);

        String sql = "INSERT INTO profesores (id, tipoContrato) VALUES (?, ?)";
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql, idPersona, profesor.getTipoContrato());
    }

    public void eliminar(Profesor profesor) {
        double idProfesor = profesor.getID();
        String sql = "DELETE FROM profesores WHERE id = ?";
        ConexionDB.obtenerInstancia().ejecutarSentenciaParametrizada(sql, idProfesor);
    }

    public Profesor obtenerProfesorPorId(double id) {
        String sql = "SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipoContrato " +
                "FROM profesores pr " +
                "JOIN personas p ON pr.id = p.id " +
                "WHERE pr.id = ?";
        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Profesor(
                            rs.getDouble("id"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("tipoContrato")
                    );
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener profesor por ID", exception);
        }
        return null;
    }

    public List<Profesor> obtenerTodosLosProfesores() {
        List<Profesor> profesores = new ArrayList<>();
        String sql = "SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipoContrato " +
                "FROM profesores pr " +
                "JOIN personas p ON pr.id = p.id";
        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                profesores.add(new Profesor(
                        rs.getDouble("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("tipoContrato")
                ));
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener todos los profesores", exception);
        }
        return profesores;
    }
}