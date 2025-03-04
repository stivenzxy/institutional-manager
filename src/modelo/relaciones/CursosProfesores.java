package modelo.relaciones;

import DAO.CursoProfesorDAO;
import modelo.entidades.Persona;
import servicios.Servicios;
import utils.AppendableObjectOutputStream;

import java.io.*;
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
        guardarInformacion(cursoProfesor);
    }

    public void eliminar(CursoProfesor cursoProfesor) {
        listado.remove(cursoProfesor);
        cursoProfesorDAO.eliminarAsignacion(cursoProfesor);
        cargarDatosDB();
    }

    public void guardarInformacion(CursoProfesor cursoProfesor) {
        if(cursoProfesor == null) {
            throw new IllegalArgumentException("Error!. No se suministró correctamente la información, verifique el curso y profesor ingresados.");
        }

        boolean archivoExiste = new File("cursosProfesores.bin").exists();

        try (FileOutputStream archivo = new FileOutputStream("cursosProfesores.bin", true);
             ObjectOutputStream escritura = archivoExiste ? new AppendableObjectOutputStream(archivo) : new ObjectOutputStream(archivo)) {
            escritura.writeObject(cursoProfesor);
            System.out.println("El curso #" + cantidadActual()  + " se ha agregado exitosamente al listado!");
        } catch (IOException error) {
            error.printStackTrace(System.out);
        }
    }

    public void cargarDatos() {
        File archivo = new File("cursosProfesores.bin");

        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("El archivo 'cursosProfesores.bin' no existe o está vacío. No se puede cargar la Información.");
            return;
        }

        try (ObjectInputStream lectura = new ObjectInputStream(new FileInputStream(archivo))) {
            listado.clear();
            while (true) {
                try {
                    Object obj = lectura.readObject();
                    if (obj instanceof CursoProfesor) {
                        listado.add((CursoProfesor) obj);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("Datos del archivo 'cursosProfesores' cargados exitosamente!");
        } catch (IOException | ClassNotFoundException error) {
            error.printStackTrace(System.out);
        }
    }

    public void cargarDatosDB() {
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
        return "";
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
