package vista.ventanas;

import controlador.ControladorEstudiantes;
import interfaces.Observable;
import interfaces.Observador;
import modelo.entidades.Estudiante;
import modelo.institucion.Programa;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class FormularioEstudiante extends JFrame implements PropertyChangeListener, Observable {
    private final ControladorEstudiantes controlador;
    private final JTextField campoCodigo, campoNombres, campoApellidos, campoEmail, campoPromedio;
    private final JCheckBox checkActivo;
    private final JComboBox<Programa> selectorPrograma;
    private final JLabel labelMensaje;

    public static FormularioEstudiante instancia;
    private final List<Observador> listadoDeObservadores;

    public FormularioEstudiante() {
        controlador = new ControladorEstudiantes();
        controlador.agregarListener(this);
        listadoDeObservadores = new ArrayList<>();

        setTitle("Formulario de Estudiantes");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoCodigo = new JTextField(15);
        campoNombres = new JTextField(15);
        campoApellidos = new JTextField(15);
        campoEmail = new JTextField(15);
        campoPromedio = new JTextField(15);
        checkActivo = new JCheckBox();
        selectorPrograma = new JComboBox<>();
        labelMensaje = new JLabel("", SwingConstants.CENTER);

        agregarCampo(panel, gbc, "Código:", campoCodigo, 0);
        JButton btnBuscar = new JButton("Buscar");
        gbc.gridx = 2;
        panel.add(btnBuscar, gbc);

        agregarCampo(panel, gbc, "Nombres:", campoNombres, 1);
        agregarCampo(panel, gbc, "Apellidos:", campoApellidos, 2);
        agregarCampo(panel, gbc, "Email:", campoEmail, 3);
        agregarCampo(panel, gbc, "Promedio:", campoPromedio, 4);
        agregarCampo(panel, gbc, "Activo:", checkActivo, 5);
        agregarCampo(panel, gbc, "Programa:", selectorPrograma, 6);

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
        panel.add(labelMensaje, gbc);

        btnGuardar.addActionListener(evento -> guardarEstudiante());
        btnEliminar.addActionListener(evento -> eliminarEstudiante());
        btnBuscar.addActionListener(evento->buscarEstudiante());

        add(panel);
        controlador.cargarProgramas();
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JComponent componente, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    public static FormularioEstudiante getInstancia() {
        if (instancia == null) {
            instancia = new FormularioEstudiante();
        }
        return instancia;
    }

    @Override
    public void notificar() {
        for (Observador observador : listadoDeObservadores) {
            observador.actualizar();
        }
    }

    @Override
    public void adicionarObservador(Observador observador) {
        listadoDeObservadores.add(observador);
    }

    @Override
    public void removerObservador(Observador observador) {
        listadoDeObservadores.remove(observador);
    }

    private void guardarEstudiante() {
        Map<String, Object> datosFormulario = obtenerDatosFormulario();
        controlador.guardarEstudiante(datosFormulario);
        this.notificar();
        this.limpiarCampos();
    }

    private void buscarEstudiante() {
        try {
            double codigo = Double.parseDouble(campoCodigo.getText());
            this.limpiarCampos();

            Estudiante estudiante = controlador.buscarEstudiantePorCodigo(codigo);

            if (estudiante != null) {
                campoNombres.setText(estudiante.getNombres());
                campoApellidos.setText(estudiante.getApellidos());
                campoEmail.setText(estudiante.getEmail());
                campoPromedio.setText(String.valueOf(estudiante.getPromedio()));
                checkActivo.setSelected(estudiante.isActivo());
                selectorPrograma.setSelectedItem(estudiante.getPrograma());
                labelMensaje.setText("Estudiante encontrado.");
            } else {
                labelMensaje.setText("Estudiante no encontrado.");
            }
        } catch (NumberFormatException e) {
            labelMensaje.setText("Código inválido.");
        }
    }

    private Map<String, Object> obtenerDatosFormulario() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("codigo", campoCodigo.getText());
        datos.put("nombres", campoNombres.getText());
        datos.put("apellidos", campoApellidos.getText());
        datos.put("email", campoEmail.getText());
        datos.put("activo", checkActivo.isSelected());
        datos.put("promedio", campoPromedio.getText());
        datos.put("programa", selectorPrograma.getSelectedItem());
        return datos;
    }

    private void eliminarEstudiante() {
        try {
            double codigo = Double.parseDouble(campoCodigo.getText());
            controlador.eliminarEstudiante(codigo);
            this.notificar();
            this.limpiarCampos();
        } catch (NumberFormatException e) {
            labelMensaje.setText("Código inválido.");
        }
    }

    private void limpiarCampos() {
        campoNombres.setText("");
        campoApellidos.setText("");
        campoEmail.setText("");
        campoPromedio.setText("");
        checkActivo.setSelected(false);
        selectorPrograma.setSelectedItem(null);
        labelMensaje.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evento) {
        if ("mensaje".equals(evento.getPropertyName())) {
            JOptionPane.showMessageDialog(this, evento.getNewValue().toString(), "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else if ("programasCargados".equals(evento.getPropertyName())) {
            @SuppressWarnings("unchecked")
            List<Programa> programas = (List<Programa>) evento.getNewValue();
            selectorPrograma.removeAllItems();
            for (Programa programa : programas) {
                selectorPrograma.addItem(programa);
            }
        }
    }
}