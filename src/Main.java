import entidades.Estudiante;
import entidades.Persona;
import entidades.Profesor;
import institucion.Curso;
import institucion.Facultad;
import institucion.Programa;
import relaciones.CursoProfesor;
import relaciones.CursosProfesores;
import relaciones.InscripcionesPersonas;

// Jhorman Alexander Carrillo Perez - 160004505
// Jesus Estiven Perez Torres - 160004725
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Persona persona1 = new Persona(1115723892, "Elvis", "Perez", "elvis@x.com");
        Facultad facultad1 = new Facultad(603, "FCBI", persona1);
        Programa programa1 = new Programa(401, "Ingenieria de Sistemas", 10, "2025", facultad1);
        Profesor profesor1 =  new Profesor(1000001, "Roger", "Calderon", "rcalderon@unillanos.edu.co", "Catedrático");

        InscripcionesPersonas inscripcionesPersonas = new InscripcionesPersonas();
        inscripcionesPersonas.cargarDatos();

        /* inscripcionesPersonas.inscribir(new Persona(5, "Johan", "Arango", "arango@gmail.com"));
        inscripcionesPersonas.inscribir(new Persona(2.0, "Ana", "Gómez", "ana@example.com"));
        inscripcionesPersonas.inscribir(new Persona(3.0, "Carlos", "López", "carlos@example.com")); */
        System.out.println("Lista de personas antes de eliminar:");
        System.out.println(inscripcionesPersonas.imprimirListado());
        // Eliminar una persona con ID 5.0
        inscripcionesPersonas.eliminar(5.0);

        System.out.println(inscripcionesPersonas.imprimirListado());
        System.out.println("Cantidad de elementos en el listado de Personas:" + inscripcionesPersonas.cantidadActual());

        Curso curso1 = new Curso(603702, "Tecnologías Avanzadas", programa1, true);
        Curso curso2 = new Curso(603702, "Software II - Profundización", programa1, true);
        CursoProfesor cursoProfesor1 = new CursoProfesor(profesor1, 2025, 7, curso1);
        CursoProfesor cursoProfesor2 = new CursoProfesor(profesor1, 2025, 8, curso2);

        CursosProfesores cursosProfesores = new CursosProfesores();
        cursosProfesores.cargarDatos();

        cursosProfesores.inscribir(cursoProfesor1);
        cursosProfesores.inscribir(cursoProfesor2);

        System.out.println(cursosProfesores.imprimirListado());

    }
}

