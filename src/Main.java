import entidades.Estudiante;
import entidades.Persona;
import institucion.Facultad;
import institucion.Programa;

// Jhorman Alexander Carrillo Perez - 160004505
// Jesus Estiven Perez Torres - 160004725
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Persona persona1 = new Persona(1115723892, "Elvis", "Perez", "elvis@x.com");
        Facultad facultad1 = new Facultad(603, "FCBI", persona1);
        Programa programa1 = new Programa(401, "Ingenieria de Sistemas", 10, "2025", facultad1);
        Estudiante estudiante1 = new Estudiante(1001, "Juan", "Torres", "juan@x.com",
                160004725, true, 3.6, programa1);

        System.out.println(estudiante1);
        System.out.println(facultad1);
    }
}