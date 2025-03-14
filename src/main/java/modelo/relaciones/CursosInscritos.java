package modelo.relaciones;

import DAO.InscripcionDAO;
import modelo.entidades.Persona;
import servicios.Servicios;
import modelo.institucion.Inscripcion;
import utils.AppendableObjectOutputStream;

import java.io.*;
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
        guardarInformacion(inscripcion);
    }

    public void actualizar(Inscripcion inscripcion) {
        if (inscripcion == null) {
            System.out.println("Error: La inscripcion que intenta actualizar no existe.");
            return;
        }

        boolean actualizado = false;
        cargarDatosDB();

        for (int i = 0; i < listado.size(); i++) {
            if (listado.get(i).getID() == inscripcion.getID()) {
                listado.set(i, inscripcion);
                actualizado = true;
                break;
            }
        }

        if (actualizado) {
            inscripcionDAO.actualizarInscripcion(inscripcion);
            guardarInformacion(null);
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

        cargarDatosDB();

        if (listado.removeIf(i -> i.getID() == inscripcion.getID())) {
            System.out.println("El estudiante " + inscripcion.getEstudiante().getNombres() + " ha cancelado su inscripción al curso " + inscripcion.getCurso().getNombre() + ".");
            inscripcionDAO.eliminarInscripcion(inscripcion);
            guardarInformacion(null);
        } else {
            System.out.println("No se encontró la persona en el listado.");
        }
    }


    public void guardarInformacion(Inscripcion inscripcion) {
        File archivo = new File("inscripciones.bin");

        if(inscripcion == null) {
            try (ObjectOutputStream listadoCompleto = new ObjectOutputStream(new FileOutputStream(archivo))) {
                for(Inscripcion i : listado) {
                    listadoCompleto.writeObject(i);
                }
            } catch (IOException error) {
                System.out.println("Error al guardar el archivo" + error.getMessage());
            }
            return;
        }
        boolean archivoExiste = archivo.exists();

        try (FileOutputStream archivoSalida = new FileOutputStream(archivo, true);
             ObjectOutputStream escritura = archivoExiste
                     ? new AppendableObjectOutputStream(archivoSalida)
                     : new ObjectOutputStream(archivoSalida)) {
            escritura.writeObject(inscripcion);
            System.out.println("El curso " + inscripcion.getCurso().getNombre() + " ha sido inscrito correctamente!");
        } catch (IOException error) {
            System.out.println("Error el curso al archivo: " + error.getMessage());
        }
    }

    public void cargarDatos() {
        File archivo = new File("inscripciones.bin");

        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("El archivo 'Inscripciones.bin' no existe o está vacío. No se puede cargar la Información.");
            return;
        }

        try (ObjectInputStream lectura = new ObjectInputStream(new FileInputStream(archivo))) {
            listado.clear();
            while (true) {
                try {
                    Object obj = lectura.readObject();
                    if (obj instanceof Inscripcion) {
                        listado.add((Inscripcion) obj);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("Datos del archivo 'Inscripciones' cargados exitosamente!");
        } catch (IOException | ClassNotFoundException error) {
            error.printStackTrace(System.out);
        }
    }

    public void cargarDatosDB() {
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
        return "";
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
