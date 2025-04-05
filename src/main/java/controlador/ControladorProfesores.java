package controlador;

import modelo.entidades.Estudiante;
import modelo.entidades.Profesor;
import servicios.InscripcionesProfesores;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

public class ControladorProfesores {
    private final InscripcionesProfesores modelo;
    private final PropertyChangeSupport notificadorMensajes;

    public ControladorProfesores() {
        modelo = new InscripcionesProfesores();
        notificadorMensajes = new PropertyChangeSupport(this);
    }

    private Profesor construirObjetoProfesor(Map<String, Object> datosFormulario) {
        String nombres = (String) datosFormulario.get("nombres");
        String apellidos = (String) datosFormulario.get("apellidos");
        String email = (String) datosFormulario.get("email");
        String tipoContrato = (String) datosFormulario.get("tipoContrato");

        return new Profesor(nombres, apellidos, email, tipoContrato);
    }

    public void guardarProfesor(Map<String, Object> datosFormulario) {
        try {
            if (datosFormulario == null || datosFormulario.isEmpty()) {
                throw new IllegalArgumentException("Faltan datos en el formulario");
            }

            Profesor profesor = construirObjetoProfesor(datosFormulario);
            modelo.inscribirProfesor(profesor);
            notificadorMensajes.firePropertyChange("mensaje", null, "Profesor guardado exitosamente!");
        } catch (IllegalArgumentException exception) {
            notificadorMensajes.firePropertyChange("mensaje", null, exception.getMessage());
        }
    }

    public void eliminarProfesor(Map<String, Object> datosFormulario) {
        String email = (String) datosFormulario.get("email");
        double idProfesor = modelo.obtenerIdPorEmail(email);

        if(idProfesor <= 0 ) {
            notificadorMensajes.firePropertyChange("mensaje", null, "Código Inválido");
            return;
        }

        modelo.getProfesores().stream().filter(profesor -> profesor.getID() == idProfesor).findFirst().ifPresentOrElse(
                profesor -> {
                    modelo.eliminarProfesor(profesor);
                    notificadorMensajes.firePropertyChange("mensaje", null, "Profesor eliminado exitosamente!");
                },
                () -> notificadorMensajes.firePropertyChange("mensaje", null, "No se encontró el profesor.")
        );
    }

    public Profesor buscarProfesorPorId(double idProfesor) {
        Profesor profesorEncontrado = modelo.buscarProfesorPorId(idProfesor);

        if (profesorEncontrado != null) {
            modelo.getProfesores().removeIf(prof -> prof.getID() == profesorEncontrado.getID());
            modelo.getProfesores().add(profesorEncontrado);
        }

        return profesorEncontrado;
    }

    public List<Profesor> cargarProfesores() {
        return modelo.cargarDatosH2();
    }

    public void agregarListener(PropertyChangeListener listener) {
        notificadorMensajes.addPropertyChangeListener(listener);
    }
}