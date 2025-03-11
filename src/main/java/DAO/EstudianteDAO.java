package DAO;

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

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String query = "SELECT e.id, p.nombres, p.apellidos, p.email, e.codigo, e.activo, e.promedio, pr.id AS programa_id, pr.nombre AS programa_nombre " +
                "FROM estudiantes e " +
                "JOIN personas p ON e.id = p.id " +
                "JOIN programas pr ON e.programa_id = pr.id";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Estudiante estudiante = new Estudiante(
                        rs.getDouble("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getDouble("codigo"),
                        rs.getBoolean("activo"),
                        rs.getDouble("promedio"),
                        new Programa(rs.getDouble("programa_id"), rs.getString("programa_nombre"))
                );
                estudiantes.add(estudiante);
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener todas las asignaciones", exception);
        }
        return estudiantes;
    }
}