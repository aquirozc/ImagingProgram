package com.aquirozc.jimp.init;

import com.aquirozc.jimp.controller.MainController;
import com.aquirozc.jimp.strings.Enviroment;
import com.aquirozc.jimp.strings.Texts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXApp extends Application{

    public static Parent MAIN_EDITOR;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        MAIN_EDITOR = FXApp.getActivityByName("Editor.fxml");

        System.setProperty("apple.awt.application.name", Texts.PROGRAM_TITLE);
        stage.setTitle(Texts.PROGRAM_TITLE);
        stage.setScene(new Scene(MAIN_EDITOR));
        stage.show();

        new MainController(stage);
      
        
    }

    public static Parent getActivityByName(String name){

        Parent parent = null;
    
        try {
            parent = new FXMLLoader(FXApp.class.getClassLoader().getResource(name)).load();
            parent.getStylesheets().add("caspian.css");
            parent.getStylesheets().add("caspianmod.css");
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return parent;

    }
    
    
}
