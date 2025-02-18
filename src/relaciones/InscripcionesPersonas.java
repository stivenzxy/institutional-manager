package relaciones;

import entidades.Persona;

import java.util.ArrayList;

public class InscripcionesPersonas {
    private ArrayList<Persona> listado;

    public InscripcionesPersonas(ArrayList<Persona> personas) {
        this.listado = new ArrayList<>();
    }

    public ArrayList<Persona> getPersonas() {
        return listado;
    }

    public void setPersonas(ArrayList<Persona> personas) {
        this.listado = personas;
    }

    public void inscribir(Persona persona) {
        listado.add(persona);
    }

    public void eliminar(Persona persona) {}
    public void actualizar(Persona persona) {}
    public void guardarInformacion(Persona persona) {}
    public void cargarDatos() {}
}