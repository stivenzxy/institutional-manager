package interfaces;

import java.util.List;

public interface GestorListado {
    String imprimirPosicion(String posicion);
    int cantidadActual();
    List<String> imprimirListado();
}