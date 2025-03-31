package controlador;

import DAO.ProgramaDAO;
import modelo.entidades.Estudiante;
import modelo.institucion.Programa;
import servicios.InscripcionesEstudiantes;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

public class ControladorEstudiantes {
    private final InscripcionesEstudiantes modelo;
    private final PropertyChangeSupport notificadorMensajes;

    public ControladorEstudiantes() {
        modelo = new InscripcionesEstudiantes();
        notificadorMensajes = new PropertyChangeSupport(this);
    }

    private Estudiante construirObjetoEstudiante(Map<String, Object> datosForm) {
        String nombres = (String) datosForm.get("nombre");
        String apellidos = (String) datosForm.get("apellidos");
        String email = (String) datosForm.get("email");
        double codigo = Double.parseDouble((String) datosForm.get("codigo"));
        boolean activo = (Boolean) datosForm.get("activo");
        double promedio = Double.parseDouble((String) datosForm.get("promedio"));
        Programa programa = (Programa) datosForm.get("programa");

        return new Estudiante(nombres, apellidos, email, codigo, activo, promedio, programa);
    }

    public void guardarEstudiante(Map<String, Object> datosForm) {
        try {
            if(datosForm == null || datosForm.isEmpty()) {
                throw new IllegalArgumentException("Faltan datos en el formulario, complete todos los campos.");
            }

            Estudiante estudiante = construirObjetoEstudiante(datosForm);
            modelo.inscribirEstudiante(estudiante);
            notificadorMensajes.firePropertyChange("mensaje", null, "Estudiante guardado exitosamente!");
        } catch (IllegalArgumentException excepcion) {
            notificadorMensajes.firePropertyChange("mensaje", null, excepcion.getMessage());
        }
    }

    public void eliminarEstudiante(double codigoEstudiante) {
        if(codigoEstudiante <= 0) {
            notificadorMensajes.firePropertyChange("mensaje", null, "Código Invalido: El código debe ser mayor que 0");
            return;
        }

        modelo.getEstudiantes().stream().
                filter(estudiante -> estudiante.getCodigo() == codigoEstudiante)
                .findFirst().ifPresentOrElse(
                        estudiante -> {
                        modelo.eliminarEstudiante(estudiante);
                        notificadorMensajes.firePropertyChange("mensaje", null, "Estudiante eliminado correctamente.");
                        },
                        () -> notificadorMensajes.firePropertyChange("mensaje", null, "No se encontró el estudiante.")
                );
    }

    public Estudiante buscarEstudiantePorCodigo(double codigoEstudiante) {
        Estudiante estudianteEncontrado = modelo.buscarEstudiantePorCodigo(codigoEstudiante);

        if (estudianteEncontrado != null) {
            modelo.getEstudiantes().removeIf(est -> est.getCodigo() == codigoEstudiante);
            modelo.getEstudiantes().add(estudianteEncontrado);
        }

        return estudianteEncontrado;
    }


    public void cargarEstudiantes() {
        modelo.cargarDatosH2();
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