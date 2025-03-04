package controlador;

import modelo.entidades.Estudiante;
import modelo.entidades.Persona;
import modelo.entidades.Profesor;
import modelo.institucion.Programa;
import modelo.relaciones.InscripcionesPersonas;
import vista.GestionPersonasGUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Objects;
import java.text.DecimalFormat;

public class ControladorPersonas {
    private final GestionPersonasGUI vista;
    private final InscripcionesPersonas modelo;
    DecimalFormat df = new DecimalFormat("#");

    public ControladorPersonas(GestionPersonasGUI vista, InscripcionesPersonas modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.getBtnGuardar().addActionListener(e -> guardarPersona());
        this.vista.getBtnActualizar().addActionListener(e -> actualizarPersona());
        this.vista.getBtnEliminar().addActionListener(e -> eliminarPersona());
        this.vista.getBtnCargar().addActionListener(e -> cargarPersonas());
        this.vista.getTablaPersonas().getSelectionModel().addListSelectionListener(e -> seleccionarPersona());

        this.actualizarCampos();
        this.cargarPersonas();
        this.vista.getCmbTipoPersona().addActionListener(e -> actualizarCampos());
    }

    private Persona obtenerDatosPersona() throws NumberFormatException {
        double id = Double.parseDouble(vista.getTxtID().getText());
        String nombres = vista.getTxtNombres().getText();
        String apellidos = vista.getTxtApellidos().getText();
        String email = vista.getTxtEmail().getText();
        String tipo = (String) vista.getCmbTipoPersona().getSelectedItem();

        if (nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos.");
            return null;
        }

        if ("Estudiante".equals(tipo)) {
            int codigo = Integer.parseInt(vista.getTxtCodigo().getText());
            double promedio = Double.parseDouble(vista.getTxtPromedio().getText());
            boolean activo = vista.getCheckActivo().isSelected();
            Programa programaSeleccionado = (Programa) vista.getCmbPrograma().getSelectedItem();

            return new Estudiante(id, nombres, apellidos, email, codigo, activo, promedio, programaSeleccionado);
        } else if ("Profesor".equals(tipo)) {
            String tipoContrato = vista.getTxtTipoContrato().getText();
            return new Profesor(id, nombres, apellidos, email, tipoContrato);
        }
        return new Persona(id, nombres, apellidos, email);
    }

    private void guardarPersona() {
        try {
            Persona persona = obtenerDatosPersona();
            if (persona == null) return;

            modelo.inscribir(persona);
            JOptionPane.showMessageDialog(vista, "Persona guardada exitosamente.");
            cargarPersonas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido.");
        }
    }

    private void actualizarPersona() {
        try {
            Persona persona = obtenerDatosPersona();
            if (persona == null) return;

            modelo.actualizar(persona);
            JOptionPane.showMessageDialog(vista, "Persona actualizada correctamente.");
            cargarPersonas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido.");
        }
    }

    private void eliminarPersona() {
        try {
            double id = Double.parseDouble(vista.getTxtID().getText());

            Persona personaAEliminar = modelo.getPersonas().stream()
                    .filter(p -> Double.compare(p.getID(), id) == 0)
                    .findFirst()
                    .orElse(null);

            if (personaAEliminar == null) {
                JOptionPane.showMessageDialog(vista, "No se encontró la persona.");
                return;
            }

            modelo.eliminar(personaAEliminar);
            JOptionPane.showMessageDialog(vista, "Persona eliminada correctamente.");
            cargarPersonas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido.");
        }
    }

    private void cargarPersonas() {
        DefaultTableModel modeloTabla = vista.getModeloTabla();
        modeloTabla.setRowCount(0);

        modelo.cargarDatosDB();

        for (Persona p : modelo.getPersonas()) {
            if (p instanceof Estudiante est) {
                modeloTabla.addRow(new Object[]{est.getID(), est.getNombres(), est.getApellidos(), est.getEmail(), "Estudiante",  df.format(est.getCodigo()), (est.isActivo() ? "Si" : "No"), est.getPromedio(), est.getPrograma() != null ? est.getPrograma().getNombre() : "", ""});
            } else if (p instanceof Profesor prof) {
                modeloTabla.addRow(new Object[]{prof.getID(), prof.getNombres(), prof.getApellidos(), prof.getEmail(), "Profesor", "", "", "", "", prof.getTipoContrato()});
            } else {
                modeloTabla.addRow(new Object[]{p.getID(), p.getNombres(), p.getApellidos(), p.getEmail(), "Persona", "", "", "", "", ""});
            }
        }
        modeloTabla.fireTableDataChanged();
    }

    private void seleccionarPersona() {
        int filaSeleccionada = vista.getTablaPersonas().getSelectedRow();

        if (filaSeleccionada != -1) {
            DefaultTableModel modeloTabla = vista.getModeloTabla();

            vista.getTxtID().setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            vista.getTxtNombres().setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            vista.getTxtApellidos().setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            vista.getTxtEmail().setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());

            String tipo = modeloTabla.getValueAt(filaSeleccionada, 4).toString();
            vista.getCmbTipoPersona().setSelectedItem(tipo);

            if ("Estudiante".equals(tipo)) {
                vista.getTxtCodigo().setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
                vista.getCheckActivo().setSelected("Si".equals(modeloTabla.getValueAt(filaSeleccionada, 6).toString()));
                vista.getTxtPromedio().setText(modeloTabla.getValueAt(filaSeleccionada, 7).toString());

                String nombrePrograma = modeloTabla.getValueAt(filaSeleccionada, 8).toString();
                for (int i = 0; i < vista.getCmbPrograma().getItemCount(); i++) {
                    Programa programa = vista.getCmbPrograma().getItemAt(i);
                    if (programa.getNombre().equals(nombrePrograma)) {
                        vista.getCmbPrograma().setSelectedItem(programa);
                        break;
                    }
                }
            } else if ("Profesor".equals(tipo)) {
                vista.getTxtTipoContrato().setText(modeloTabla.getValueAt(filaSeleccionada, 9).toString());
            }
            actualizarCampos();
        }
    }

    private void actualizarCampos() {
        boolean esEstudiante = Objects.equals(vista.getCmbTipoPersona().getSelectedItem(), "Estudiante");
        boolean esProfesor = vista.getCmbTipoPersona().getSelectedItem().equals("Profesor");
        boolean esPersona = vista.getCmbTipoPersona().getSelectedItem().equals("Persona");

        vista.getTxtCodigo().setEnabled(esEstudiante && (!esPersona || !esProfesor));
        vista.getCheckActivo().setEnabled(esEstudiante && (!esPersona || !esProfesor));
        vista.getTxtPromedio().setEnabled(esEstudiante && (!esPersona || !esProfesor));
        vista.getCmbPrograma().setEnabled(esEstudiante && (!esPersona || !esProfesor));
        vista.getTxtTipoContrato().setEnabled(esProfesor && (!esEstudiante || !esPersona));
    }
}