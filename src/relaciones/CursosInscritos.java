package relaciones;

import institucion.Inscripcion;

import java.util.ArrayList;
import java.util.List;

public class CursosInscritos {
    private List<Inscripcion> listado;

    public CursosInscritos(List<Inscripcion> listado) {
        this.listado = new ArrayList<>();
    }

    public List<Inscripcion> getListado() {
        return listado;
    }

    public void setListado(List<Inscripcion> listado) {
        this.listado = listado;
    }
}
