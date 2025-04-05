package modelo.institucion;

import java.io.Serializable;

public class Curso implements Serializable {
    private int ID;
    private double codigo;
    private String nombre;
    private Programa programa;
    private boolean activo;

    public Curso(double codigo, String nombre, Programa programa, boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.programa = programa;
        this.activo = activo;
    }

    public Curso(int id, String nombre) {
        this.ID = id;
        this.nombre = nombre;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getCodigo() { return codigo; }
    public void setCodigo(double codigo) { this.codigo = codigo; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public boolean isActivo() { return activo; }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}