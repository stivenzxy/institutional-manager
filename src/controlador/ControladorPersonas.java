package controlador;

import DAO.ProgramaDAO;
import modelo.entidades.Estudiante;
import modelo.entidades.Persona;
import modelo.entidades.Profesor;
import modelo.institucion.Programa;
import modelo.relaciones.InscripcionesPersonas;
import vista.GestionPersonasGUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Objects;
import java.text.DecimalFormat;
import java.util.stream.IntStream;

public class ControladorPersonas {
    private final GestionPersonasGUI vista;
    private final InscripcionesPersonas modelo;
    DecimalFormat df = new DecimalFormat("#");

    public ControladorPersonas(GestionPersonasGUI vista, InscripcionesPersonas modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.inicializarEventos();
        this.actualizarCampos();
        this.cargarPersonas();
        this.cargarProgramasEnComboBox();
    }

    private void inicializarEventos() {
        this.vista.getBtnGuardar().addActionListener(e -> guardarPersona());
        this.vista.getBtnActualizar().addActionListener(e -> actualizarPersona());
        this.vista.getBtnEliminar().addActionListener(e -> eliminarPersona());
        this.vista.getBtnCargar().addActionListener(e -> cargarPersonas());
        this.vista.getTablaPersonas().getSelectionModel().addListSelectionListener(e -> seleccionarPersona());
        this.vista.getCmbTipoPersona().addActionListener(e -> actualizarCampos());
    }

    private Persona obtenerDatosPersona() throws NumberFormatException {
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

            return new Estudiante(nombres, apellidos, email, codigo, activo, promedio, programaSeleccionado);
        } else if ("Profesor".equals(tipo)) {
            String tipoContrato = vista.getTxtTipoContrato().getText();
            return new Profesor(nombres, apellidos, email, tipoContrato);
        }
        return new Persona(nombres, apellidos, email);
    }

    private void guardarPersona() {
        try {
            Persona persona = obtenerDatosPersona();
            if (persona == null) return;

            modelo.inscribir(persona);
            JOptionPane.showMessageDialog(vista, "Persona guardada exitosamente.");
            cargarPersonas();
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido.");
        }
    }

    private void actualizarPersona() {
        int filaSeleccionada = vista.getTablaPersonas().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una persona para actualizar.");
            return;
        }

        try {
            Persona persona = obtenerDatosPersona();
            if (persona == null) return;

            double id = Double.parseDouble(vista.getModeloTabla().getValueAt(filaSeleccionada, 0).toString());
            persona.setID(id);

            modelo.actualizar(persona);
            JOptionPane.showMessageDialog(vista, "Persona actualizada correctamente.");
            cargarPersonas();
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(vista, "Error al procesar los datos.");
        }
    }


    private void eliminarPersona() {
        int filaSeleccionada = vista.getTablaPersonas().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una persona para eliminar.");
            return;
        }

        try {
            double id = Double.parseDouble(vista.getModeloTabla().getValueAt(filaSeleccionada, 0).toString());

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
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(vista, "Error al procesar los datos.");
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
        int fila = vista.getTablaPersonas().getSelectedRow();
        if (fila == -1) return;

        DefaultTableModel modelo = vista.getModeloTabla();
        limpiarCampos();

        JTextField[] campos = { vista.getTxtNombres(), vista.getTxtApellidos(), vista.getTxtEmail() };

        IntStream.range(0, campos.length)
                .forEach(i -> campos[i].setText(modelo.getValueAt(fila, i + 1).toString()));

        String tipo = modelo.getValueAt(fila, 4).toString();
        vista.getCmbTipoPersona().setSelectedItem(tipo);

        if ("Estudiante".equals(tipo)) {
            vista.getTxtCodigo().setText(modelo.getValueAt(fila, 5).toString());
            vista.getCheckActivo().setSelected("Si".equals(modelo.getValueAt(fila, 6).toString()));
            vista.getTxtPromedio().setText(modelo.getValueAt(fila, 7).toString());
            vista.getCmbPrograma().setSelectedItem(
                    IntStream.range(0, vista.getCmbPrograma().getItemCount())
                            .mapToObj(vista.getCmbPrograma()::getItemAt)
                            .filter(p -> p.getNombre().equals(modelo.getValueAt(fila, 8).toString()))
                            .findFirst().orElse(null)
            );
        } else if ("Profesor".equals(tipo)) {
            vista.getTxtTipoContrato().setText(modelo.getValueAt(fila, 9).toString());
        }
        actualizarCampos();
    }

    private void limpiarCampos() {
        vista.getTxtCodigo().setText("");
        vista.getCheckActivo().setSelected(false);
        vista.getTxtPromedio().setText("");
        vista.getCmbPrograma().setSelectedItem(null);
        vista.getTxtTipoContrato().setText("");
    }

    private void actualizarCampos() {
        String tipo = Objects.toString(vista.getCmbTipoPersona().getSelectedItem(), "");
        boolean esEstudiante = "Estudiante".equals(tipo);
        boolean esProfesor = "Profesor".equals(tipo);

        vista.getTxtCodigo().setEnabled(esEstudiante);
        vista.getCheckActivo().setEnabled(esEstudiante);
        vista.getTxtPromedio().setEnabled(esEstudiante);
        vista.getCmbPrograma().setEnabled(esEstudiante);
        vista.getTxtTipoContrato().setEnabled(esProfesor);
    }

    private void cargarProgramasEnComboBox() {
        ProgramaDAO programaDAO = new ProgramaDAO();
        List<Programa> programas = programaDAO.obtenerTodosLosProgramas();
        vista.getCmbPrograma().removeAllItems();
        for (Programa programa : programas) {
            vista.getCmbPrograma().addItem(programa);
        }
    }
}