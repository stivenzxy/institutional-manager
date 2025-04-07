package controlador;

import DAO.ProgramaDAO;
import fabricas.ServicioFactory;
import modelo.institucion.Curso;
import modelo.institucion.Programa;
import servicios.InscripcionesCursos;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

public class ControladorCursos {
    private final InscripcionesCursos servicio;
    private final PropertyChangeSupport notificadorMensajes;

    public ControladorCursos() {
        servicio = ServicioFactory.crearInscripcionesCursos();
        notificadorMensajes = new PropertyChangeSupport(this);
    }

    private Curso construirObjetoCurso(Map<String, Object> datosFormulario){
        String nombre = (String) datosFormulario.get("nombre");
        double codigo = Double.parseDouble((String) datosFormulario.get("codigo"));
        Programa programa = (Programa) datosFormulario.get("programa");
        boolean activo = (Boolean) datosFormulario.get("activo");

        return new Curso(codigo, nombre, programa, activo);
    }

    public void guardarCurso(Map<String, Object> datosFormulario) {
        try {
            if (datosFormulario == null ||   datosFormulario.isEmpty()) {
                throw new IllegalArgumentException("Faltan datos en el formulario");
            }

            Curso curso = construirObjetoCurso(datosFormulario);
            servicio.guardarCurso(curso);
            notificadorMensajes.firePropertyChange("mensaje", null, "Curso guardado exitosamente!");
        } catch (IllegalArgumentException exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, exception.getMessage());
        }
    }

    public void eliminarCurso(double codigoCurso) {
        if (codigoCurso <= 0) {
            notificadorMensajes.firePropertyChange("mensaje", null, "Código Inválido");
            return;
        }

        servicio.getCursos().stream()
                .filter(curso -> curso.getCodigo() == codigoCurso)
                .findFirst()
                .ifPresentOrElse(
                        curso -> {
                            String mensaje = servicio.eliminarCurso(curso);
                            notificadorMensajes.firePropertyChange("mensaje", null, mensaje);
                        },
                        () -> notificadorMensajes.firePropertyChange("mensaje", null, "No se encontró el curso.")
                );
    }


    public Curso buscarCursoPorCodigo(double codigoCurso) {
        Curso cursoEncontrado = servicio.buscarCursoPorCodigo(codigoCurso);

        if (cursoEncontrado != null) {
            servicio.getCursos().removeIf(c -> c.getCodigo() == codigoCurso);
            servicio.getCursos().add(cursoEncontrado);
        }

        return cursoEncontrado;
    }

    public List<Curso> cargarCursos() { return servicio.cargarDatosH2(); }

    public void cargarProgramas() {
        ProgramaDAO programaDAO = new ProgramaDAO();
        List<Programa> programas = programaDAO.obtenerTodosLosProgramas();
        notificadorMensajes.firePropertyChange("programasCargados", null, programas);
    }

    public void agregarListener(PropertyChangeListener listener) {
        notificadorMensajes.addPropertyChangeListener(listener);
    }
}