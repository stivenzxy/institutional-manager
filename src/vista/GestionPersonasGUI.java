package vista;

import DAO.ProgramaDAO;
import modelo.institucion.Programa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionPersonasGUI extends JPanel {
    private final JTextField txtID, txtNombres, txtApellidos, txtEmail;
    private final JTextField txtCodigo, txtPromedio, txtTipoContrato;
    private final JButton btnGuardar, btnActualizar, btnEliminar, btnCargar;
    private final JTable tablaPersonas;
    private final DefaultTableModel modeloTabla;
    private final JComboBox<String> cmbTipoPersona;
    private final JComboBox<Programa> cmbPrograma;
    private final JCheckBox checkActivo;

    public GestionPersonasGUI() {
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
        JLabel lblTituloFormulario = new JLabel("Formulario de Registro de Usuarios", SwingConstants.CENTER);
        lblTituloFormulario.setFont(new Font("Arial", Font.BOLD, 16));
        panelFormulario.add(lblTituloFormulario, gbc);

        gbc.gridwidth = 1;
        agregarCampo(panelFormulario, gbc, "ID:", txtID = new JTextField(10), 1);
        agregarCampo(panelFormulario, gbc, "Nombres:", txtNombres = new JTextField(10), 2);
        agregarCampo(panelFormulario, gbc, "Apellidos:", txtApellidos = new JTextField(10), 3);
        agregarCampo(panelFormulario, gbc, "Email:", txtEmail = new JTextField(10), 4);
        agregarCampo(panelFormulario, gbc, "Tipo de Usuario:", cmbTipoPersona = new JComboBox<>(new String[]{"Persona", "Estudiante", "Profesor"}), 5);
        agregarCampo(panelFormulario, gbc, "Código:", txtCodigo = new JTextField(10), 6);
        agregarCampo(panelFormulario, gbc, "Activo:", checkActivo = new JCheckBox(), 7);
        agregarCampo(panelFormulario, gbc, "Promedio:", txtPromedio = new JTextField(10), 8);
        agregarCampo(panelFormulario, gbc, "Programa:", cmbPrograma = new JComboBox<Programa>(), 9);
        agregarCampo(panelFormulario, gbc, "Tipo de Contrato:", txtTipoContrato = new JTextField(10), 10);

        cargarProgramasEnComboBox();

        gbc.gridx = 0;
        gbc.gridy = 11;
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
        JLabel lblTituloTabla = new JLabel("Listado de Usuarios", SwingConstants.CENTER);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 14));
        panelTabla.add(lblTituloTabla, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombres", "Apellidos", "Email", "Tipo", "Código", "Activo", "Promedio", "Programa", "Tipo de Contrato"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };


        tablaPersonas = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaPersonas), BorderLayout.CENTER);
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

    private void cargarProgramasEnComboBox() {
        ProgramaDAO programaDAO = new ProgramaDAO();
        List<Programa> programas = programaDAO.obtenerTodosLosProgramas();
        cmbPrograma.removeAllItems();
        for (Programa programa : programas) {
            cmbPrograma.addItem(programa);
        }
    }

    public JTextField getTxtID() { return txtID; }
    public JTextField getTxtNombres() { return txtNombres; }
    public JTextField getTxtApellidos() { return txtApellidos; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextField getTxtCodigo() { return txtCodigo; }
    public JTextField getTxtPromedio() { return txtPromedio; }
    public JTextField getTxtTipoContrato() { return txtTipoContrato; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnCargar() { return btnCargar; }
    public JComboBox<String> getCmbTipoPersona() { return cmbTipoPersona; }
    public JComboBox<Programa> getCmbPrograma() { return cmbPrograma; }
    public JCheckBox getCheckActivo() { return checkActivo; }
    public JTable getTablaPersonas() { return tablaPersonas; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
}