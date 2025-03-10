package DAO;

import modelo.dbConfig.ConexionDB;
import modelo.entidades.Estudiante;
import modelo.entidades.Persona;
import modelo.entidades.Profesor;
import modelo.institucion.Programa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonaDAO {
    private static final Logger logger = Logger.getLogger(PersonaDAO.class.getName());

    public PersonaDAO() {}

    public double insertarPersona(Persona persona) {
        String sql = "INSERT INTO personas (nombres, apellidos, email) VALUES (?, ?, ?)";
        double idGenerado = -1;

        try (Connection conn = ConexionDB.getConexion();
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

    public void insertarProfesor(Profesor profesor) {
        double idPersona = insertarPersona(profesor);
        if (idPersona == -1) return;

        String sql = "INSERT INTO profesores (id, tipoContrato) VALUES (?, ?)";
        ConexionDB.ejecutarSentenciaParametrizada(sql, idPersona, profesor.getTipoContrato());
    }

    public void insertarEstudiante(Estudiante estudiante) {
        double idPersona = insertarPersona(estudiante);
        if (idPersona == -1) return;

        String sql = "INSERT INTO estudiantes (id, codigo, activo, promedio, programa_id) VALUES (?, ?, ?, ?, ?)";
        ConexionDB.ejecutarSentenciaParametrizada(sql, idPersona, estudiante.getCodigo(), estudiante.isActivo(), estudiante.getPromedio(), estudiante.getPrograma().getID());
    }

    public void actualizarPersona(Persona persona) {
        String sql = "UPDATE personas SET nombres = ?, apellidos = ?, email = ? WHERE id = ?";
        ConexionDB.ejecutarSentenciaParametrizada(sql, persona.getNombres(), persona.getApellidos(), persona.getEmail(), persona.getID());
    }

    public void actualizarProfesor(Profesor profesor) {
        actualizarPersona(profesor);
        String sql = "UPDATE profesores SET tipoContrato = ? WHERE id = ?";
        ConexionDB.ejecutarSentenciaParametrizada(sql, profesor.getTipoContrato(), profesor.getID());
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        actualizarPersona(estudiante);
        String sql = "UPDATE estudiantes SET codigo = ?, activo = ?, promedio = ?, programa_id = ? WHERE id = ?";
        ConexionDB.ejecutarSentenciaParametrizada(sql, estudiante.getCodigo(), estudiante.isActivo(), estudiante.getPromedio(), estudiante.getPrograma().getID(), estudiante.getID());
    }

    public void eliminarPersona(double id) {
        String sql = "DELETE FROM personas WHERE id = ?";
        ConexionDB.ejecutarSentenciaParametrizada(sql, id);
    }

    public List<Persona> obtenerTodasLasPersonas() {
        List<Persona> personas = new ArrayList<>();
        String sql = """
            SELECT p.id, p.nombres, p.apellidos, p.email,
                   e.codigo, e.activo, e.promedio, prog.nombre AS nombre_programa,
                   pr.tipoContrato
            FROM personas p
            LEFT JOIN estudiantes e ON p.id = e.id
            LEFT JOIN programas prog ON e.programa_id = prog.id
            LEFT JOIN profesores pr ON p.id = pr.id
        """;

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                double id = rs.getDouble("id");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String email = rs.getString("email");

                if (rs.getString("codigo") != null) {
                    Programa programa = new Programa(
                            rs.getDouble("id"),
                            rs.getString("nombre_programa")
                    );
                    personas.add(new Estudiante(
                            id, nombres, apellidos, email,
                            rs.getDouble("codigo"),
                            rs.getBoolean("activo"),
                            rs.getDouble("promedio"),
                            programa
                    ));
                } else if (rs.getString("tipoContrato") != null) {
                    personas.add(new Profesor(
                            id, nombres, apellidos, email,
                            rs.getString("tipoContrato")
                    ));
                } else {
                    personas.add(new Persona(id, nombres, apellidos, email));
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error al obtener todas las personas", exception);
        }
        return personas;
    }
}