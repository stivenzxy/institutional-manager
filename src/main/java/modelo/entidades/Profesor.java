package modelo.entidades;

public class Profesor extends Persona {
    private String tipoContrato;

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
