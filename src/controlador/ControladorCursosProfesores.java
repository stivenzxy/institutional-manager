package controlador;

import modelo.entidades.Profesor;
import modelo.institucion.Curso;
import modelo.relaciones.CursoProfesor;
import modelo.relaciones.CursosProfesores;
import vista.GestionCursosProfesoresGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ControladorCursosProfesores {
    private final GestionCursosProfesoresGUI vista;
    private final CursosProfesores modelo;

    public ControladorCursosProfesores(GestionCursosProfesoresGUI vista, CursosProfesores modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.getBtnAsignar().addActionListener(e -> inscribirCursoProfesor());
        this.vista.getBtnCargar().addActionListener(e -> cargarCursoProfesores());
        this.vista.getBtnEliminar().addActionListener(e -> eliminarAsignacion());
        this.vista.getTablaAsignaciones().getSelectionModel().addListSelectionListener(e -> seleccionarAsignacion());

        cargarCursoProfesores();
    }

    private void inscribirCursoProfesor() {
        try {
            Profesor profesorSeleccionado = (Profesor) vista.getCmbProfesores().getSelectedItem();
            Curso cursoSeleccionado = (Curso) vista.getCmbCursos().getSelectedItem();
            String anioTexto = vista.getTxtAnio().getText();
            String semestreTexto = vista.getTxtSemestre().getText();

            if (profesorSeleccionado == null || cursoSeleccionado == null) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un profesor y un curso.");
                return;
            }

            int anio = Integer.parseInt(anioTexto);
            int semestre = Integer.parseInt(semestreTexto);

            CursoProfesor cursoProfesor = new CursoProfesor(profesorSeleccionado, anio, semestre, cursoSeleccionado);

            modelo.inscribir(cursoProfesor);
            JOptionPane.showMessageDialog(vista, "Asignación guardada exitosamente.");
            cargarCursoProfesores();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El año y el semestre deben ser números válidos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void cargarCursoProfesores() {
        DefaultTableModel modeloTabla = vista.getModeloTabla();
        modeloTabla.setRowCount(0);

        try {
            modelo.cargarDatosDB();

            for (CursoProfesor asignacion : modelo.getListado()) {
                modeloTabla.addRow(new Object[]{
                        asignacion.getProfesor().getNombres(),
                        asignacion.getCurso().getNombre(),
                        asignacion.getAnio(),
                        asignacion.getSemestre()
                });
            }
            modeloTabla.fireTableDataChanged();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar asignaciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarAsignacion() {
        int filaSeleccionada = vista.getTablaAsignaciones().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una asignación para eliminar.");
            return;
        }

        String profesorNombre = vista.getModeloTabla().getValueAt(filaSeleccionada, 0).toString();
        String cursoNombre = vista.getModeloTabla().getValueAt(filaSeleccionada, 1).toString();

        CursoProfesor asignacionAEliminar = modelo.getListado().stream()
                .filter(cp -> cp.getProfesor().getNombres().equals(profesorNombre) &&
                        cp.getCurso().getNombre().equals(cursoNombre))
                .findFirst()
                .orElse(null);

        if (asignacionAEliminar == null) {
            JOptionPane.showMessageDialog(vista, "No se encontró la asignación en el modelo.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar esta asignación?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            modelo.eliminar(asignacionAEliminar);
            JOptionPane.showMessageDialog(vista, "Asignación eliminada correctamente.");
            cargarCursoProfesores();
        }
    }


    private void seleccionarAsignacion() {
        int filaSeleccionada = vista.getTablaAsignaciones().getSelectedRow();
        if (filaSeleccionada != -1) {
            vista.getTxtAnio().setText(vista.getModeloTabla().getValueAt(filaSeleccionada, 2).toString());
            vista.getTxtSemestre().setText(vista.getModeloTabla().getValueAt(filaSeleccionada, 3).toString());
        }
    }
}