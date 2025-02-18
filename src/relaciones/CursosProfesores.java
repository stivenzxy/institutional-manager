package relaciones;

import java.util.ArrayList;
import java.util.List;

public class CursosProfesores {
    private ArrayList<CursoProfesor> listado;

    public CursosProfesores(ArrayList<CursoProfesor> listado) {
        this.listado = new ArrayList<>();
    }

    public ArrayList<CursoProfesor> getListado() {
        return listado;
    }

    public void setListado(ArrayList<CursoProfesor> listado) {
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
