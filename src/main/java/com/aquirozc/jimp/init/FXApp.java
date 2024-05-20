package com.aquirozc.jimp.init;

import com.aquirozc.jimp.controller.MainController;
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

        stage.setTitle("Java Imaging Program Milestone 1");
        stage.setScene(new Scene(MAIN_EDITOR));
        stage.show();

        new MainController(stage);
      
        
    }

    public static Parent getActivityByName(String name){

        Parent parent = null;
    
        try {
            System.out.println(FXApp.class.getClassLoader().getResource(name));
            parent = new FXMLLoader(FXApp.class.getClassLoader().getResource(name)).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return parent;

    }
    
    
}
