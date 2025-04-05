import modelo.dbConfig.ConexionDB;
import vista.ventanas.EstudianteDetalle;

import javax.swing.*;

/**
 * @author Jesus Estiven Peréz Torres - 160004725
 * @author Jhorman Alexander Carrillo Perez - 160004505
 * */

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConexionDB.obtenerInstancia().inicializarBaseDeDatos();
            EstudianteDetalle estudianteDetalle = new EstudianteDetalle();

            estudianteDetalle.setVisible(true);
        });
    }
}