package utils;

import java.util.ResourceBundle;

public class Configuracion {
    public static String getPropiedad(String clave){
        ResourceBundle recursoBundle = ResourceBundle.getBundle("configuracion");
        return recursoBundle.getString(clave);
    }
}
