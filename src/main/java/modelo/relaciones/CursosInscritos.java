package modelo.relaciones;

import DAO.InscripcionDAO;
import servicios.Servicios;
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

    public void actualizar(Inscripcion inscripcion) {
        if (inscripcion == null) {
            System.out.println("Error: La inscripcion que intenta actualizar no existe.");
            return;
        }

        boolean actualizado = false;
        cargarDatosH2();

        for (int i = 0; i < listado.size(); i++) {
            if (listado.get(i).getID() == inscripcion.getID()) {
                listado.set(i, inscripcion);
                actualizado = true;
                break;
            }
        }

        if (actualizado) {
            inscripcionDAO.actualizarInscripcion(inscripcion);
            System.out.println("La inscripción " + inscripcion.getID() + " se ha actualizado correctamente.");
        } else {
            System.out.println("No se encontró la inscripción en el listado.");
        }
    }

    public void eliminar(Inscripcion inscripcion) {
        if (inscripcion == null) {
            System.out.println("Error: La inscripción que intenta eliminar de la lista no existe.");
            return;
        }

        cargarDatosH2();

        if (listado.removeIf(i -> i.getID() == inscripcion.getID())) {
            System.out.println("El estudiante " + inscripcion.getEstudiante().getNombres() + " ha cancelado su inscripción al curso " + inscripcion.getCurso().getNombre() + ".");
            inscripcionDAO.eliminarInscripcion(inscripcion);
        } else {
            System.out.println("No se encontró la persona en el listado.");
        }
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