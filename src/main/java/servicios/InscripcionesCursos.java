package servicios;

import DAO.CursoDAO;
import fabricas.DAOFactory;
import interfaces.GestorListado;
import modelo.institucion.Curso;

import java.util.ArrayList;
import java.util.List;

public class InscripcionesCursos implements GestorListado {
    CursoDAO cursoDAO;
    List<Curso> listado;

    public InscripcionesCursos() {
        cursoDAO = DAOFactory.crearCursoDAO();
        listado = new ArrayList<>();
    }

    public List<Curso> getCursos() { return listado; }

    public void guardarCurso(Curso curso) {
        if (cursoDAO.buscarPorCodigo(curso.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un curso registrado con el codigo: "+ curso.getCodigo());
        }

        listado.add(curso);
        cursoDAO.insertarCurso(curso);
    }

    public String eliminarCurso(Curso curso) {
        if (cursoDAO.tieneInscripciones(curso.getCodigo())) {
            return "No se puede eliminar el curso, tiene inscripciones activas.";
        }

        cursoDAO.eliminarCurso(curso);

        if (listado.removeIf(c -> c.getCodigo() == curso.getCodigo())) {
            return curso.getNombre() + " se ha eliminado correctamente de la lista.";
        } else {
            return "No se encontró el curso en el listado.";
        }
    }

    public Curso buscarCursoPorCodigo(double codigo) {
        return cursoDAO.buscarPorCodigo(codigo);
    }

    public List<Curso> cargarDatosH2() {
        List<Curso> cursos = cursoDAO.obtenerTodosLosCursos();
        listado.addAll(cursos);

        if (listado.isEmpty()) {
            System.out.println("No hay cursos registrados en el sistema.");
        } else {
            System.out.println("Registros cargados correctamente!");
        }

        return cursos;
    }

    @Override
    public String imprimirPosicion(String posicion) {
        return listado.get(Integer.parseInt(posicion)).toString();
    }

    @Override
    public int cantidadActual() { return listado.size(); }

    @Override
    public List<String> imprimirListado() {
        List<String> resultado = new ArrayList<>();
        for (Curso curso : listado) {
            resultado.add(curso.toString());
        }
        return resultado;
    }
}