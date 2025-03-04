import modelo.ConexionDB;
import vista.GestionPersonasGUI;
import controlador.ControladorPersonas;
import modelo.relaciones.InscripcionesPersonas;
import vista.VentanaPrincipal;

import javax.swing.*;

/**
 * @author Jesus Estiven Peréz Torres - 160004725
 * @author Jhorman Alexander Carrillo Perez - 160004505
 * */

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
            ConexionDB.inicializarBaseDeDatos();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Cerrando la conexión antes de salir...");
                ConexionDB.finalizarConexion();
            }));

            GestionPersonasGUI gestionPersonasGUI = (GestionPersonasGUI) ventanaPrincipal.getTabbedPane().getComponentAt(0);

            InscripcionesPersonas modelo = new InscripcionesPersonas();
            new ControladorPersonas(gestionPersonasGUI, modelo);

            ventanaPrincipal.setVisible(true);
        });
    }
}