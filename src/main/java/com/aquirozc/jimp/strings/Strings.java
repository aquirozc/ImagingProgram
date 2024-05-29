package com.aquirozc.jimp.strings;

import java.util.jar.Manifest;
import com.aquirozc.jimp.init.FXApp;

public class Strings {

static String buildate = getBuildDate();

    public final static String BW_WARNING = 
    
    """

            La siguiente operación requiere convertir
            la imagen a escala de grises, ¿Desea continuar?
            
    """.trim();

    public final static String OUTOFBOUNDS_ERROR = 
    
    """

           Los valores deben estar entre 0 y 255
            
    """.trim();

    public final static String PROGRAM_INFO = 

    String.format(

    """

        Desarrollado por Alejandro Quiroz Carmona
        Numero de compilación = %s
        Fecha de compilación = %s
                    
    """,Strings.class.getPackage().getImplementationVersion(),buildate);

    private static String getBuildDate(){

        String date = "";

        try {
        Manifest manifest = new Manifest(FXApp.class.getResourceAsStream("/META-INF/MANIFEST.MF"));
        date = manifest.getMainAttributes().getValue("Build-Time");
        } catch (Exception e) {}

        return date;

    }
    
}
