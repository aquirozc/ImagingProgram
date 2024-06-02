package com.aquirozc.jimp.strings;

public class Texts {

    public final static String BW_WARNING = 
    
    """

        La siguiente operación requiere convertir
        la imagen a escala de grises, ¿Desea continuar?
            
    """.trim();

    public final static String NON_ENCLOSING_BOUNDS_ERROR = 
    
    """
        El límite inferior debe ser menor
        al límite superior.
            
    """;

    public final static String OUTOFBOUNDS_ERROR = 
    
    """

        Los valores deben estar entre 0 y 255
            
    """.trim();

    public final static String PROGRAM_INFO = 

    String.format(

    """

        Desarrollado por %s
        Numero de compilación = %s
        Fecha de compilación = %s
                    
    """,Enviroment.MAIN_AUTHOR, Enviroment.CURRENT_VERSION, Enviroment.BUILD_DATE);

    public static final String PROGRAM_TITLE = String.format("The Java Imaging Program (%s)",Enviroment.CURRENT_VERSION);
    
}
