package vista.ventanas;

import controlador.ControladorProfesores;
import fabricas.ControladorFactory;
import interfaces.Observable;
import interfaces.Observador;
import modelo.entidades.Profesor;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormularioProfesor extends JFrame implements PropertyChangeListener, Observable {
    private final ControladorProfesores controlador;
    private JTextField campoID, campoNombres, campoApellidos, campoEmail, campoTipoContrato;
    private JLabel labelMensaje;

    public static FormularioProfesor instancia;
    private final List<Observador> listadoDeObservadores;

    private FormularioProfesor() {
        controlador = ControladorFactory.CrearControladorProfesores();
        controlador.agregarListener(this);
        listadoDeObservadores = new ArrayList<>();

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Formulario de Profesores");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoID = new JTextField(15);
        campoNombres = new JTextField(15);
        campoApellidos = new JTextField(15);
        campoEmail = new JTextField(15);
        campoTipoContrato = new JTextField(15);
        labelMensaje = new JLabel("", SwingConstants.CENTER);

        agregarCampo(panel, gbc, "ID:", campoID, 0);
        JButton btnBuscar = new JButton("Buscar");
        gbc.gridx = 2;
        panel.add(btnBuscar, gbc);

        agregarCampo(panel, gbc, "Nombres:", campoNombres, 1);
        agregarCampo(panel, gbc, "Apellidos:", campoApellidos, 2);
        agregarCampo(panel, gbc, "Email:", campoEmail, 3);
        agregarCampo(panel, gbc, "Tipo de Contrato: ", campoTipoContrato,4);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        panel.add(panelBotones, gbc);

        gbc.gridy = 6;
        panel.add(labelMensaje, gbc);

        btnGuardar.addActionListener(evento -> guardarProfesor());
        btnEliminar.addActionListener(evento -> eliminarProfesor());
        btnBuscar.addActionListener(evento -> buscarProfesor());

        add(panel);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JComponent componente, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    public static FormularioProfesor getInstancia() {
        if (instancia == null) {
            instancia = new FormularioProfesor();
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


    private void guardarProfesor() {
        Map<String, Object> datosFormulario = obtenerDatosFormulario();

        if (campoID.getText() != null && !campoID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El ID es generado automáticamente. Omita este campo al guardar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        controlador.guardarProfesor(datosFormulario);
        notificar();
        limpiarCampos();
    }

    private Map<String, Object> obtenerDatosFormulario() {
        Map<String, Object> datos = new HashMap<>();

        if (!campoNombres.getText().trim().isEmpty()) datos.put("nombres", campoNombres.getText().trim());
        if (!campoApellidos.getText().trim().isEmpty()) datos.put("apellidos", campoApellidos.getText().trim());
        if (!campoEmail.getText().trim().isEmpty()) datos.put("email", campoEmail.getText().trim());
        if (!campoTipoContrato.getText().trim().isEmpty()) datos.put("tipoContrato", campoTipoContrato.getText().trim());

        return datos;
    }

    private void buscarProfesor() {
        try {
            double codigo = Double.parseDouble(campoID.getText());
            limpiarCampos();

            Profesor profesor = controlador.buscarProfesorPorId(codigo);

            if (profesor != null) {
                campoNombres.setText(profesor.getNombres());
                campoApellidos.setText(profesor.getApellidos());
                campoEmail.setText(profesor.getEmail());
                campoTipoContrato.setText(profesor.getTipoContrato());

                labelMensaje.setText("Profesor encontrado.");
            } else {
                labelMensaje.setText("Profesor no encontrado.");
            }
        } catch (NumberFormatException e) {
            labelMensaje.setText("Código inválido.");
        }
    }

    private void eliminarProfesor() {
        try {
            Map<String, Object> datosForm = obtenerDatosFormulario();
            controlador.eliminarProfesor(datosForm);
            notificar();
            limpiarCampos();
        } catch (NumberFormatException e) {
            labelMensaje.setText("ID inválido.");
        }
    }

    private void limpiarCampos() {
        campoNombres.setText("");
        campoApellidos.setText("");
        campoEmail.setText("");
        campoTipoContrato.setText("");
        labelMensaje.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evento) {
        if ("mensaje".equals(evento.getPropertyName())) {
            JOptionPane.showMessageDialog(this, evento.getNewValue().toString(), "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}