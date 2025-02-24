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

    public void actualizar(Persona persona) {
        if (persona == null) {
            System.out.println("Error: La persona que intenta actualizar no existe.");
            return;
        }

        cargarDatos();
        boolean actualizado = false;

        for (int i = 0; i < listado.size(); i++) {
            if (listado.get(i).getID() == persona.getID()) {
                listado.set(i, persona);
                actualizado = true;
                break;
            }
        }

        if (actualizado) {
            guardarInformacion(null);
            System.out.println("Información de " + persona.getNombres() + " " + persona.getApellidos() + " actualizada correctamente.");
        } else {
            System.out.println("No se encontró la persona en el listado.");
        }
    }

    public void eliminar(Persona persona) {
        if (persona == null) {
            System.out.println("Error: La persona que intenta eliminar de la lista no existe.");
            return;
        }

        cargarDatos();

        if (listado.removeIf(p -> p.getID() == persona.getID())) {
            System.out.println(persona.getNombres() + " " + persona.getApellidos() +
                    " se ha eliminado correctamente de la lista.\n");
            guardarInformacion(null);
        } else {
            System.out.println("No se encontró la persona en el listado.");
        }
    }

    public void guardarInformacion(Persona persona) {
        File archivo = new File("personas.bin");

        if (persona == null) { // Si el objeto es 'null' se guarda la lista completa en el archivo
            try (ObjectOutputStream listadoCompleto = new ObjectOutputStream(new FileOutputStream(archivo))) {
                for (Persona p : listado) {
                    listadoCompleto.writeObject(p);
                }
            } catch (IOException error) {
                System.out.println("Error al escribir la lista en el archivo: " + error.getMessage());
            }
            return;
        }
        boolean archivoExiste = archivo.exists();

        try (FileOutputStream archivoSalida = new FileOutputStream(archivo, true);
             ObjectOutputStream escritura = archivoExiste
                     ? new AppendableObjectOutputStream(archivoSalida)
                     : new ObjectOutputStream(archivoSalida)) {
            escritura.writeObject(persona);
            System.out.println("Persona #" + cantidadActual()  + " agregada exitosamente al listado!");
        } catch (IOException error) {
            System.out.println("Error al agregar persona al archivo: " + error.getMessage());
        }
    }

    public void cargarDatos() {
        File archivo = new File("personas.bin");

        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("El archivo 'Personas.bin' no existe o está vacío. No se puede cargar la Información.");
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
            System.out.println("Datos del archivo 'Personas' cargados exitosamente!");
        } catch (IOException | ClassNotFoundException error) {
            error.printStackTrace(System.out);
        }
    }

    @Override
    public String imprimirPosicion(String posicion) {
        return "";
    }

    @Override
    public int cantidadActual() { return listado.size(); }

    @Override
    public List<String> imprimirListado() {
        List<String> resultado = new ArrayList<>();
        for (Persona persona : listado) {
            resultado.add(persona.toString());
        }
        return resultado;
    }
}