package controlador;

import DAO.CursoDAO;
import DAO.EstudianteDAO;
import modelo.entidades.Estudiante;
import modelo.institucion.Curso;
import modelo.institucion.Inscripcion;
import modelo.relaciones.CursosInscritos;
import vista.GestionCursosEstudiantesGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ControladorCursosEstudiantes {
    private final CursosInscritos modelo;
    private final PropertyChangeSupport notificadorMensajes;

    public ControladorCursosEstudiantes() {
        modelo = new CursosInscritos();
        notificadorMensajes = new PropertyChangeSupport(this);
    }

    private Inscripcion convertirDatosAInscripcion(Map<String, Object> datos) {
        try {
            Estudiante estudiante = (Estudiante) datos.get("estudiante");
            Curso curso = (Curso) datos.get("curso");
            int anio = Integer.parseInt((String) datos.get("anio"));
            int semestre = Integer.parseInt((String) datos.get("semestre"));

            if (estudiante == null || curso == null) {
                throw new IllegalArgumentException("Seleccione un estudiante y un curso.");
            }

            return new Inscripcion(curso, anio, semestre, estudiante);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("El año y el semestre deben ser números válidos.");
        }
    }

    public void inscribirEstudianteACurso(Map<String, Object> datos) {
        try {
            Inscripcion inscripcion = convertirDatosAInscripcion(datos);
            modelo.inscribir(inscripcion);
            notificadorMensajes.firePropertyChange("mensaje", null, "Estudiante inscrito exitosamente!");
            cargarInscripciones();
        } catch (IllegalArgumentException exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, exception.getMessage());
        }
    }

    public void actualizarInscripcion(Map<String, Object> datos) {
        try {
            Inscripcion inscripcion = convertirDatosAInscripcion(datos);
            double id = (double) datos.get("id");
            inscripcion.setID(id);

            modelo.actualizar(inscripcion);
            cargarInscripciones();
            notificadorMensajes.firePropertyChange("mensaje", null, "Inscripción actualizada correctamente.");
        } catch (IllegalArgumentException exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, "Error al procesar los datos.");
        }
    }

    public void eliminarInscripcion(double inscripcionId) {
        Inscripcion inscripcionAEliminar = modelo.getListado().stream()
                .filter(i -> i.getID() == inscripcionId)
                .findFirst()
                .orElse(null);

        if (inscripcionAEliminar == null) {
            notificadorMensajes.firePropertyChange("mensaje", null, "No se encontró la inscripción.");
            return;
        }

        modelo.eliminar(inscripcionAEliminar);
        cargarInscripciones();
        notificadorMensajes.firePropertyChange("mensaje", null, "Inscripción eliminada correctamente.");
    }

    public void cargarInscripciones() {
        modelo.cargarDatosDB();
        List<Object[]> datos = modelo.getListado().stream()
                .map(inscripcion -> new Object[]{
                        inscripcion.getID(),
                        inscripcion.getEstudiante().getNombreCompleto(),
                        inscripcion.getSemestre(),
                        inscripcion.getCurso().getNombre(),
                        inscripcion.getAnio()
                }).toList();

        notificadorMensajes.firePropertyChange("datosInscripciones", null, datos);
    }

    public void agregarListener(PropertyChangeListener listener) {
        notificadorMensajes.addPropertyChangeListener(listener);
    }

    public void cargarCursos() {
        CursoDAO cursoDAO = new CursoDAO();
        List<Curso> cursos = cursoDAO.obtenerTodosLosCursos();
        notificadorMensajes.firePropertyChange("cursosCargados", null, cursos);
    }

    public void cargarEstudiantes() {
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        List<Estudiante> estudiantes = estudianteDAO.obtenerTodosLosEstudiantes();
        notificadorMensajes.firePropertyChange("estudiantesCargados", null, estudiantes);
    }
}