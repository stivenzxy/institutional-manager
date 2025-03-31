package DAO;

import modelo.dbConfig.ConexionDB;
import modelo.entidades.Persona;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonaDAO {
    private static final Logger logger = Logger.getLogger(PersonaDAO.class.getName());

    public double insertar(Persona persona) {
        String sql = "INSERT INTO personas (nombres, apellidos, email) VALUES (?, ?, ?)";
        double idGenerado = -1;

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion() ;
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, persona.getNombres());
            pstmt.setString(2, persona.getApellidos());
            pstmt.setString(3, persona.getEmail());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                idGenerado = generatedKeys.getDouble(1);
            } else {
                throw new SQLException("Error al obtener el ID generado de persona.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al insertar persona", e);
        }
        return idGenerado;
    }
}