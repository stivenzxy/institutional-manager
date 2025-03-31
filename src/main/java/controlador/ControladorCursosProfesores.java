package controlador;

import DAO.CursoDAO;
import DAO.ProfesorDAO;
import modelo.entidades.Profesor;
import modelo.institucion.Curso;
import modelo.relaciones.CursoProfesor;
import modelo.relaciones.CursosProfesores;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

public class ControladorCursosProfesores {
    private final CursosProfesores modelo;
    private final PropertyChangeSupport notificadorMensajes;

    public ControladorCursosProfesores() {
        modelo = new CursosProfesores();
        notificadorMensajes = new PropertyChangeSupport(this);
    }

    private CursoProfesor convertirDatosACursoProfesor(Map<String, Object> datos) {
        try {
            Profesor profesor = (Profesor) datos.get("profesor");
            Curso curso = (Curso) datos.get("curso");
            int anio = Integer.parseInt((String) datos.get("anio"));
            int semestre = Integer.parseInt((String) datos.get("semestre"));

            if (profesor == null || curso == null) {
                throw new IllegalArgumentException("Debe seleccionar un profesor y un curso.");
            }

            return new CursoProfesor(profesor, anio, semestre, curso);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("El año y el semestre deben ser números válidos.");
        }
    }

    public void inscribirCursoProfesor(Map<String, Object> datos) {
        try {
            CursoProfesor cursoProfesor = convertirDatosACursoProfesor(datos);
            modelo.inscribir(cursoProfesor);
            cargarCursoProfesores();
            notificadorMensajes.firePropertyChange("mensaje", null, "Profesor inscrito exitosamente!");
        } catch (Exception exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, exception.getMessage());
        }
    }

    public void eliminarAsignacion(String profesorNombre, String cursoNombre) {
        CursoProfesor asignacionAEliminar = modelo.getListado().stream()
                .filter(cp -> cp.getProfesor().getNombres().equals(profesorNombre) &&
                        cp.getCurso().getNombre().equals(cursoNombre))
                .findFirst()
                .orElse(null);

        if (asignacionAEliminar == null) {
            notificadorMensajes.firePropertyChange("mensaje", null, "No se encontró la asignación.");
            return;
        }

        modelo.eliminar(asignacionAEliminar);
        cargarCursoProfesores();
        notificadorMensajes.firePropertyChange("mensaje", null, "Asignación eliminada correctamente.");
    }

    public void cargarCursoProfesores() {
        modelo.cargarDatosH2();
        List<Object[]> datos = modelo.getListado().stream()
                .map(asignacion -> new Object[]{
                        asignacion.getProfesor().getNombreCompleto(),
                        asignacion.getCurso().getNombre(),
                        asignacion.getAnio(),
                        asignacion.getSemestre()
                }).toList();

        notificadorMensajes.firePropertyChange("datosCursoProfesores", null, datos);
    }

    public void agregarListener(PropertyChangeListener listener) {
        notificadorMensajes.addPropertyChangeListener(listener);
    }

    public void cargarProfesores() {
        ProfesorDAO profesorDAO = new ProfesorDAO();
        List<Profesor> profesores = profesorDAO.obtenerTodosLosProfesores();
        notificadorMensajes.firePropertyChange("profesoresCargados", null, profesores);
    }

    public void cargarCursos() {
        CursoDAO cursoDAO = new CursoDAO();
        List<Curso> cursos = cursoDAO.obtenerTodosLosCursos();
        notificadorMensajes.firePropertyChange("cursosCargados", null, cursos);
    }
}