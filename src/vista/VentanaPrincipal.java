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

        // Agregar el tab de "Gestión de Personas"
        tabbedPane.addTab("Gestión de Personas", new GestionPersonasGUI());

        // Aquí podrías agregar más pestañas en el futuro
        tabbedPane.addTab("Otra Sección", new JPanel()); // Ejemplo de otra pestaña vacía

        add(tabbedPane);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}
