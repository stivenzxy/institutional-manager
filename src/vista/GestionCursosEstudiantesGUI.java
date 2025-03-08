package vista;

import DAO.CursoDAO;
import DAO.EstudianteDAO;
import modelo.institucion.Curso;
import modelo.entidades.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionCursosEstudiantesGUI extends JPanel {
    private final JTextField txtID, txtAnio, txtSemestre;
    private final JComboBox<Curso> cmbCurso;
    private final JComboBox<Estudiante> cmbEstudiante;
    private final JButton btnGuardar, btnActualizar, btnEliminar, btnCargar;
    private final JTable tablaInscripciones;
    private final DefaultTableModel modeloTabla;

    public GestionCursosEstudiantesGUI() {
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
        JLabel lblTituloFormulario = new JLabel("Formulario de Inscripción", SwingConstants.CENTER);
        lblTituloFormulario.setFont(new Font("Arial", Font.BOLD, 16));
        panelFormulario.add(lblTituloFormulario, gbc);

        gbc.gridwidth = 1;
        agregarCampo(panelFormulario, gbc, "ID:", txtID = new JTextField(10), 1);
        agregarCampo(panelFormulario, gbc, "Curso:", cmbCurso = new JComboBox<>(), 2);
        agregarCampo(panelFormulario, gbc, "Estudiante:", cmbEstudiante = new JComboBox<>(), 3);
        agregarCampo(panelFormulario, gbc, "Año:", txtAnio = new JTextField(10), 4);
        agregarCampo(panelFormulario, gbc, "Semestre:", txtSemestre = new JTextField(10), 5);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 5, 5));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnCargar = new JButton("Cargar listado");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        JSeparator separador = new JSeparator();
        panelCentral.add(separador, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblTituloTabla = new JLabel("Listado de Inscripciones", SwingConstants.CENTER);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 14));
        panelTabla.add(lblTituloTabla, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                    new Object[]{"ID", "Estudiante", "Semestre", "Curso", "Año"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        tablaInscripciones = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaInscripciones), BorderLayout.CENTER);
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

    public JTextField getTxtID() { return txtID; }
    public JTextField getTxtAnio() { return txtAnio; }
    public JTextField getTxtSemestre() { return txtSemestre; }
    public JComboBox<Curso> getCmbCurso() { return cmbCurso; }
    public JComboBox<Estudiante> getCmbEstudiante() { return cmbEstudiante; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnCargar() { return btnCargar; }
    public JTable getTablaInscripciones() { return tablaInscripciones; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
}