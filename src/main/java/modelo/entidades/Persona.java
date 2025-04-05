package modelo.entidades;


import java.io.Serializable;

public abstract class Persona implements Serializable {
    private double ID;
    private String nombres;
    private String apellidos;
    private String email;

    public Persona(String nombres, String apellidos, String email) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }

    public double getID() {
        return ID;
    }

    public void setID(double ID) {
        this.ID = ID;
    }

    public String getNombres() {
        return nombres;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "{ \"ID\": " + ID +
                ", \"Nombre\": \"" + nombres +
                "\", \"Apellidos\": \"" + apellidos +
                "\", \"Email\": \"" + email + "\" }\n";
    }
}
