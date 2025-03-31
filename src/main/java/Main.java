import modelo.dbConfig.ConexionDB;
import vista.VentanaPrincipal;
import vista.VistaEstudiantes;

import javax.swing.*;

/**
 * @author Jesus Estiven Peréz Torres - 160004725
 * @author Jhorman Alexander Carrillo Perez - 160004505
 * */

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaEstudiantes vistaEstudiantes = new VistaEstudiantes();
            ConexionDB.obtenerInstancia().inicializarBaseDeDatos();

            vistaEstudiantes.setVisible(true);
        });
    }
}