package relaciones;

import java.util.ArrayList;
import java.util.List;

public class CursosProfesores {
    private List<CursoProfesor> listado;

    public CursosProfesores(List<CursoProfesor> listado) {
        this.listado = new ArrayList<>();
    }

    public List<CursoProfesor> getListado() {
        return listado;
    }

    public void setListado(List<CursoProfesor> listado) {
        this.listado = listado;
    }

    public void inscribir(CursoProfesor cursoProfesor) {
        listado.add(cursoProfesor);
    }
    public void guardarInformacion(CursoProfesor cursoProfesor) {}

    @Override
    public String toString() {
        return super.toString();
    }
}
