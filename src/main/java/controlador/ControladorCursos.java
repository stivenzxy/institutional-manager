package controlador;

import DAO.ProgramaDAO;
import modelo.institucion.Curso;
import modelo.institucion.Programa;
import servicios.GestionCursos;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

public class ControladorCursos {
    private final GestionCursos modelo;
    private final PropertyChangeSupport notificadorMensajes;

    public ControladorCursos() {
        modelo = new GestionCursos();
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
            modelo.guardarCurso(curso);
            notificadorMensajes.firePropertyChange("mensaje", null, "Curso guardado exitosamente!");
        } catch (IllegalArgumentException exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, exception.getMessage());
        }
    }

    public void elimnarCurso(double codigoCurso) {
        if(codigoCurso <= 0) {
            notificadorMensajes.firePropertyChange("mensaje", null, "Código Inválido");
            return;
        }

        modelo.getCursos().stream().
                filter(curso -> curso.getCodigo() == codigoCurso)
                .findFirst().ifPresentOrElse(
                        curso -> {
                            modelo.eliminarCurso(curso);
                            notificadorMensajes.firePropertyChange("mensaje", null, "Curso eliminado correctamente.");
                        },
                        () -> notificadorMensajes.firePropertyChange("mensaje", null, "No se encontró el curso.")
                );
    }

    public Curso buscarCursoPorCodigo(double codigoCurso) {
        Curso cursoEncontrado = modelo.buscarCursoPorCodigo(codigoCurso);

        if (cursoEncontrado != null) {
            modelo.getCursos().removeIf(c -> c.getCodigo() == codigoCurso);
            modelo.getCursos().add(cursoEncontrado);
        }

        return cursoEncontrado;
    }

    public List<Curso> cargarCursos() { return modelo.cargarDatosH2(); }

    public void cargarProgramas() {
        ProgramaDAO programaDAO = new ProgramaDAO();
        List<Programa> programas = programaDAO.obtenerTodosLosProgramas();
        notificadorMensajes.firePropertyChange("programasCargados", null, programas);
    }

    public void agregarListener(PropertyChangeListener listener) {
        notificadorMensajes.addPropertyChangeListener(listener);
    }
}