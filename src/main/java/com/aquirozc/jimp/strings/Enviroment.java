package com.aquirozc.jimp.strings;

import com.aquirozc.jimp.init.FXApp;
import java.util.jar.Manifest;

public class Enviroment {

    public static String MAIN_AUTHOR = "Alejandro Quiroz Carmona";
    public static String CURRENT_VERSION = Enviroment.class.getPackage().getImplementationVersion();
    public static String BUILD_DATE = Enviroment.getMVNBuildDate();

    private static String getMVNBuildDate(){

        String date = "";

        try {
            Manifest manifest = new Manifest(FXApp.class.getResourceAsStream("/META-INF/MANIFEST.MF"));
            date = manifest.getMainAttributes().getValue("Build-Time");
        } catch (Exception e) {}

        return date;

    }
    
}
