package vista.paneles;

import controlador.ControladorCursosEstudiantes;
import interfaces.Observable;
import interfaces.Observador;
import modelo.entidades.Estudiante;
import modelo.institucion.Curso;
import vista.ventanas.FormularioCurso;
import vista.ventanas.FormularioEstudiante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormularioInscripcionCurso extends JPanel implements Observable {
    private final JTextField campoCodigo;
    private final JTextField campoCurso;
    private final JTextField campoAnio;
    private final JComboBox<String> selectorPeriodo;

    private Estudiante estudiante;
    private final ControladorCursosEstudiantes controlador;

    private static FormularioInscripcionCurso instancia;
    private final List<Observador> listadoDeObservadores;

    public FormularioInscripcionCurso() {
        controlador = new ControladorCursosEstudiantes();
        listadoDeObservadores = new ArrayList<>();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Inscripción a Curso", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        int fila = 1;

        campoCodigo = new JTextField(10);
        JButton botonBuscar = new JButton("Buscar");
        agregarCampo(this, gbc, "Código:", campoCodigo, fila++);
        gbc.gridx = 2;
        add(botonBuscar, gbc);

        campoCurso = new JTextField(15);
        campoCurso.setEditable(false);
        agregarCampo(this, gbc, "Curso:", campoCurso, fila++);

        campoAnio = new JTextField(6);
        selectorPeriodo = new JComboBox<>(new String[]{"1", "2"});

        JPanel panelAnioPeriodo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelAnioPeriodo.add(new JLabel("Año:"));
        panelAnioPeriodo.add(campoAnio);
        panelAnioPeriodo.add(new JLabel("Período:"));
        panelAnioPeriodo.add(selectorPeriodo);

        agregarCampo(this, gbc, "Año y Período:", panelAnioPeriodo, fila++);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        JButton botonInscribir = new JButton("Inscribir");
        add(botonInscribir, gbc);

        botonBuscar.addActionListener(evento -> buscarCurso());
        botonInscribir.addActionListener(evento -> inscribirEstudianteACurso());
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JComponent componente, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    public static FormularioInscripcionCurso getInstancia() {
        if (instancia == null) {
            instancia = new FormularioInscripcionCurso();
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

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    private Map<String, Object> obtenerDatosFormulario() {
        Map<String, Object> datos = new HashMap<>();

        if (!campoCodigo.getText().trim().isEmpty()) datos.put("codigo", campoCodigo.getText());

        Curso curso = controlador.buscarCurso(Double.parseDouble(campoCodigo.getText()));
        if (curso != null) {
            datos.put("curso", curso);
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un curso válido.");
            return null;
        }

        if (!campoAnio.getText().trim().isEmpty()) datos.put("anio", campoAnio.getText());
        datos.put("periodo", selectorPeriodo.getSelectedItem());

        if (estudiante != null) {
            datos.put("estudiante", estudiante);
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estudiante antes de inscribir.");
            return null;
        }

        return datos;
    }

    private void inscribirEstudianteACurso() {
        Map<String, Object> datosFormulario = obtenerDatosFormulario();
        controlador.inscribirEstudianteACurso(datosFormulario);
        notificar();
        this.limpiarCampos();
    }

    private void buscarCurso() {
        try {
            double codigo = Double.parseDouble(campoCodigo.getText());
            limpiarCampos();

            Curso curso = controlador.buscarCurso(codigo);

            if (curso != null) {
                campoCurso.setText(curso.getNombre());
            } else {
                JOptionPane.showMessageDialog(this, "Curso no encontrado.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código inválido.");
        }
    }

    private void limpiarCampos() {
        campoCurso.setText("");
        campoAnio.setText("");
        selectorPeriodo.setSelectedItem(null);
    }
}