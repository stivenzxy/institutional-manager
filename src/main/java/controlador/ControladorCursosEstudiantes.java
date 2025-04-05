package controlador;

import modelo.entidades.Estudiante;
import modelo.institucion.Curso;
import modelo.institucion.Inscripcion;
import modelo.relaciones.CursosInscritos;
import servicios.GestionCursos;
import servicios.InscripcionesEstudiantes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

public class ControladorCursosEstudiantes {
    private final CursosInscritos modeloCursosInscritos;
    private final InscripcionesEstudiantes modeloEstudiantes;
    private final GestionCursos modeloCursos;
    private final PropertyChangeSupport notificadorMensajes;

    public ControladorCursosEstudiantes() {
        modeloCursosInscritos = new CursosInscritos();
        modeloEstudiantes = new InscripcionesEstudiantes();
        modeloCursos = new GestionCursos();

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
            modeloCursosInscritos.inscribir(inscripcion);
            notificadorMensajes.firePropertyChange("mensaje", null, "Estudiante inscrito exitosamente!");
            cargarInscripciones();
        } catch (IllegalArgumentException exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, exception.getMessage());
        }
    }

    public void cargarInscripciones() {
        modeloEstudiantes.cargarDatosH2();
    }

    public Estudiante buscarEstudiante(double codigoEstudiante) {
        Estudiante estudianteEncontrado = modeloEstudiantes.buscarEstudiantePorCodigo(codigoEstudiante);

        if (estudianteEncontrado != null) {
            modeloEstudiantes.getEstudiantes().removeIf(est -> est.getCodigo() == codigoEstudiante);
            modeloEstudiantes.getEstudiantes().add(estudianteEncontrado);
        }

        return estudianteEncontrado;
    }

    public Curso buscarCurso(double codigoCurso) {
        Curso cursoEncontrado = modeloCursos.buscarCursoPorCodigo(codigoCurso);

        if (cursoEncontrado != null) {
            modeloCursos.getCursos().removeIf(c -> c.getCodigo() == codigoCurso);
            modeloCursos.getCursos().add(cursoEncontrado);
        }

        return cursoEncontrado;
    }

    public void agregarListener(PropertyChangeListener listener) {
        notificadorMensajes.addPropertyChangeListener(listener);
    }
}