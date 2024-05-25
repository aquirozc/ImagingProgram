package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.data.AWTKernel;
import com.aquirozc.jimp.engine.ConvolveOp;
import com.aquirozc.jimp.init.FXApp;

import javafx.scene.Parent;
import javafx.scene.control.Button;

public class ConvolveOPController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private Button laplaceBtn = (Button) parent.lookup("#filter_tab").lookup("#laplace_btn");
	private Button gaussBtn = (Button) parent.lookup("#filter_tab").lookup("#gauss_btn");
	private Button sharperBtn = (Button) parent.lookup("#filter_tab").lookup("#sharper_btn");
	private Button embossBtn = (Button) parent.lookup("#filter_tab").lookup("#emboss_btn");
	private Button highPassBtn = (Button) parent.lookup("#filter_tab").lookup("#high_pass_btn");

    private MainController controller;
    
    public ConvolveOPController(MainController controller){

        this.controller = controller;

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
}
