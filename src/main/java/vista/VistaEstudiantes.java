package vista;

import controlador.ControladorEstudiantes;
import modelo.entidades.Estudiante;
import modelo.institucion.Programa;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class VistaEstudiantes extends JFrame implements PropertyChangeListener {
    private final ControladorEstudiantes controlador;
    private final JTextField txtCodigo, txtNombres, txtApellidos, txtEmail, txtPromedio;
    private final JCheckBox chkActivo;
    private final JComboBox<Programa> cbPrograma;
    private final JLabel lblMensaje;

    public VistaEstudiantes() {
        controlador = new ControladorEstudiantes();
        controlador.agregarListener(this);

        setTitle("Gestión de Estudiantes");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo = new JTextField(15);
        txtNombres = new JTextField(15);
        txtApellidos = new JTextField(15);
        txtEmail = new JTextField(15);
        txtPromedio = new JTextField(15);
        chkActivo = new JCheckBox();
        cbPrograma = new JComboBox<>();
        lblMensaje = new JLabel("", SwingConstants.CENTER);

        agregarCampo(panel, gbc, "Código:", txtCodigo, 0);
        JButton btnBuscar = new JButton("Buscar");
        gbc.gridx = 2;
        panel.add(btnBuscar, gbc);

        agregarCampo(panel, gbc, "Nombres:", txtNombres, 1);
        agregarCampo(panel, gbc, "Apellidos:", txtApellidos, 2);
        agregarCampo(panel, gbc, "Email:", txtEmail, 3);
        agregarCampo(panel, gbc, "Promedio:", txtPromedio, 4);
        agregarCampo(panel, gbc, "Activo:", chkActivo, 5);
        agregarCampo(panel, gbc, "Programa:", cbPrograma, 6);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        panel.add(panelBotones, gbc);

        gbc.gridy = 8;
        panel.add(lblMensaje, gbc);

        btnGuardar.addActionListener(evento -> guardarEstudiante());
        btnEliminar.addActionListener(evento -> eliminarEstudiante());
        btnBuscar.addActionListener(evento->buscarEstudiante());

        add(panel);
        controlador.cargarProgramas();
        setVisible(true);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JComponent componente, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    private void guardarEstudiante() {
        Map<String, Object> datosFormulario = obtenerDatosFormulario();
        controlador.guardarEstudiante(datosFormulario);
        limpiarCampos();
    }

    private void buscarEstudiante() {
        try {
            double codigo = Double.parseDouble(txtCodigo.getText());
            limpiarCampos();

            Estudiante estudiante = controlador.buscarEstudiantePorCodigo(codigo);

            if (estudiante != null) {
                txtNombres.setText(estudiante.getNombres());
                txtApellidos.setText(estudiante.getApellidos());
                txtEmail.setText(estudiante.getEmail());
                txtPromedio.setText(String.valueOf(estudiante.getPromedio()));
                chkActivo.setSelected(estudiante.isActivo());
                cbPrograma.setSelectedItem(estudiante.getPrograma());
                lblMensaje.setText("Estudiante encontrado.");
            } else {
                lblMensaje.setText("Estudiante no encontrado.");
            }
        } catch (NumberFormatException e) {
            lblMensaje.setText("Código inválido.");
        }
    }

    private Map<String, Object> obtenerDatosFormulario() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("codigo", txtCodigo.getText());
        datos.put("nombre", txtNombres.getText());
        datos.put("apellidos", txtApellidos.getText());
        datos.put("email", txtEmail.getText());
        datos.put("activo", chkActivo.isSelected());
        datos.put("promedio", txtPromedio.getText());
        datos.put("programa", cbPrograma.getSelectedItem());
        return datos;
    }

    private void eliminarEstudiante() {
        try {
            double codigo = Double.parseDouble(txtCodigo.getText());
            controlador.eliminarEstudiante(codigo);
            limpiarCampos();
        } catch (NumberFormatException e) {
            lblMensaje.setText("Código inválido.");
        }
    }

    private void limpiarCampos() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
        txtPromedio.setText("");
        chkActivo.setSelected(false);
        cbPrograma.setSelectedItem(null);
        lblMensaje.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("mensaje".equals(evt.getPropertyName())) {
            JOptionPane.showMessageDialog(this, evt.getNewValue().toString(), "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else if ("programasCargados".equals(evt.getPropertyName())) {
            @SuppressWarnings("unchecked")
            List<Programa> programas = (List<Programa>) evt.getNewValue();
            cbPrograma.removeAllItems();
            for (Programa programa : programas) {
                cbPrograma.addItem(programa);
            }
        }
    }
}