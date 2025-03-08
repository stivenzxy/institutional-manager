import controlador.ControladorCursosEstudiantes;
import controlador.ControladorCursosProfesores;
import modelo.dbConfig.ConexionDB;
import modelo.relaciones.CursosInscritos;
import modelo.relaciones.CursosProfesores;
import vista.GestionCursosEstudiantesGUI;
import vista.GestionCursosProfesoresGUI;
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
            GestionCursosProfesoresGUI gestionCursosProfesoresGUI = (GestionCursosProfesoresGUI) ventanaPrincipal.getTabbedPane().getComponentAt(1);
            GestionCursosEstudiantesGUI gestionCursosEstudiantesGUI = (GestionCursosEstudiantesGUI) ventanaPrincipal.getTabbedPane().getComponentAt(2);

            InscripcionesPersonas modeloPersonas = new InscripcionesPersonas();
            new ControladorPersonas(gestionPersonasGUI, modeloPersonas);

            CursosProfesores modeloCursosProfesores = new CursosProfesores();
            new ControladorCursosProfesores(gestionCursosProfesoresGUI, modeloCursosProfesores);

            CursosInscritos modeloCursosInscritos = new CursosInscritos();
            new ControladorCursosEstudiantes(gestionCursosEstudiantesGUI, modeloCursosInscritos);

            ventanaPrincipal.setVisible(true);
        });
    }
}