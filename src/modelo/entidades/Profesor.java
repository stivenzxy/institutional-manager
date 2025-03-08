package modelo.entidades;

import java.io.Serializable;

public class Profesor extends Persona implements Serializable {
    private String tipoContrato;

    public Profesor(double ID, String nombres, String apellidos, String email, String tipoContrato) {
        super(ID, nombres, apellidos, email);
        this.tipoContrato = tipoContrato;
    }

    public Profesor(String nombres, String apellidos, String email, String tipoContrato) {
        super(nombres, apellidos, email);
        this.tipoContrato = tipoContrato;
    }

    public String getTipoContrato() { return tipoContrato;}

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    @Override
    public String toString() {
        return getNombres() + " " + getApellidos();
    }
}
