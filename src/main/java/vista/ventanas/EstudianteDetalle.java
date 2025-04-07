package vista.ventanas;

import controlador.ControladorCursosEstudiantes;
import fabricas.ControladorFactory;
import interfaces.Observable;
import interfaces.Observador;
import modelo.entidades.Estudiante;
import vista.paneles.FormularioInscripcionCurso;
import vista.paneles.HistorialInscripcionesPanel;
import vista.paneles.VistaCursosPanel;
import vista.paneles.VistaProfesoresPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDetalle extends JFrame implements Observable {
    private JTextField campoCodigo;
    private JTextField campoEstudiante;
    private JButton botonBuscar;
    private JTabbedPane tabs;

    private final ControladorCursosEstudiantes controlador;
    private final List<Observador> listadoDeObservadores;
    private static EstudianteDetalle instancia;

    private EstudianteDetalle() {
        controlador = ControladorFactory.CrearControladorCursosEstudiantes();
        listadoDeObservadores = new ArrayList<>();
    }

    private void inicializar() {
        configurarVentana();
        agregarComponentes();
        configurarMenu();

        botonBuscar.addActionListener(evento -> buscarEstudiante());
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

        JMenu menuFormularios = new JMenu("Menú de Formularios >");
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
        tabs.addTab("Historial Cursos", HistorialInscripcionesPanel.getInstancia());
        tabs.addTab("Inscribir Curso", FormularioInscripcionCurso.getInstancia());
        tabs.addTab("Cursos", new VistaCursosPanel());
        tabs.addTab("Profesores", new VistaProfesoresPanel());

        JPanel panelPestanias = new JPanel(new BorderLayout());
        panelPestanias.add(tabs, BorderLayout.CENTER);

        return panelPestanias;
    }

    public static EstudianteDetalle getInstancia() {
        if (instancia == null) {
            instancia = new EstudianteDetalle();
            instancia.inicializar();
        }
        return instancia;
    }

    @Override
    public void notificar() {
        System.out.println("Notificando observadores...");
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

    private void buscarEstudiante() {
        try {
            double codigo = Double.parseDouble(campoCodigo.getText());
            Estudiante estudiante = controlador.buscarEstudiante(codigo);

            campoEstudiante.setText(estudiante != null ? estudiante.toString() : "");

            String mensaje = estudiante != null ? "" : "Estudiante no encontrado.";
            if (!mensaje.isEmpty()) {
                JOptionPane.showMessageDialog(this, mensaje);
            }

            actualizarComponentesConEstudiante(estudiante);
            notificar();
        } catch (NumberFormatException e) {
            campoEstudiante.setText("");
            JOptionPane.showMessageDialog(this, "Código inválido.");
            actualizarComponentesConEstudiante(null);
            notificar();
        }
    }

    private void actualizarComponentesConEstudiante(Estudiante estudiante) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component componente = tabs.getComponentAt(i);
            if (componente instanceof FormularioInscripcionCurso) {
                ((FormularioInscripcionCurso) componente).setEstudiante(estudiante);
            }
            if (componente instanceof HistorialInscripcionesPanel) {
                ((HistorialInscripcionesPanel) componente).setEstudiante(estudiante);
            }
        }
    }
}