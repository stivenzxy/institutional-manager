package vista;

import controlador.ControladorPersonas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.IntStream;
import modelo.institucion.Programa;

public class GestionPersonasGUI extends JPanel implements PropertyChangeListener {
    private final JTextField txtNombres, txtApellidos, txtEmail;
    private final JTextField txtCodigo, txtPromedio, txtTipoContrato;
    private final JButton btnGuardar, btnActualizar, btnEliminar, btnCargar, btnLimpiarForm;
    private final JTable tablaPersonas;
    private final DefaultTableModel modeloTabla;
    private final JComboBox<String> cmbTipoPersona;
    private final JComboBox<Programa> cmbPrograma;
    private final JCheckBox checkActivo;

    private final ControladorPersonas controlador;

    public GestionPersonasGUI() {
        setLayout(new BorderLayout());
        controlador = new ControladorPersonas();

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
        agregarCampo(panelFormulario, gbc, "Nombres:", txtNombres = new JTextField(10), 1);
        agregarCampo(panelFormulario, gbc, "Apellidos:", txtApellidos = new JTextField(10), 2);
        agregarCampo(panelFormulario, gbc, "Email:", txtEmail = new JTextField(10), 3);
        agregarCampo(panelFormulario, gbc, "Tipo de Usuario:", cmbTipoPersona = new JComboBox<>(new String[]{"Persona", "Estudiante", "Profesor"}), 4);
        agregarCampo(panelFormulario, gbc, "Código:", txtCodigo = new JTextField(10), 5);
        agregarCampo(panelFormulario, gbc, "Activo:", checkActivo = new JCheckBox(), 6);
        agregarCampo(panelFormulario, gbc, "Promedio:", txtPromedio = new JTextField(10), 7);
        agregarCampo(panelFormulario, gbc, "Programa:", cmbPrograma = new JComboBox<Programa>(), 8);
        agregarCampo(panelFormulario, gbc, "Tipo de Contrato:", txtTipoContrato = new JTextField(10), 9);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 5, 5));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnCargar = new JButton("Cargar listado");
        btnLimpiarForm = new JButton("Limpiar Form");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);
        panelBotones.add(btnLimpiarForm);
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

        inicializarEventos();
        controlador.agregarListener(this);
        controlador.cargarProgramas();
        eventoBtnCargar();
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JComponent componente, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    private void inicializarEventos() {
        btnGuardar.addActionListener(event -> eventoBtnGuardar());
        btnActualizar.addActionListener(event -> eventoBtnActualizar());
        btnEliminar.addActionListener(event -> eventoBtnEliminar());
        btnCargar.addActionListener(event -> eventoBtnCargar());
        btnLimpiarForm.addActionListener(event -> limpiarCampos());

        eventoSeleccionarRegistro();
        mostrarMensajes();
    }

    private void eventoSeleccionarRegistro() {
        tablaPersonas.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) seleccionarPersona();
        });
    }

    public void mostrarMensajes() {
        controlador.agregarListener(event -> {
            if ("mensaje".equals(event.getPropertyName())) {
                JOptionPane.showMessageDialog(this, event.getNewValue().toString());
            }
        });
    }

    private void eventoBtnGuardar() {
        try {
            Map<String, Object> datos = obtenerDatosFormulario();
            controlador.guardarPersona(datos);
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
    }

    private void eventoBtnActualizar() {
        int filaSeleccionada = tablaPersonas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una persona para actualizar.");
            return;
        }

        try {
            Map<String, Object> datos = obtenerDatosFormulario();
            double id = Double.parseDouble(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            datos.put("id", id);

            controlador.actualizarPersona(datos);
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Error al procesar los datos.");
        }
    }

    private void eventoBtnEliminar() {
        int filaSeleccionada = tablaPersonas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una persona para eliminar.");
            return;
        }

        try {
            double id = Double.parseDouble(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            controlador.eliminarPersona(id);
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Error al procesar los datos.");
        }
    }

    public void eventoBtnCargar() { controlador.cargarPersonas(); }

    private Map<String, Object> obtenerDatosFormulario() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("nombres", txtNombres.getText());
        datos.put("apellidos", txtApellidos.getText());
        datos.put("email", txtEmail.getText());
        datos.put("tipo", cmbTipoPersona.getSelectedItem());
        datos.put("codigo", txtCodigo.getText());
        datos.put("promedio", txtPromedio.getText());
        datos.put("activo", checkActivo.isSelected());
        datos.put("programa", cmbPrograma.getSelectedItem());
        datos.put("tipoContrato", txtTipoContrato.getText());

        return datos;
    }

    private void actualizarTabla(List<Object[]> datos) {
        modeloTabla.setRowCount(0);
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
        modeloTabla.fireTableDataChanged();
    }

    public void seleccionarPersona() {
        int fila = tablaPersonas.getSelectedRow();
        if (fila == -1) return;

        limpiarCampos();

        JTextField[] campos = { txtNombres, txtApellidos, txtEmail };

        IntStream.range(0, campos.length)
                .forEach(i -> campos[i].setText(modeloTabla.getValueAt(fila, i + 1).toString()));

        String tipo = modeloTabla.getValueAt(fila, 4).toString();
        cmbTipoPersona.setSelectedItem(tipo);

        if ("Estudiante".equals(tipo)) {
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(0);

            Object codigoObj = modeloTabla.getValueAt(fila, 5);
            String codigoFormateado = codigoObj instanceof Number ? df.format(((Number) codigoObj).longValue()) : codigoObj.toString();

            txtCodigo.setText(codigoFormateado);
            checkActivo.setSelected("Si".equals(modeloTabla.getValueAt(fila, 6).toString()));
            txtPromedio.setText(modeloTabla.getValueAt(fila, 7).toString());

            cmbPrograma.setSelectedItem(
                    IntStream.range(0, cmbPrograma.getItemCount())
                            .mapToObj(cmbPrograma::getItemAt)
                            .filter(p -> p.getNombre().equals(modeloTabla.getValueAt(fila, 8).toString()))
                            .findFirst().orElse(null)
            );
        } else if ("Profesor".equals(tipo)) {
            txtTipoContrato.setText(modeloTabla.getValueAt(fila, 9).toString());
        }
        actualizarCampos();
    }

    private void actualizarCampos() {
        boolean esEstudiante = "Estudiante".equals(cmbTipoPersona.getSelectedItem());
        txtCodigo.setEnabled(esEstudiante);
        checkActivo.setEnabled(esEstudiante);
        txtPromedio.setEnabled(esEstudiante);
        cmbPrograma.setEnabled(esEstudiante);

        boolean esProfesor = "Profesor".equals(cmbTipoPersona.getSelectedItem());
        txtTipoContrato.setEnabled(esProfesor);
    }

    private void limpiarCampos() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
        txtCodigo.setText("");
        txtPromedio.setText("");
        txtTipoContrato.setText("");
        checkActivo.setSelected(false);
        cmbTipoPersona.setSelectedIndex(0);
        cmbPrograma.setSelectedIndex(-1);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propiedad = evt.getPropertyName();
        Object nuevoValor = evt.getNewValue();

        if ("datosPersonas".equals(propiedad)) {
            if (nuevoValor instanceof List<?> datosGenericos) {
                List<Object[]> datos = datosGenericos.stream()
                        .filter(obj -> obj instanceof Object[])
                        .map(obj -> (Object[]) obj)
                        .toList();
                actualizarTabla(datos);
            }
        } else if ("programasCargados".equals(propiedad)) {
            if (nuevoValor instanceof List<?> listaGenerica) {
                cmbPrograma.removeAllItems();
                listaGenerica.forEach(programa -> {
                    if (programa instanceof Programa) {
                        cmbPrograma.addItem((Programa) programa);
                    }
                });
            }
        }
    }
}