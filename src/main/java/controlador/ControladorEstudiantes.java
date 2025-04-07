package controlador;

import DAO.ProgramaDAO;
import fabricas.ServicioFactory;
import modelo.entidades.Estudiante;
import modelo.institucion.Programa;
import servicios.InscripcionesEstudiantes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

public class ControladorEstudiantes {
    private final InscripcionesEstudiantes servicio;
    private final PropertyChangeSupport notificadorMensajes;

    public ControladorEstudiantes() {
        servicio = ServicioFactory.crearInscripcionesEstudiantes();
        notificadorMensajes = new PropertyChangeSupport(this);
    }

    private Estudiante construirObjetoEstudiante(Map<String, Object> datosFormulario) {
        String nombres = (String) datosFormulario.get("nombres");
        String apellidos = (String) datosFormulario.get("apellidos");
        String email = (String) datosFormulario.get("email");
        double codigo = Double.parseDouble((String) datosFormulario.get("codigo"));
        boolean activo = (Boolean) datosFormulario.get("activo");
        double promedio = Double.parseDouble((String) datosFormulario.get("promedio"));
        Programa programa = (Programa) datosFormulario.get("programa");

        return new Estudiante(nombres, apellidos, email, codigo, activo, promedio, programa);
    }

    public void guardarEstudiante(Map<String, Object> datosFormulario) {
        try {
            if(datosFormulario == null || datosFormulario.isEmpty()) {
                throw new IllegalArgumentException("Faltan datos en el formulario, complete todos los campos.");
            }

            Estudiante estudiante = construirObjetoEstudiante(datosFormulario);
            servicio.inscribirEstudiante(estudiante);
            notificadorMensajes.firePropertyChange("mensaje", null, "Estudiante guardado exitosamente!");
        } catch (IllegalArgumentException exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, exception.getMessage());
        }
    }

    public void eliminarEstudiante(double codigoEstudiante) {
        if(codigoEstudiante <= 0) {
            notificadorMensajes.firePropertyChange("mensaje", null, "Código Inválido");
            return;
        }

        servicio.getEstudiantes().stream().
                filter(estudiante -> estudiante.getCodigo() == codigoEstudiante)
                .findFirst().ifPresentOrElse(
                        estudiante -> {
                        servicio.eliminarEstudiante(estudiante);
                        notificadorMensajes.firePropertyChange("mensaje", null, "Estudiante eliminado correctamente.");
                        },
                        () -> notificadorMensajes.firePropertyChange("mensaje", null, "No se encontró el estudiante.")
                );
    }

    public Estudiante buscarEstudiantePorCodigo(double codigoEstudiante) {
        Estudiante estudianteEncontrado = servicio.buscarEstudiantePorCodigo(codigoEstudiante);

        if (estudianteEncontrado != null) {
            servicio.getEstudiantes().removeIf(est -> est.getCodigo() == codigoEstudiante);
            servicio.getEstudiantes().add(estudianteEncontrado);
        }

        return estudianteEncontrado;
    }

    public List<Estudiante> cargarEstudiantes() {
        return servicio.cargarDatosH2();
    }

    public void cargarProgramas() {
        ProgramaDAO programaDAO = new ProgramaDAO();
        List<Programa> programas = programaDAO.obtenerTodosLosProgramas();
        notificadorMensajes.firePropertyChange("programasCargados", null, programas);
    }

    public void agregarListener(PropertyChangeListener listener) {
        notificadorMensajes.addPropertyChangeListener(listener);
    }
}