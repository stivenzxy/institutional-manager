package vista.ventanas;

import controlador.ControladorCursosEstudiantes;
import modelo.entidades.Estudiante;
import vista.paneles.FormularioInscripcionCurso;
import vista.paneles.VistaCursosPanel;
import vista.paneles.VistaProfesoresPanel;

import javax.swing.*;
import java.awt.*;

public class EstudianteDetalle extends JFrame {
    private JTextField campoCodigo;
    private JTextField campoEstudiante;
    private JButton botonBuscar;
    private JTabbedPane tabs;

    private final ControladorCursosEstudiantes controlador;

    public EstudianteDetalle() {
        controlador = new ControladorCursosEstudiantes();

        configurarVentana();
        agregarComponentes();
        configurarMenu();

        botonBuscar.addActionListener(evento->buscarEstudiante());
    }

    private void configurarVentana() {
        setTitle("Detalle de estudiante");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void agregarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelPrincipal.add(crearPanelSuperior(), BorderLayout.NORTH);
        panelPrincipal.add(crearPanelPestanias(), BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private void configurarMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFormularios = new JMenu("Menú");
        JMenuItem itemCursos = new JMenuItem("Formulario de Cursos");
        itemCursos.addActionListener(evento -> FormularioCurso.getInstancia().setVisible(true));
        JMenuItem itemProfesores = new JMenuItem("Formulario de Profesores");
        itemProfesores.addActionListener(evento -> FormularioProfesor.getInstancia().setVisible(true));
        JMenuItem itemEstudiantes = new JMenuItem("Formulario de Estudiantes");
        itemEstudiantes.addActionListener(evento -> FormularioEstudiante.getInstancia().setVisible(true));

        menuFormularios.add(itemCursos);
        menuFormularios.add(itemProfesores);
        menuFormularios.add(itemEstudiantes);

        menuBar.add(menuFormularios);
        setJMenuBar(menuBar);
    }

    private JPanel crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelSuperior.add(new JLabel("Código:"), gbc);

        gbc.gridy++;
        campoCodigo = new JTextField(15);
        panelSuperior.add(campoCodigo, gbc);

        gbc.gridx = 1;
        botonBuscar = new JButton("Buscar");
        panelSuperior.add(botonBuscar, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panelSuperior.add(new JLabel("Estudiante:"), gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        campoEstudiante = new JTextField(20);
        campoEstudiante.setEditable(false);
        panelSuperior.add(campoEstudiante, gbc);

        return panelSuperior;
    }

    private JPanel crearPanelPestanias() {
        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.addTab("Historial Cursos", new JPanel());
        tabs.addTab("Inscribir Curso", new FormularioInscripcionCurso());
        tabs.addTab("Cursos", new VistaCursosPanel());
        tabs.addTab("Profesores", new VistaProfesoresPanel());

        JPanel panelPestanias = new JPanel(new BorderLayout());
        panelPestanias.add(tabs, BorderLayout.CENTER);

        return panelPestanias;
    }

    private void buscarEstudiante() {
        try {
            double codigo = Double.parseDouble(campoCodigo.getText());
            Estudiante estudiante = controlador.buscarEstudiante(codigo);

            if (estudiante != null) {
                campoEstudiante.setText(estudiante.toString());

                for (int i = 0; i < tabs.getTabCount(); i++) {
                    Component componente = tabs.getComponentAt(i);
                    if (componente instanceof FormularioInscripcionCurso) {
                        ((FormularioInscripcionCurso) componente).setEstudiante(estudiante);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Estudiante no encontrado.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código inválido.");
        }
    }
}