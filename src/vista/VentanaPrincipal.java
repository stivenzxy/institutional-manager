package vista;


import javax.swing.*;

public class VentanaPrincipal extends JFrame {
    private final JTabbedPane tabbedPane;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Gestión de Personas", new GestionPersonasGUI());
        tabbedPane.addTab("Gestión de Cursos y Profesores", new GestionCursosProfesoresGUI());
        tabbedPane.addTab("Inscripción de Cursos", new GestionCursosEstudiantesGUI());

        add(tabbedPane);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}