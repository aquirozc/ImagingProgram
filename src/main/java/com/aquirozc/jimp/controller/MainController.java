package com.aquirozc.jimp.controller;


import com.aquirozc.jimp.engine.ColorOp;
import com.aquirozc.jimp.helper.FXImageIO;
import com.aquirozc.jimp.init.FXApp;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class MainController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private MenuItem openBtn = ((MenuBar)parent.lookup("#top_menu")).getMenus().get(0).getItems().get(0);
    private Slider zoomBar = (Slider) parent.lookup("#zoom_bar");
    private ImageView targetVW = (ImageView) parent.lookup("#target_vw");

    private Image ogImage;
    private FXImageIO imgHelper;

    private GrayScaleOPController grayOPController = new GrayScaleOPController(this);

    public MainController (Stage stage){

        
        openBtn.setOnAction(this::openImageFromDisk);
        zoomBar.valueProperty().addListener(this::updateZoomLevel);

        imgHelper = new FXImageIO(stage);
        targetVW = (ImageView) parent.lookup("#target_vw");

    }

    private void openImageFromDisk(ActionEvent e){

        ogImage = imgHelper.readImage();

        if(ogImage == null){return;}

        ogImage = ColorOp.toGrayScale(ogImage);

        targetVW.setImage(ogImage);
        targetVW.setFitHeight(ogImage.getHeight());
        targetVW.setFitWidth(ogImage.getWidth());

        zoomBar.setValue(100);

    }

    public void applyChanges(){

        if(isOGImageNull()){
            return;
        }

        ogImage = targetVW.getImage();

    }

    public Image getOGImage(){
        return ogImage;
    }

    public void refreshCanvas(){

        grayOPController.resetSliders();

        if(isOGImageNull()){
            return;
        }

        updateCanvas(ogImage);

    }

    public void updateCanvas(Image img){
        targetVW.setImage(img);
    }

    public boolean isOGImageNull(){
        return ogImage == null;
    }

    public boolean isBWImage(){
        return true;
    }

    private void updateZoomLevel(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		
		if (ogImage == null) {
	        return;
	    }

	    double newFactor = (double) newValue / 100d;

	    targetVW.setFitHeight(ogImage.getHeight() * newFactor);
	    targetVW.setFitWidth(ogImage.getWidth() * newFactor);
		
	}
    
}
