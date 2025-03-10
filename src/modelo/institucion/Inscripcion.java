package modelo.institucion;

import modelo.entidades.Estudiante;

import java.io.Serializable;

public class Inscripcion implements Serializable {
    private double ID;
    private Curso curso;
    private int anio;
    private int semestre;
    private Estudiante estudiante;

    public Inscripcion(double ID,Curso curso, int anio, int semestre, Estudiante estudiante) {
        this.ID = ID;
        this.curso = curso;
        this.anio = anio;
        this.semestre = semestre;
        this.estudiante = estudiante;
    }

    public Inscripcion(Curso curso, int anio, int semestre, Estudiante estudiante) {
        this.curso = curso;
        this.anio = anio;
        this.semestre = semestre;
        this.estudiante = estudiante;
    }

    public double getID() {
        return ID;
    }

    public void setID(double ID) {
        this.ID = ID;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public String toString() {
        return "{ " +
                "\"ID\": \"" + ID + "\", " +
                "\"curso\": \"" + curso.getNombre() + "\", " +
                "\"anio\": " + anio + ", " +
                "\"semestre\": " + semestre + ", " +
                "\"estudiante\": { " +
                "\"nombres\": \"" + estudiante.getNombres() + "\", " +
                "\"apellidos\": \"" + estudiante.getApellidos() + "\"" +
                " }" + " }\n";
    }
}