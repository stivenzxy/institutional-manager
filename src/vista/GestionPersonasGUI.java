package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionPersonasGUI extends JFrame {
    private  final JTextField txtID, txtNombres, txtApellidos, txtEmail;
    private final JButton btnGuardar, btnActualizar, btnEliminar, btnCargar;
    private final JTable tablaPersonas;
    private final DefaultTableModel modeloTabla;

    public GestionPersonasGUI() {
        setTitle("Gestión de Personas");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Agrega margen
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtID = new JTextField(15);
        txtID.setEditable(false);
        panelFormulario.add(txtID, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombres:"), gbc);
        gbc.gridx = 1;
        txtNombres = new JTextField(15);
        panelFormulario.add(txtNombres, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 1;
        txtApellidos = new JTextField(15);
        panelFormulario.add(txtApellidos, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(15); // Campo de texto con 15 columnas de ancho
        panelFormulario.add(txtEmail, gbc);

        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 5, 5));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnCargar = new JButton("Cargar listado");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());

        JSeparator separador = new JSeparator();
        panelCentral.add(separador, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTituloTabla = new JLabel("Listado de personas", SwingConstants.CENTER);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 14));
        panelTabla.add(lblTituloTabla, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombres", "Apellidos", "Email"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo permite la edición en las columnas distintas de ID (columna 0)
                return column != 0;
            }
        };

        tablaPersonas = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaPersonas), BorderLayout.CENTER);

        panelCentral.add(panelTabla, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public JTextField getTxtID() { return txtID; }
    public JTextField getTxtNombres() { return txtNombres; }
    public JTextField getTxtApellidos() { return txtApellidos; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnCargar() { return btnCargar; }
    public JTable getTablaPersonas() { return tablaPersonas; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
}
