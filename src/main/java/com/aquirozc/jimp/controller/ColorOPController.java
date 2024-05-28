package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.engine.ColorOp;
import com.aquirozc.jimp.init.FXApp;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ColorOPController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private Button laplaceBtn = (Button) parent.lookup("#color_tab").lookup("#laplace_btn");
	private Button gaussBtn = (Button) parent.lookup("#color_tab").lookup("#gauss_btn");
	private Button sharperBtn = (Button) parent.lookup("#color_tab").lookup("#sharper_btn");
	private Button embossBtn = (Button) parent.lookup("#color_tab").lookup("#emboss_btn");
    private Button highPassBtn = (Button) parent.lookup("#color_tab").lookup("#high_pass_btn");

    private MainController controller;
    
    public ColorOPController(MainController controller){

        this.controller = controller;

        laplaceBtn.setOnMouseClicked(this::convertToSepia);
		gaussBtn.setOnMouseClicked(this::convertToSepiaBlueish);
		sharperBtn.setOnMouseClicked(this::convertToSepiaPinkish);
		embossBtn.setOnMouseClicked(this::convertToPseudoColor);
        highPassBtn.setOnMouseClicked(this::convertToGrayscale);

    }

    private void convertToGrayscale(MouseEvent e){

        if (controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(ColorOp.toGrayScale(controller.getOGImage()));
        controller.setIsBWImage(true);
        controller.applyChanges();

    }

    private void convertToSepia(MouseEvent e){

        if (controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(ColorOp.toSepia(controller.getOGImage()));
        controller.setIsBWImage(false);
        controller.applyChanges();

    }

    private void convertToSepiaBlueish(MouseEvent e){

        if (controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(ColorOp.toBlueish(controller.getOGImage()));
        controller.setIsBWImage(false);
        controller.applyChanges();

    }

    private void convertToSepiaPinkish(MouseEvent e){

        if (controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(ColorOp.toPinkish(controller.getOGImage()));

    }

    private void convertToPseudoColor(MouseEvent e){

        if (controller.isOGImageNull()){
            return;
        }

        if (!controller.isBWImage()) {
            return;
        }

        controller.updateCanvas(ColorOp.toPseudoColor(controller.getOGImage()));
        controller.setIsBWImage(false);
        controller.applyChanges();

    }

}
