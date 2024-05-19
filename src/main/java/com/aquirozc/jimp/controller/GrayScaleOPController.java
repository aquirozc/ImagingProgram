package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.engine.GrayScaleOp;
import com.aquirozc.jimp.init.FXApp;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;

public class GrayScaleOPController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private TitledPane brightnessPane = ((Accordion)parent.lookup("#bn_tab")).getPanes().get(0);

    private Button brightnessYBtn = (Button) brightnessPane.getContent().lookup("#brightnessy_btn");
    private Button brightnessNBtn = (Button) brightnessPane.getContent().lookup("#brightnessn_btn");
    private Slider brightnessSlider = (Slider) brightnessPane.getContent().lookup("#brightness_bar");

    private TitledPane contrastPane = ((Accordion)parent.lookup("#bn_tab")).getPanes().get(1);

    private Button contrastYBtn = (Button) contrastPane.getContent().lookup("#contrasty_btn");
    private Button contrastNBtn = (Button) contrastPane.getContent().lookup("#contrastn_btn");
    private Slider contrastGSlider = (Slider) contrastPane.getContent().lookup("#contrastg_bar");
    private Slider contrastBSlider = (Slider) contrastPane.getContent().lookup("#contrastb_bar");

    private TitledPane negativePane = ((Accordion)parent.lookup("#bn_tab")).getPanes().get(2);
    private Button negativeBtn = (Button) negativePane.getContent().lookup("#negative_btn");

    private MainController controller;

    public GrayScaleOPController (MainController controller){

        this.controller = controller;

        brightnessPane.expandedProperty().addListener(this::undoUnsavedChanges);
        brightnessYBtn.setOnMouseClicked(e -> {controller.applyChanges(); brightnessNBtn.fireEvent(e);});
        brightnessNBtn.setOnMouseClicked(e -> brightnessSlider.setValue(0));
        brightnessSlider.valueProperty().addListener(this::adjustBrightness);

        contrastPane.expandedProperty().addListener(this::undoUnsavedChanges);
        contrastYBtn.setOnMouseClicked(e -> {controller.applyChanges(); contrastNBtn.fireEvent(e);});
        contrastNBtn.setOnMouseClicked(e -> controller.refreshCanvas());
        contrastBSlider.valueProperty().addListener(this::adjustContrast);
        contrastGSlider.valueProperty().addListener(this::adjustContrast);

        negativePane.expandedProperty().addListener(this::undoUnsavedChanges);
        negativeBtn.setOnMouseClicked(this::invertColors);

    }

    public void adjustBrightness(ObservableValue<? extends Number> observable, Number oldValue, Number newValue){

        if(controller.isOGImageNull()){
            return;
        }

        if(!controller.isBWImage()){
            throw new IllegalArgumentException("La siguiente operación solo es valida para imagenes en ByN");
        }

        controller.updateCanvas(GrayScaleOp.adjustBrightness(controller.getOGImage(), (int) brightnessSlider.getValue()));
    }

    public void adjustContrast(ObservableValue<? extends Number> observable, Number oldValue, Number newValue){

        if(controller.isOGImageNull()){
            return;
        }

        if(!controller.isBWImage()){
            throw new IllegalArgumentException("La siguiente operación solo es valida para imagenes en ByN");
        }

        controller.updateCanvas(GrayScaleOp.adjustContrast(controller.getOGImage(), contrastGSlider.getValue(),contrastBSlider.getValue()));

    }

    public void invertColors(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        if(!controller.isBWImage()){
            throw new IllegalArgumentException("La siguiente operación solo es valida para imagenes en ByN");
        }

        controller.updateCanvas(GrayScaleOp.makeNegative(controller.getOGImage()));
        controller.applyChanges();

        Thread.ofVirtual().start(() -> {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            Platform.runLater(() -> invertColors(e));

        });

    }

    public void resetSliders(){

        brightnessSlider.setValue(0);
        contrastBSlider.setValue(0); 
        contrastGSlider.setValue(1);

    }

    private void undoUnsavedChanges(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
        controller.refreshCanvas();
    }

}

