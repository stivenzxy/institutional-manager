import entidades.Estudiante;
import entidades.Persona;
import entidades.Profesor;
import institucion.Curso;
import institucion.Facultad;
import institucion.Inscripcion;
import institucion.Programa;
import relaciones.CursoProfesor;
import relaciones.CursosInscritos;
import relaciones.CursosProfesores;
import relaciones.InscripcionesPersonas;

/**
 * @author Jesus Estiven Peréz Torres - 160004725
 * @author Jhorman Alexander Carrillo Perez - 160004505
 * */

public class Main {
    public static void main(String[] args) {
        Persona persona1 = new Persona(1, "Jhorman", "Carrillo", "alexander@gmail.com");
        Persona persona2 = new Persona(2, "Ana", "Gómez", "ana@gmail.com");

        Profesor profesor1 = new Profesor(101010, "Roger", "Calderon", "rcalderon@unillanos.edu.co", "Catedrático");
        Profesor profesor2 = new Profesor(202020, "Olga", "Vega", "olgavega@unillanos.edu.co", "Ocasional");

        Facultad facultad1 = new Facultad(603, "FCBI", persona1);
        Programa programa1 = new Programa(401, "Ingenieria de Sistemas", 10, "4167", facultad1);

        Estudiante estudiante1 = new Estudiante(111, "Jesús", "Peréz", "jesus.perez.torres@unillanos.edu.co", 160004725, true, 3.6, programa1);
        Estudiante estudiante2 = new Estudiante(222, "Johan", "Arango", "hjarango.edu.co", 160004613, true, 3.8, programa1);
        Estudiante estudiante3 = new Estudiante(333, "Yeimy", "Lopez", "yeimiLopez.edu.co", 160004710, true, 4.2, programa1);

        InscripcionesPersonas inscripcionesPersonas = new InscripcionesPersonas();
        inscripcionesPersonas.cargarDatos();

        inscripcionesPersonas.inscribir(persona1);
        inscripcionesPersonas.inscribir(persona2);
        System.out.println(inscripcionesPersonas.imprimirListado());
        System.out.println("Cantidad de elementos en el listado de Personas:" + inscripcionesPersonas.cantidadActual() + "\n");

        inscripcionesPersonas.eliminar(persona2);

        Persona persona1Actualizada = new Persona(1, "Jhorman", "Perez", "alex2004@gmail.com");
        inscripcionesPersonas.actualizar(persona1Actualizada);

        System.out.println(inscripcionesPersonas.imprimirListado());
        System.out.println("Cantidad de elementos en el listado de Personas:" + inscripcionesPersonas.cantidadActual() + "\n");

        Curso curso1 = new Curso(603702,"Tecnologías Avanzadas", programa1, true);
        Curso curso2 = new Curso(603801,"Software I - Profundización", programa1, true);
        CursoProfesor cursoProfesor1 = new CursoProfesor(profesor1, 2025, 7, curso1);
        CursoProfesor cursoProfesor2 = new CursoProfesor(profesor2, 2025, 8, curso2);

        CursosProfesores cursosProfesores = new CursosProfesores();
        cursosProfesores.cargarDatos();

        cursosProfesores.inscribir(cursoProfesor1);
        cursosProfesores.inscribir(cursoProfesor2);

        System.out.println(cursosProfesores.imprimirListado());

        Inscripcion inscripcion1 = new Inscripcion(100,curso1, 2025, 7, estudiante1);
        Inscripcion inscripcion2 = new Inscripcion(120,curso2, 2025, 8, estudiante2);

        CursosInscritos cursosInscritos = new CursosInscritos();
        cursosInscritos.cargarDatos();

        cursosInscritos.inscribir(inscripcion1);
        cursosInscritos.inscribir(inscripcion2);
        System.out.println(cursosInscritos.imprimirListado());
        System.out.println("Numero de inscripciones activas:" + cursosInscritos.cantidadActual() + "\n");

        cursosInscritos.eliminar(inscripcion1);
        Inscripcion inscripcion2Actualizada = new Inscripcion(120,curso2, 2026, 7, estudiante3);
        cursosInscritos.actualizar(inscripcion2Actualizada);

        System.out.println(cursosInscritos.imprimirListado());
        System.out.println("Numero de inscripciones activas:" + cursosInscritos.cantidadActual() + "\n");
    }
}

