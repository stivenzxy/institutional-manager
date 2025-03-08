package controlador;

import DAO.CursoDAO;
import DAO.EstudianteDAO;
import modelo.entidades.Estudiante;
import modelo.institucion.Curso;
import modelo.institucion.Inscripcion;
import modelo.relaciones.CursosInscritos;
import vista.GestionCursosEstudiantesGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Objects;

public class ControladorCursosEstudiantes {
    private final GestionCursosEstudiantesGUI vista;
    private final CursosInscritos modelo;

    public ControladorCursosEstudiantes(GestionCursosEstudiantesGUI vista, CursosInscritos modelo) {
        this.vista = vista;
        this.modelo = modelo;
        EstudianteDAO estudianteDAO = new EstudianteDAO();

        this.vista.getBtnGuardar().addActionListener(e -> inscribirEstudianteACurso());
        this.vista.getBtnActualizar().addActionListener(e->actualizarInscripcion());
        this.vista.getBtnEliminar().addActionListener(e -> eliminarInscripcion());
        this.vista.getBtnCargar().addActionListener(e -> cargarInscripciones());
        this.vista.getTablaInscripciones().getSelectionModel().addListSelectionListener(e -> seleccionarInscripcion());

        this.cargarInscripciones();
        estudianteDAO.obtenerTodosLosEstudiantes();

        cargarCursosEnComboBox();
        cargarEstudiantesEnComboBox();
    }

    private Inscripcion obtenerDatosInscripcion() {
        try {
            Estudiante estudianteSeleccionado = (Estudiante) vista.getCmbEstudiante().getSelectedItem();
            Curso cursoSeleccionado = (Curso) vista.getCmbCurso().getSelectedItem();
            double id = Double.parseDouble(vista.getTxtID().getText());
            int anio = Integer.parseInt(vista.getTxtAnio().getText());
            int semestre = Integer.parseInt(vista.getTxtSemestre().getText());

            if (estudianteSeleccionado == null || cursoSeleccionado == null) {
                JOptionPane.showMessageDialog(vista, "Seleccione un estudiante y un curso.");
                return null;
            }

            return new Inscripcion(id, cursoSeleccionado, anio, semestre, estudianteSeleccionado);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El año y el semestre deben ser números válidos.");
            return null;
        }
    }

    private void inscribirEstudianteACurso() {
        try {
            Inscripcion inscripcion = obtenerDatosInscripcion();
            if (inscripcion == null) return;

            modelo.inscribir(inscripcion);
            JOptionPane.showMessageDialog(vista, "Estudiante inscrito exitosamente.");
            cargarInscripciones();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Los campos deben contener valores numéricos válidos.");
        }
    }

    private void actualizarInscripcion() {
        try {
            Inscripcion inscripcion = obtenerDatosInscripcion();
            if (inscripcion == null) return;

            modelo.actualizar(inscripcion);
            JOptionPane.showMessageDialog(vista, "Inscripción actualizada correctamente.");
            cargarInscripciones();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Datos inválidos para la actualización.");
        }
    }

    private void eliminarInscripcion() {
        int filaSeleccionada = vista.getTablaInscripciones().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una inscripción para eliminar.");
            return;
        }

        DefaultTableModel modeloTabla = vista.getModeloTabla();
        double estudianteID = Double.parseDouble(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
        int cursoID = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 2).toString());

        Inscripcion inscripcionAEliminar = modelo.getListado().stream()
                .filter(i -> i.getEstudiante().getID() == estudianteID && i.getCurso().getID() == cursoID)
                .findFirst()
                .orElse(null);

        if (inscripcionAEliminar == null) {
            JOptionPane.showMessageDialog(vista, "No se encontró la inscripción.");
            return;
        }

        modelo.eliminar(inscripcionAEliminar);
        JOptionPane.showMessageDialog(vista, "Inscripción eliminada con éxito.");
        cargarInscripciones();
    }

    private void cargarInscripciones() {
        DefaultTableModel modeloTabla = vista.getModeloTabla();
        modeloTabla.setRowCount(0);

        modelo.cargarDatosDB();

        for (Inscripcion i : modelo.getListado()) {
            modeloTabla.addRow(new Object[]{i.getID(), i.getEstudiante().getNombres() + " " + i.getEstudiante().getApellidos(), i.getCurso().getID(), i.getCurso().getNombre(), i.getAnio(), i.getSemestre()});
        }
        modeloTabla.fireTableDataChanged();
    }

    private void seleccionarInscripcion() {
        int filaSeleccionada = vista.getTablaInscripciones().getSelectedRow();
        if (filaSeleccionada != -1) {
            DefaultTableModel modeloTabla = vista.getModeloTabla();

            String idInscripcion = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
            String estudianteNombre = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
            String semestre = modeloTabla.getValueAt(filaSeleccionada, 2).toString();
            String cursoNombre = modeloTabla.getValueAt(filaSeleccionada, 3).toString();
            String anio = modeloTabla.getValueAt(filaSeleccionada, 4).toString();

            for (int i = 0; i < vista.getCmbEstudiante().getItemCount(); i++) {
                Estudiante estudiante = (Estudiante) vista.getCmbEstudiante().getItemAt(i);
                if (estudiante.getNombres().equals(estudianteNombre)) {
                    vista.getCmbEstudiante().setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < vista.getCmbCurso().getItemCount(); i++) {
                Curso curso = (Curso) vista.getCmbCurso().getItemAt(i);
                if (curso.getNombre().equals(cursoNombre)) {
                    vista.getCmbCurso().setSelectedIndex(i);
                    break;
                }
            }
            vista.getTxtID().setText(idInscripcion);
            vista.getTxtAnio().setText(anio);
            vista.getTxtSemestre().setText(semestre);
        }
    }

    private void cargarCursosEnComboBox() {
        CursoDAO cursoDAO = new CursoDAO();
        List<Curso> cursos = cursoDAO.obtenerTodosLosCursos();
        vista.getCmbCurso().removeAllItems();
        for (Curso curso : cursos) {
            vista.getCmbCurso().addItem(curso);
        }
    }

    public void cargarEstudiantesEnComboBox() {
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        List<Estudiante> estudiantes = estudianteDAO.obtenerTodosLosEstudiantes();
        vista.getCmbEstudiante().removeAllItems();
        for (Estudiante estudiante : estudiantes) {
            vista.getCmbEstudiante().addItem(estudiante);
        }
    }
}