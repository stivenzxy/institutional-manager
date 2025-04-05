package vista.ventanas;

import controlador.ControladorCursos;
import interfaces.Observable;
import interfaces.Observador;
import modelo.institucion.Curso;
import modelo.institucion.Programa;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormularioCurso extends JFrame implements PropertyChangeListener, Observable {
    private final ControladorCursos controlador;
    private final JTextField campoCodigo, campoNombre;
    private final JCheckBox checkActivo;
    private final JComboBox<Programa> selectorPrograma;
    private final JLabel labelMensaje;

    private static FormularioCurso instancia;
    private final List<Observador> listadoDeObservadores;

    private FormularioCurso() {
        controlador = new ControladorCursos();
        controlador.agregarListener(this);
        listadoDeObservadores = new ArrayList<>();

        setTitle("Formulario de Cursos");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoCodigo = new JTextField(15);
        campoNombre = new JTextField(15);
        checkActivo = new JCheckBox();
        selectorPrograma = new JComboBox<>();
        labelMensaje = new JLabel("", SwingConstants.CENTER);

        agregarCampo(panel, gbc, "Código:", campoCodigo, 0);
        JButton btnBuscar = new JButton("Buscar");
        gbc.gridx = 2;
        panel.add(btnBuscar, gbc);

        agregarCampo(panel, gbc, "Nombre:", campoNombre, 1);
        agregarCampo(panel, gbc, "Activo:", checkActivo, 2);
        agregarCampo(panel, gbc, "Programa:", selectorPrograma, 3);

        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        panel.add(panelBotones, gbc);

        gbc.gridy = 5;
        panel.add(labelMensaje, gbc);


        btnGuardar.addActionListener(evento -> guardarCurso());
        btnEliminar.addActionListener(evento -> eliminarCurso());
        btnBuscar.addActionListener(evento -> buscarCurso());

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

    public static FormularioCurso getInstancia() {
        if (instancia == null) {
            instancia = new FormularioCurso();
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

    private void guardarCurso() {
        Map<String, Object> datosFormulario = obtenerDatosFormulario();
        controlador.guardarCurso(datosFormulario);
        this.notificar();
        limpiarCampos();
    }

    private Map<String, Object> obtenerDatosFormulario() {
        Map<String, Object> datos = new HashMap<>();

        if (!campoCodigo.getText().trim().isEmpty()) datos.put("codigo", campoCodigo.getText());
        if (!campoNombre.getText().trim().isEmpty()) datos.put("nombre", campoNombre.getText());
        datos.put("activo", checkActivo.isSelected());
        datos.put("programa", selectorPrograma.getSelectedItem());
        return datos;
    }

    private void buscarCurso() {
        try {
            double codigo = Double.parseDouble(campoCodigo.getText());
            limpiarCampos();

            Curso curso = controlador.buscarCursoPorCodigo(codigo);

            if (curso != null) {
                campoNombre.setText(curso.getNombre());
                checkActivo.setSelected(curso.isActivo());
                selectorPrograma.setSelectedItem(curso.getPrograma());
                labelMensaje.setText("Curso encontrado.");
            } else {
                labelMensaje.setText("Curso no encontrado.");
            }
        } catch (NumberFormatException e) {
            labelMensaje.setText("Código inválido.");
        }
    }

    private void eliminarCurso() {
        try {
            double codigo = Double.parseDouble(campoCodigo.getText());
            controlador.elimnarCurso(codigo);
            this.notificar();
            limpiarCampos();
        } catch (NumberFormatException e) {
            labelMensaje.setText("Código inválido.");
        }
    }

    private void limpiarCampos() {
        campoNombre.setText("");
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
            java.util.List<Programa> programas = (List<Programa>) evento.getNewValue();
            selectorPrograma.removeAllItems();
            for (Programa programa : programas) {
                selectorPrograma.addItem(programa);
            }
        }
    }
}