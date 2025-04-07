package servicios;

import DAO.InscripcionDAO;
import fabricas.DAOFactory;
import interfaces.GestorListado;
import modelo.entidades.Estudiante;
import modelo.institucion.Inscripcion;

import java.util.ArrayList;
import java.util.List;

public class InscripcionesCursosEstudiantes implements GestorListado {
    private final List<Inscripcion> listado;
    private final InscripcionDAO inscripcionDAO;

    public InscripcionesCursosEstudiantes() {
        this.inscripcionDAO = DAOFactory.crearInscripcionDAO();
        this.listado = new ArrayList<>();
    }

    public List<Inscripcion> getListado() {
        return listado;
    }

    public void inscribir(Inscripcion inscripcion) {
        listado.add(inscripcion);
        inscripcionDAO.insertarInscripcion(inscripcion);
    }

    public List<Inscripcion> obtenerInscripcionesPorEstudiante(Estudiante estudiante) {
        listado.clear();
        return inscripcionDAO.obtenerPorEstudiante(estudiante);
    }

    public List<Inscripcion> cargarDatosH2() {
        listado.clear();

        List<Inscripcion> inscripciones = inscripcionDAO.obtenerTodasLasInscripciones();
        listado.addAll(inscripciones);

        if (listado.isEmpty()) {
            System.out.println("No hay datos en la base de datos.");
        } else {
            System.out.println("Datos de la base de datos cargados exitosamente!");
        }

        return inscripciones;
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
        for (Inscripcion inscripcion : listado) {
            resultado.add(inscripcion.toString());
        }
        return resultado;
    }
}