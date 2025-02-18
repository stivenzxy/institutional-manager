package relaciones;

import institucion.Inscripcion;

import java.util.ArrayList;

public class CursosInscritos {
    private ArrayList<Inscripcion> listado;

    public CursosInscritos(ArrayList<Inscripcion> listado) {
        this.listado = new ArrayList<>();
    }

    public ArrayList<Inscripcion> getListado() {
        return listado;
    }

    public void setListado(ArrayList<Inscripcion> listado) {
        this.listado = listado;
    }
}
