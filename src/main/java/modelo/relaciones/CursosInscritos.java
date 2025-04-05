package modelo.relaciones;

import DAO.InscripcionDAO;
import interfaces.Servicios;
import modelo.institucion.Inscripcion;

import java.util.ArrayList;
import java.util.List;

public class CursosInscritos implements Servicios {
    private List<Inscripcion> listado;
    private final InscripcionDAO inscripcionDAO;

    public CursosInscritos() {
        this.inscripcionDAO = new InscripcionDAO();
        this.listado = new ArrayList<>();
    }

    public List<Inscripcion> getListado() {
        return listado;
    }

    public void setListado(List<Inscripcion> listado) {
        this.listado = listado;
    }

    public void inscribir(Inscripcion inscripcion) {
        listado.add(inscripcion);
        inscripcionDAO.insertarInscripcion(inscripcion);
    }

    public void cargarDatosH2() {
        listado.clear();

        List<Inscripcion> inscripciones = inscripcionDAO.obtenerTodasLasInscripciones();
        listado.addAll(inscripciones);

        if (listado.isEmpty()) {
            System.out.println("No hay datos en la base de datos.");
        } else {
            System.out.println("Datos de la base de datos cargados exitosamente!");
        }
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