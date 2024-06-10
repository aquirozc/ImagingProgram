package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.engine.ToneOp;
import com.aquirozc.jimp.init.FXApp;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class ToneOPController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private VBox toneTab = ((VBox)parent.lookup("#tone_tab"));

    private Button commitBtn = (Button) toneTab.lookup("#commit_btn");
    private Button rollbackBtn = (Button) toneTab.lookup("#rollback_btn");
    private Slider brightnessSlider = (Slider) toneTab.lookup("#brightness_bar");
    private Slider contrastGSlider = (Slider) toneTab.lookup("#contrastg_bar");
    private Slider contrastBSlider = (Slider) toneTab.lookup("#contrastb_bar");

    private MainController controller;

    public ToneOPController (MainController controller){

        this.controller = controller;
    
        brightnessSlider.valueProperty().addListener(this::adjustBrightnessAndContrast);
        contrastBSlider.valueProperty().addListener(this::adjustBrightnessAndContrast);
        contrastGSlider.valueProperty().addListener(this::adjustBrightnessAndContrast);

        commitBtn.setOnAction(e -> {controller.applyChanges(); rollbackBtn.fireEvent(e);});
        rollbackBtn.setOnAction(e -> resetSliders());

    }

    public void adjustBrightnessAndContrast(ObservableValue<? extends Number> observable, Number oldValue, Number newValue){

        if(controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(ToneOp.adjustBrightnessAndContrast(controller.getOGImage(), (int) brightnessSlider.getValue(), (int) contrastBSlider.getValue(), contrastGSlider.getValue()));
    }

    public void resetSliders(){

        brightnessSlider.setValue(0);
        contrastBSlider.setValue(0); 
        contrastGSlider.setValue(1);

    }

}

