package vista;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {
    private final JTabbedPane tabbedPane;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        GestionCursosProfesoresGUI gestionCursosProfesoresGUI = new GestionCursosProfesoresGUI();
        GestionCursosEstudiantesGUI gestionCursosEstudiantesGUI = new GestionCursosEstudiantesGUI();


        tabbedPane.addTab("Gestión de Cursos y Profesores", gestionCursosProfesoresGUI);
        tabbedPane.addTab("Inscripción de Cursos", gestionCursosEstudiantesGUI);

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == gestionCursosEstudiantesGUI) {
                gestionCursosEstudiantesGUI.actualizarListaEstudiantes();
            } else if (tabbedPane.getSelectedComponent() == gestionCursosProfesoresGUI) {
                gestionCursosProfesoresGUI.actualizarListaProfesores();
            }
        });
        
        add(tabbedPane);
    }
}