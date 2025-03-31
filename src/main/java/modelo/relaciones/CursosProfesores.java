package modelo.relaciones;

import DAO.CursoProfesorDAO;
import servicios.Servicios;

import java.util.ArrayList;
import java.util.List;

public class CursosProfesores implements Servicios {
    private List<CursoProfesor> listado;
    private final CursoProfesorDAO cursoProfesorDAO;

    public CursosProfesores() {
        this.listado = new ArrayList<>();
        this.cursoProfesorDAO = new CursoProfesorDAO();
    }

    public List<CursoProfesor> getListado() {
        return listado;
    }

    public void setListado(List<CursoProfesor> listado) {
        this.listado = listado;
    }

    public void inscribir(CursoProfesor cursoProfesor) throws Exception {
        listado.add(cursoProfesor);
        cursoProfesorDAO.asignarProfesorACurso(cursoProfesor);
    }

    public void eliminar(CursoProfesor cursoProfesor) {
        cursoProfesorDAO.eliminarAsignacion(cursoProfesor);
        cargarDatosH2();
    }

    public void cargarDatosH2() {
        listado.clear();

        List<CursoProfesor> cursoProfesor = cursoProfesorDAO.obtenerTodasLasAsignaciones();
        listado.addAll(cursoProfesor);

        if (listado.isEmpty()) {
            System.out.println("No hay datos en la base de datos.");
        } else {
            System.out.println("Datos de la base de datos cargados exitosamente!");
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String imprimirPosicion(String posicion) {
        return listado.get(Integer.parseInt(posicion)).toString();
    }

    @Override
    public int cantidadActual() {
        return listado.size();
    }

    @Override
    public List<String> imprimirListado() {
        List<String> resultado = new ArrayList<>();
        for (CursoProfesor cursoProfesor : listado) {
            resultado.add(cursoProfesor.toString());
        }
        return resultado;
    }
}