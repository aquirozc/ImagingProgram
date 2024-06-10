package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.data.AWTKernel;
import com.aquirozc.jimp.engine.ColorFilterOp;
import com.aquirozc.jimp.engine.ConvolveOp;
import com.aquirozc.jimp.init.FXApp;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class FilterOPController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private Button grayscaleBtn = (Button) parent.lookup("#filter_tab").lookup("#grayscale_btn");
	private Button pseudocolorBtn = (Button) parent.lookup("#filter_tab").lookup("#pseudocolor_btn");
	private Button sepia0Btn = (Button) parent.lookup("#filter_tab").lookup("#sepia0_btn");
    private Button sepia1Btn = (Button) parent.lookup("#filter_tab").lookup("#sepia1_btn");
    private Button negativeBtn = (Button) parent.lookup("#filter_tab").lookup("#negative_btn");

    private Button laplaceBtn = (Button) parent.lookup("#filter_tab").lookup("#laplace_btn");
	private Button gaussBtn = (Button) parent.lookup("#filter_tab").lookup("#gauss_btn");
	private Button sharperBtn = (Button) parent.lookup("#filter_tab").lookup("#sharper_btn");
	private Button embossBtn = (Button) parent.lookup("#filter_tab").lookup("#emboss_btn");
	private Button highPassBtn = (Button) parent.lookup("#filter_tab").lookup("#high_pass_btn");

    private MainController controller;
    
    public FilterOPController(MainController controller){

        this.controller = controller;

        grayscaleBtn.setOnMouseClicked(this::convertToGrayscale);
		pseudocolorBtn.setOnMouseClicked(this::convertToPseudoColor);
        sepia0Btn.setOnMouseClicked(this::convertToSepia);
        sepia1Btn.setOnMouseClicked(this::convertToSepiaBlueish);
        negativeBtn.setOnMouseClicked(this::convertToNegative);

        laplaceBtn.setOnMouseClicked(e -> applyFilter(AWTKernel.LAPLACE_KERNEL));
		gaussBtn.setOnMouseClicked(e -> applyFilter(AWTKernel.GAUSS_KERNEL));
		sharperBtn.setOnMouseClicked(e -> applyFilter(AWTKernel.SHARPER_KERNEL));
		embossBtn.setOnMouseClicked(e -> applyFilter(AWTKernel.EMBOSS_KERNEL));
		highPassBtn.setOnMouseClicked(e -> applyFilter(AWTKernel.HIGH_PASS_KERNEL));

    }

    private void applyFilter(AWTKernel kernel){

        controller.updateCanvas(ConvolveOp.applyFilter(controller.getOGImage(), kernel));
        controller.applyChanges();

    }

    private void convertToGrayscale(MouseEvent e){

        if (controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(ColorFilterOp.toGrayScale(controller.getOGImage()));
        controller.setIsBWImage(true);
        controller.applyChanges();

    }

    private void convertToSepia(MouseEvent e){

        if (controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(ColorFilterOp.toSepia(controller.getOGImage()));
        controller.setIsBWImage(false);
        controller.applyChanges();

    }

    private void convertToSepiaBlueish(MouseEvent e){

        if (controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(ColorFilterOp.toBlueish(controller.getOGImage()));
        controller.setIsBWImage(false);
        controller.applyChanges();

    }

    private void convertToNegative(MouseEvent e){

        if (controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(ColorFilterOp.toNegative(controller.getOGImage()));
        controller.applyChanges();

    }

    private void convertToPseudoColor(MouseEvent e){

        if (controller.isOGImageNull()){
            return;
        }

        if (!controller.isBWImage()) {
            return;
        }

        controller.updateCanvas(ColorFilterOp.toPseudoColor(controller.getOGImage()));
        controller.setIsBWImage(false);
        controller.applyChanges();

    }

}
