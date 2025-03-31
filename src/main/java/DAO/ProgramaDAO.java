package DAO;

import modelo.dbConfig.ConexionDB;
import modelo.institucion.Programa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgramaDAO {
    private static final Logger logger = Logger.getLogger(ProgramaDAO.class.getName());

    public ProgramaDAO() {}

    public List<Programa> obtenerTodosLosProgramas() {
        List<Programa> programas = new ArrayList<>();
        String sql = "SELECT id, nombre FROM programas";

        try (Connection conn = ConexionDB.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                programas.add(new Programa(rs.getDouble("id"), rs.getString("nombre")));
            }

        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener todos los programas", exception);
        }
        return programas;
    }
}
