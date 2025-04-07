package controlador;

import fabricas.ServicioFactory;
import modelo.entidades.Estudiante;
import modelo.institucion.Curso;
import modelo.institucion.Inscripcion;
import servicios.InscripcionesCursosEstudiantes;
import servicios.InscripcionesCursos;
import servicios.InscripcionesEstudiantes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

public class ControladorCursosEstudiantes {
    private final InscripcionesCursosEstudiantes servicioInscripcionesCursosEstudiantes;
    private final InscripcionesEstudiantes servicioEstudiantes;
    private final InscripcionesCursos servicioCursos;
    private final PropertyChangeSupport notificadorMensajes;

    public ControladorCursosEstudiantes() {
        servicioInscripcionesCursosEstudiantes = ServicioFactory.crearInscripcionesCursosEstudiantes();
        servicioEstudiantes = ServicioFactory.crearInscripcionesEstudiantes();
        servicioCursos = ServicioFactory.crearInscripcionesCursos();

        notificadorMensajes = new PropertyChangeSupport(this);
    }

    private Inscripcion construirObjetoInscripcion(Map<String, Object> datosFormulario) {
        Estudiante estudiante = (Estudiante) datosFormulario.get("estudiante");
        Curso curso = (Curso) datosFormulario.get("curso");
        int anio = Integer.parseInt((String) datosFormulario.get("anio"));
        int periodo = Integer.parseInt(datosFormulario.get("periodo").toString());

        return new Inscripcion(curso, anio, periodo, estudiante);
    }

    public void inscribirEstudianteACurso(Map<String, Object> datosFormulario) {
        try {
            if(datosFormulario == null || datosFormulario.isEmpty()) {
                throw new IllegalArgumentException("Faltan datos en el formulario, complete todos los campos.");
            }

            Inscripcion inscripcion = construirObjetoInscripcion(datosFormulario);
            servicioInscripcionesCursosEstudiantes.inscribir(inscripcion);
            notificadorMensajes.firePropertyChange("mensaje", null, "Estudiante inscrito exitosamente!");
        } catch (IllegalArgumentException exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, exception.getMessage());
        }
    }

    public List<Inscripcion> cargarInscripciones() {
         return servicioInscripcionesCursosEstudiantes.cargarDatosH2();
    }

    public Estudiante buscarEstudiante(double codigoEstudiante) {
        Estudiante estudianteEncontrado = servicioEstudiantes.buscarEstudiantePorCodigo(codigoEstudiante);

        if (estudianteEncontrado != null) {
            servicioEstudiantes.getEstudiantes().removeIf(est -> est.getCodigo() == codigoEstudiante);
            servicioEstudiantes.getEstudiantes().add(estudianteEncontrado);
        }

        return estudianteEncontrado;
    }

    public List<Inscripcion> obtenerInscripcionesPorEstudiante(Estudiante estudiante) {
        return servicioInscripcionesCursosEstudiantes.obtenerInscripcionesPorEstudiante(estudiante);
    }

    public Curso buscarCurso(double codigoCurso) {
        Curso cursoEncontrado = servicioCursos.buscarCursoPorCodigo(codigoCurso);

        if (cursoEncontrado != null) {
            servicioCursos.getCursos().removeIf(c -> c.getCodigo() == codigoCurso);
            servicioCursos.getCursos().add(cursoEncontrado);
        }

        return cursoEncontrado;
    }

    public void agregarListener(PropertyChangeListener listener) {
        notificadorMensajes.addPropertyChangeListener(listener);
    }
}