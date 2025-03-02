package controlador;

import modelo.entidades.Persona;
import modelo.relaciones.InscripcionesPersonas;
import vista.GestionPersonasGUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ControladorPersonas {
    private final GestionPersonasGUI vista;
    private final InscripcionesPersonas modelo;

    public ControladorPersonas(GestionPersonasGUI vista, InscripcionesPersonas modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.getBtnGuardar().addActionListener(e -> guardarPersona());
        this.vista.getBtnActualizar().addActionListener(e -> actualizarPersona());
        this.vista.getBtnEliminar().addActionListener(e -> eliminarPersona());
        this.vista.getBtnCargar().addActionListener(e -> cargarPersonas());
        this.vista.getTablaPersonas().getSelectionModel().addListSelectionListener(e -> seleccionarPersona());
    }

    private Persona obtenerDatosPersona() throws NumberFormatException {
        double id = Double.parseDouble(vista.getTxtID().getText());
        String nombres = vista.getTxtNombres().getText();
        String apellidos = vista.getTxtApellidos().getText();
        String email = vista.getTxtEmail().getText();

        if (nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos.");
            return null;
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
                    .filter(p -> p.getID() == id)
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

        modelo.cargarDatos();

        for (Persona p : modelo.getPersonas()) {
            modeloTabla.addRow(new Object[]{p.getID(), p.getNombres(), p.getApellidos(), p.getEmail()});
        }

        modeloTabla.fireTableDataChanged(); // Actualiza todas las celdas de la tabla (evento)
    }

    private void seleccionarPersona() {
        int filaSeleccionada = vista.getTablaPersonas().getSelectedRow();
        if (filaSeleccionada != -1) {
            vista.getTxtID().setText(vista.getModeloTabla().getValueAt(filaSeleccionada, 0).toString());
            vista.getTxtNombres().setText(vista.getModeloTabla().getValueAt(filaSeleccionada, 1).toString());
            vista.getTxtApellidos().setText(vista.getModeloTabla().getValueAt(filaSeleccionada, 2).toString());
            vista.getTxtEmail().setText(vista.getModeloTabla().getValueAt(filaSeleccionada, 3).toString());
        }
    }
}