import entidades.Estudiante;
import entidades.Persona;
import institucion.Facultad;
import institucion.Programa;
import relaciones.InscripcionesPersonas;

// Jhorman Alexander Carrillo Perez - 160004505
// Jesus Estiven Perez Torres - 160004725
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        /*Persona persona1 = new Persona(1115723892, "Elvis", "Perez", "elvis@x.com");
        Facultad facultad1 = new Facultad(603, "FCBI", persona1);
        Programa programa1 = new Programa(401, "Ingenieria de Sistemas", 10, "2025", facultad1);
        Estudiante estudiante1 = new Estudiante(1001, "Juan", "Torres", "juan@x.com",
                160004725, true, 3.6, programa1); */

        InscripcionesPersonas inscripcionesPersonas = new InscripcionesPersonas();
        inscripcionesPersonas.cargarDatos();

        /* inscripcionesPersonas.inscribir(new Persona(5, "Johan", "Arango", "arango@gmail.com"));
        inscripcionesPersonas.inscribir(new Persona(2.0, "Ana", "Gómez", "ana@example.com"));
        inscripcionesPersonas.inscribir(new Persona(3.0, "Carlos", "López", "carlos@example.com"));
        System.out.println("Lista de personas antes de eliminar:");
        System.out.println(inscripcionesPersonas.imprimirListado());
        // Eliminar una persona con ID 2.0
  //      inscripcionesPersonas.eliminar(2.0);
        inscripcionesPersonas.inscribir(new Persona(7.0, "Anamaria", "Clavijo Sanchez", "anasanchez@gmail.com"));
        inscripcionesPersonas.inscribir(new Persona(6.0, "Jorge", "Clavijo Carrero", "jorgecla@example.com"));
        inscripcionesPersonas.inscribir(new Persona(4.0, "Alex", "Carrillo", "carlex@example.com"));
        inscripcionesPersonas.inscribir(new Persona(3.0, "Astrid", "Uribe", "urtrid@example.com")); */

        // Mostrar la lista de personas después de eliminar
    //    System.out.println("Lista de personas después de eliminar:");
        System.out.println(inscripcionesPersonas.imprimirListado());

    }
}

