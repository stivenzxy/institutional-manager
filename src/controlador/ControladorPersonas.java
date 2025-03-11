package controlador;

import DAO.ProgramaDAO;
import modelo.entidades.Estudiante;
import modelo.entidades.Persona;
import modelo.entidades.Profesor;
import modelo.institucion.Programa;
import modelo.relaciones.InscripcionesPersonas;
import vista.GestionPersonasGUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.text.DecimalFormat;
import java.util.stream.IntStream;

public class ControladorPersonas {
    private final InscripcionesPersonas modelo;
    private final PropertyChangeSupport notificadorMensajes;

    public ControladorPersonas() {
        modelo = new InscripcionesPersonas();
        notificadorMensajes = new PropertyChangeSupport(this);
    }

    private Persona convertirDatosAPersona(Map<String, Object> datos) {
        String nombres = (String) datos.get("nombres");
        String apellidos = (String) datos.get("apellidos");
        String email = (String) datos.get("email");
        String tipo = (String) datos.get("tipo");

        if (nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("Por favor, complete todos los campos.");
        }

        if ("Estudiante".equals(tipo)) {
            int codigo = Integer.parseInt((String) datos.get("codigo"));
            double promedio = Double.parseDouble((String) datos.get("promedio"));
            boolean activo = (Boolean) datos.get("activo");
            Programa programa = (Programa) datos.get("programa");

            return new Estudiante(nombres, apellidos, email, codigo, activo, promedio, programa);
        } else if ("Profesor".equals(tipo)) {
            String tipoContrato = (String) datos.get("tipoContrato");
            return new Profesor(nombres, apellidos, email, tipoContrato);
        }

        return new Persona(nombres, apellidos, email);
    }

    public void guardarPersona(Map<String, Object> datos) {
        try {
            Persona persona = convertirDatosAPersona(datos);
            modelo.inscribir(persona);
            notificadorMensajes.firePropertyChange("mensaje", null, "Persona guardada exitosamente!");
            cargarPersonas();
        } catch (IllegalArgumentException exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, exception.getMessage());
        }
    }

    public void actualizarPersona(Map<String, Object> datos) {
        try {
            Persona persona = convertirDatosAPersona(datos);
            double id = (double) datos.get("id");
            persona.setID(id);

            modelo.actualizar(persona);
            cargarPersonas();
            notificadorMensajes.firePropertyChange("mensaje", null, "Persona actualizada correctamente.");
        } catch (IllegalArgumentException exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, "Error al procesar los datos.");
        }
    }

    public void eliminarPersona(double id) {
        Persona personaAEliminar = modelo.getPersonas().stream()
                .filter(p -> Double.compare(p.getID(), id) == 0)
                .findFirst()
                .orElse(null);

        if (personaAEliminar == null) {
            notificadorMensajes.firePropertyChange("mensaje", null, "No se encontró la persona.");
            return;
        }

        modelo.eliminar(personaAEliminar);
        cargarPersonas();
        notificadorMensajes.firePropertyChange("mensaje", null, "Persona eliminada correctamente.");
    }

    public void cargarPersonas() {
        modelo.cargarDatosDB();
        List<Object[]> datos = modelo.obtenerDatosPersonas();
        notificadorMensajes.firePropertyChange("datosPersonas", null, datos);
    }

    public void agregarListener(PropertyChangeListener listener) {
        notificadorMensajes.addPropertyChangeListener(listener);
    }

    public void cargarProgramas() {
        ProgramaDAO programaDAO = new ProgramaDAO();
        List<Programa> programas = programaDAO.obtenerTodosLosProgramas();
        notificadorMensajes.firePropertyChange("programasCargados", null, programas);
    }
}