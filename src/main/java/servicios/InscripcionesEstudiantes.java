package servicios;

import DAO.EstudianteDAO;
import interfaces.Servicios;
import modelo.entidades.Estudiante;

import java.util.ArrayList;
import java.util.List;

public class InscripcionesEstudiantes implements Servicios {
    private final List<Estudiante> listado;
    private final EstudianteDAO estudianteDAO;

    public List<Estudiante> getEstudiantes() {
        return listado;
    }

    public InscripcionesEstudiantes() {
        listado = new ArrayList<>();
        estudianteDAO = new EstudianteDAO();
    }

    public void inscribirEstudiante(Estudiante estudiante) {
        if (estudianteDAO.buscarPorCodigo(estudiante.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un estudiante registrado con el código: " + estudiante.getCodigo());
        }

        listado.add(estudiante);
        estudianteDAO.insertar(estudiante);
    }

    public void eliminarEstudiante(Estudiante estudiante) {
        estudianteDAO.eliminar(estudiante);

        if (listado.removeIf(est -> est.getCodigo() == estudiante.getCodigo())) {
            System.out.println(estudiante.getNombres() + " " + estudiante.getApellidos() +
                    " se ha eliminado correctamente de la lista.\n");
        } else {
            System.out.println("No se encontró el estudiante en el listado.");
        }
    }

    public Estudiante buscarEstudiantePorCodigo(double codigo) {
        return estudianteDAO.buscarPorCodigo(codigo);
    }

    public List<Estudiante> cargarDatosH2() {
        listado.clear();

        List<Estudiante> estudiantes = estudianteDAO.obtenerTodosLosEstudiantes();
        listado.addAll(estudiantes);

        if (listado.isEmpty()) {
            System.out.println("No hay estudiantes registrados en el sistema.");
        } else {
            System.out.println("Registros cargados correctamente!");
        }

        return estudiantes;
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
        for (Estudiante estudiante : listado) {
            resultado.add(estudiante.toString());
        }
        return resultado;
    }
}