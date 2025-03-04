package vista;

import DAO.CursoDAO;
import DAO.ProfesorDAO;
import DAO.CursoProfesorDAO;
import modelo.institucion.Curso;
import modelo.entidades.Profesor;
import modelo.relaciones.CursoProfesor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionCursosProfesoresGUI extends JPanel {
    private final JComboBox<Profesor> cmbProfesores;
    private final JComboBox<Curso> cmbCursos;
    private final JTextField txtAnio, txtSemestre;
    private final JButton btnAsignar, btnEliminar ,btnCargar;
    private final JTable tablaAsignaciones;
    private final DefaultTableModel modeloTabla;

    public GestionCursosProfesoresGUI() {
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTituloFormulario = new JLabel("Asignación de Cursos a Profesores", SwingConstants.CENTER);
        lblTituloFormulario.setFont(new Font("Arial", Font.BOLD, 16));
        panelFormulario.add(lblTituloFormulario, gbc);

        gbc.gridwidth = 1;
        agregarCampo(panelFormulario, gbc, "Profesor:", cmbProfesores = new JComboBox<Profesor>(), 1);
        agregarCampo(panelFormulario, gbc, "Curso:", cmbCursos = new JComboBox<Curso>(), 2);
        agregarCampo(panelFormulario, gbc, "Año:", txtAnio = new JTextField(10), 3);
        agregarCampo(panelFormulario, gbc, "Semestre:", txtSemestre = new JTextField(10), 4);

        cargarProfesoresEnComboBox();
        cargarCursosEnComboBox();

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 5, 5));
        btnAsignar = new JButton("Asignar");
        btnEliminar = new JButton("Eliminar");
        btnCargar = new JButton("Cargar Listado");
        panelBotones.add(btnAsignar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        JSeparator separador = new JSeparator();
        panelCentral.add(separador, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblTituloTabla = new JLabel("Listado de Asignaciones", SwingConstants.CENTER);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 14));
        panelTabla.add(lblTituloTabla, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"Profesor", "Curso", "Año", "Semestre"}, 0);

        tablaAsignaciones = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaAsignaciones), BorderLayout.CENTER);
        panelCentral.add(panelTabla, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JComponent componente, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    private void cargarProfesoresEnComboBox() {
        ProfesorDAO profesorDAO = new ProfesorDAO();
        List<Profesor> profesores = profesorDAO.obtenerTodosLosProfesores();
        cmbProfesores.removeAllItems();
        for (Profesor profesor : profesores) {
            cmbProfesores.addItem(profesor);
        }
    }

    private void cargarCursosEnComboBox() {
        CursoDAO cursoDAO = new CursoDAO();
        List<Curso> cursos = cursoDAO.obtenerTodosLosCursos();
        cmbCursos.removeAllItems();
        for (Curso curso : cursos) {
            cmbCursos.addItem(curso);
        }
    }

    public JComboBox<Profesor> getCmbProfesores() { return cmbProfesores; }
    public JComboBox<Curso> getCmbCursos() { return cmbCursos; }
    public JTextField getTxtAnio() { return txtAnio; }
    public JTextField getTxtSemestre() { return txtSemestre; }
    public JButton getBtnAsignar() { return btnAsignar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnCargar() { return btnCargar; }
    public JTable getTablaAsignaciones() { return tablaAsignaciones; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
}