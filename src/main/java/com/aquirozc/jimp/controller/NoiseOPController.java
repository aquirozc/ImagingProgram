package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.engine.NoiseOp;
import com.aquirozc.jimp.init.FXApp;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class NoiseOPController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private Button laplaceBtn = (Button) parent.lookup("#noise_tab").lookup("#laplace_btn");
	private Button gaussBtn = (Button) parent.lookup("#noise_tab").lookup("#gauss_btn");
	private Button sharperBtn = (Button) parent.lookup("#noise_tab").lookup("#sharper_btn");

    private MainController controller;

    public NoiseOPController(MainController controller){

        this.controller = controller;

        this.laplaceBtn.setOnMouseClicked(e -> this.addSigmaNoise(25));
        this.gaussBtn.setOnMouseClicked(e -> this.addSigmaNoise(75));
        this.sharperBtn.setOnMouseClicked(this::addSaltPepperNoise);

    }

    public void addSaltPepperNoise(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        if(!controller.isBWImage()){
            return;
        }

        controller.updateCanvas(NoiseOp.addSaltPepperNoise(controller.getOGImage()));
        controller.applyChanges();

    }

    public void addSigmaNoise(int sigma){

        if(controller.isOGImageNull()){
            return;
        }

        controller.updateCanvas(NoiseOp.addNoise(controller.getOGImage(),sigma));
        controller.applyChanges();

    }
    
}
