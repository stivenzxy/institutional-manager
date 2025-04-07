package servicios;

import DAO.ProfesorDAO;
import fabricas.DAOFactory;
import interfaces.GestorListado;
import modelo.entidades.Profesor;

import java.util.ArrayList;
import java.util.List;

public class InscripcionesProfesores implements GestorListado {
    private final List<Profesor> listado;
    private final ProfesorDAO profesorDAO;

    public InscripcionesProfesores() {
        listado = new ArrayList<>();
        profesorDAO = DAOFactory.crearProfesorDAO();
    }

    public List<Profesor> getProfesores() { return listado; }

    public void inscribirProfesor(Profesor profesor) {
        if (profesorDAO.buscarPorId(profesor.getID()) != null) {
            throw new IllegalArgumentException("Ya existe un profesor registrado con el id " + profesor.getID());
        }

        listado.add(profesor);
        profesorDAO.insertar(profesor);
    }

    public Double obtenerIdPorEmail(String email) {
        return profesorDAO.obtenerIdProfesorPorEmail(email);
    }

    public void eliminarProfesor(Profesor profesor) {
        profesorDAO.eliminar(profesor);

        if(listado.removeIf(prof -> prof.getID() == profesor.getID())) {
            System.out.println(profesor.getNombres() + " " + profesor.getApellidos() +
                    " se ha eliminado correctamente de la lista.\n");
        } else {
            System.out.println("No se encontró el profesor en el listado.");
        }
    }

    public Profesor buscarProfesorPorId(double idProfesor) {
        return profesorDAO.buscarPorId(idProfesor);
    }

    public List<Profesor> cargarDatosH2() {
        listado.clear();

        List<Profesor> profesores = profesorDAO.obtenerTodosLosProfesores();
        listado.addAll(profesores);

        if (listado.isEmpty()) {
            System.out.println("No hay profesores registrados en el sistema.");
        } else {
            System.out.println("Registros cargados correctamente!");
        }

        return profesores;
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
        for (Profesor profesor : listado) {
            resultado.add(profesor.toString());
        }
        return resultado;
    }
}
