package relaciones;

import Servicios.Servicios;
import entidades.Persona;
import utils.AppendableObjectOutputStream;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class InscripcionesPersonas implements Servicios {
    private List<Persona> listado;

    public InscripcionesPersonas() {
        this.listado = new ArrayList<>();
    }

    public List<Persona> getPersonas() {
        return listado;
    }

    public void setPersonas(List<Persona> personas) {
        this.listado = personas;
    }

    public void inscribir(Persona persona) {
        listado.add(persona);
        guardarInformacion(persona);
    }

    public void eliminar(Persona persona) {}
    public void actualizar(Persona persona) {}

    public void guardarInformacion(Persona persona) {
        if(persona == null) {
            throw new IllegalArgumentException("Error!. No se suministró correctamente la información de la persona.");
        }

        boolean archivoExiste = new File("personas.bin").exists();

        try (FileOutputStream archivo = new FileOutputStream("personas.bin", true);
             ObjectOutputStream escritura = archivoExiste
                     ? new AppendableObjectOutputStream(archivo)
                     : new ObjectOutputStream(archivo)) {
            escritura.writeObject(persona);
            System.out.println("Persona agregada exitosamente al listado!");
        } catch (IOException error) {
            error.printStackTrace(System.out);
        }
    }

    public void cargarDatos() {
        File archivo = new File("personas.bin");

        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("El archivo no existe o está vacío. No se puede cargar la Información.");
            return;
        }

        try (ObjectInputStream lectura = new ObjectInputStream(new FileInputStream(archivo))) {
            listado.clear();
            while (true) {
                try {
                    Object obj = lectura.readObject();
                    if (obj instanceof Persona) {
                        listado.add((Persona) obj);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("Datos cargados exitosamente!");
        } catch (IOException | ClassNotFoundException error) {
            error.printStackTrace(System.out);
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
        for (Persona persona : listado) {
            resultado.add(persona.toString());
        }
        return resultado;
    }
}