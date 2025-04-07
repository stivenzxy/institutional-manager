package fabricas;

import servicios.InscripcionesCursos;
import servicios.InscripcionesCursosEstudiantes;
import servicios.InscripcionesEstudiantes;
import servicios.InscripcionesProfesores;

public class ServicioFactory {
    public static InscripcionesCursosEstudiantes crearInscripcionesCursosEstudiantes() {
        return new InscripcionesCursosEstudiantes();
    }

    public static InscripcionesEstudiantes crearInscripcionesEstudiantes() {
        return new InscripcionesEstudiantes();
    }

    public static InscripcionesProfesores crearInscripcionesProfesores() {
        return new InscripcionesProfesores();
    }

    public static InscripcionesCursos crearInscripcionesCursos() {
        return new InscripcionesCursos();
    }
}
