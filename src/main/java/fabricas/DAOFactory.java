package fabricas;

import DAO.*;

public class DAOFactory {
    public static PersonaDAO crearPersonaDAO() {
        return new PersonaDAO();
    }

    public static EstudianteDAO crearEstudianteDAO() {
        return new EstudianteDAO();
    }

    public static ProfesorDAO crearProfesorDAO() {
        return new ProfesorDAO();
    }

    public static ProgramaDAO crearProgramaDAO() {
        return new ProgramaDAO();
    }

    public static CursoDAO crearCursoDAO() {
        return new CursoDAO();
    }

    public static InscripcionDAO crearInscripcionDAO() {
        return new InscripcionDAO();
    }
}
