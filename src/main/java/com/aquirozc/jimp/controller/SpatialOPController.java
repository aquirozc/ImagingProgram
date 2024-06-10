package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.engine.SpatialOP;
import com.aquirozc.jimp.init.FXApp;

import javafx.animation.RotateTransition;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class SpatialOPController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private Button rotate90DBtn = (Button) parent.lookup("#rotate90_btn");
    private Button rotate270DBtn = (Button) parent.lookup("#rotate270_btn");
    private Button mirrorXAxisBtn = (Button) parent.lookup("#mirrorx_btn");
    private Button mirrorYAxisBtn = (Button) parent.lookup("#mirrory_btn");

    private MainController controller;
    private ImageView target_vw;

    public SpatialOPController (MainController controller){

       this.controller = controller;
       this.target_vw = controller.getTargetVW();

       rotate90DBtn.setOnMouseClicked(this::rotate90DClockwise);
       rotate270DBtn.setOnMouseClicked(this::rotate90DCounterClockwise);
       mirrorXAxisBtn.setOnMouseClicked(this::mirrorXAxis);
       mirrorYAxisBtn.setOnMouseClicked(this::mirrorYAxis);

    }

    private void rotate90DClockwise(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        RotateTransition transition = new RotateTransition(Duration.millis(300), target_vw);

        transition.setByAngle(90);
        transition.setOnFinished(a -> {
            target_vw.setRotate(0);
            controller.updateCanvas(SpatialOP.rotate90DClockwise(controller.getOGImage()));
            controller.applyChanges();
        });

        transition.play();

    }

    private void rotate90DCounterClockwise(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        RotateTransition transition = new RotateTransition(Duration.millis(300), target_vw);
        
        transition.setByAngle(-90);
        transition.setOnFinished(a -> {
            controller.updateCanvas(SpatialOP.rotate90DCounterClockwise(controller.getOGImage()));
            target_vw.setRotate(0);
            controller.applyChanges();
        });

        transition.play();

    }

    private void mirrorXAxis(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(SpatialOP.mirrorXAxis(controller.getOGImage()));
        controller.applyChanges();

    }

    private void mirrorYAxis(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(SpatialOP.mirrorYAxis(controller.getOGImage()));
        controller.applyChanges();

    }

    public void onRefresh(){
        
    }
    
}
