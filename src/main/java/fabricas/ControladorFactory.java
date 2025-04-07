package fabricas;

import controlador.ControladorCursos;
import controlador.ControladorCursosEstudiantes;
import controlador.ControladorEstudiantes;
import controlador.ControladorProfesores;

public class ControladorFactory {
    public static ControladorCursos CrearControladorCursos () {
        return new ControladorCursos();
    }

    public static ControladorProfesores CrearControladorProfesores() {
        return new ControladorProfesores();
    }

    public static ControladorEstudiantes CrearControladorEstudiantes() {
        return new ControladorEstudiantes();
    }

    public static ControladorCursosEstudiantes CrearControladorCursosEstudiantes() {
        return new ControladorCursosEstudiantes();
    }
}
