package modelo.entidades;

import modelo.institucion.Programa;

public class Estudiante extends Persona {
    private double codigo;
    private boolean activo;
    private double promedio;
    private Programa programa;

    public Estudiante(String nombres, String apellidos, String email, double codigo, boolean activo, double promedio, Programa programa) {
        super(nombres, apellidos, email);
        this.codigo = codigo;
        this.activo = activo;
        this.promedio = promedio;
        this.programa = programa;
    }

    public double getCodigo() {
        return codigo;
    }

    public void setCodigo(double codigo) {
        this.codigo = codigo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public Programa getPrograma() { return programa; }

    public void setPrograma(Programa programa) { this.programa = programa; }

    @Override
    public String toString() {
        return getNombres() + " " + getApellidos();
    }
}
