package relaciones;

import Servicios.Servicios;
import entidades.Persona;
import institucion.Inscripcion;

import java.util.ArrayList;
import java.util.List;

public class CursosInscritos implements Servicios {
    private List<Inscripcion> listado;

    public CursosInscritos(List<Inscripcion> listado) {
        this.listado = new ArrayList<>();
    }

    public List<Inscripcion> getListado() {
        return listado;
    }

    public void setListado(List<Inscripcion> listado) {
        this.listado = listado;
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
