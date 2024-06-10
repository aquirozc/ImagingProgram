package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.data.AWTKernel;
import com.aquirozc.jimp.data.Point;
import com.aquirozc.jimp.data.Region;
import com.aquirozc.jimp.engine.ConvolveOp;
import com.aquirozc.jimp.engine.CorrectorOp;
import com.aquirozc.jimp.engine.FaceDetector;
import com.aquirozc.jimp.init.FXApp;

import java.util.stream.IntStream;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class BeautyController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private TabPane tabPane = (TabPane) parent.lookup("#tab_pane");
    private Slider zoomBar = (Slider) parent.lookup("#zoom_bar");
    private StackPane canvas = (StackPane) parent.lookup("#canvas_vb");

    private RadioButton dragModeRB = (RadioButton) parent.lookup("#beauty_tab").lookup("#dragmode_rb");
	private RadioButton advModeRB = (RadioButton) parent.lookup("#beauty_tab").lookup("#advmode_rb");
    private Slider radioBar = (Slider) parent.lookup("#beauty_tab").lookup("#radio_bar");
    private Circle brush = new Circle(0, 0, 0);

    private double radio = radioBar.getValue();
    private double drawRadio = radio;
    private boolean isAdvModeEnabled = false;
    private boolean isDragModeEnabled = true;
    private boolean isTabActive = false;
    private double zoomFactor = 1.0;
    private MainController controller;
    


    public BeautyController(MainController controller){

        this.controller = controller;

        canvas.addEventFilter(MouseEvent.ANY, this::detectFaces);
        canvas.setOnMouseClicked(this::detectBlemishes);
        canvas.addEventFilter(MouseEvent.ANY, this::handleMouseSelection);
        canvas.getChildren().add(brush);

        dragModeRB.setSelected(isDragModeEnabled);
        advModeRB.selectedProperty().addListener(this::enableAdvancedMode);
		dragModeRB.selectedProperty().addListener(this::enableDragMode);

        radioBar.valueProperty().addListener(this::updateSelectedRadio);

        tabPane.getSelectionModel().selectedItemProperty().addListener(this::seeIfActive);
        zoomBar.valueProperty().addListener(this::updateZoomLevel);

        brush.getStyleClass().add("selected-area");
        brush.setRadius(0);
        brush.setOpacity(0);

    }

    private void enableAdvancedMode(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		
		if(!newValue) {
			return;
		}
		
		isDragModeEnabled = false;
		isAdvModeEnabled = true;
		
	}
	
	private void enableDragMode(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		
		if(!newValue) {
			return;
		}
		
		isDragModeEnabled = true;
		isAdvModeEnabled = false;

        brush.setRadius(0);
        brush.setOpacity(0);
		
	}

    public void detectFaces(MouseEvent e){

        if(!isTabActive){
            return;
        }

        if(!isDragModeEnabled){
            return;
        }

        if(!e.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
            return;
        }

        if(controller.isOGImageNull()){
            return;
        }

        if(!controller.isBWImage()){
            return;
        }

        int w = (int) controller.getOGImage().getWidth(); int h = (int) controller.getOGImage().getHeight();

        WritableImage gray = new WritableImage(controller.getOGImage().getPixelReader(),w,h);
        Image gauss = ConvolveOp.applyFilter(gray,AWTKernel.MEAN27X27_KERNEL);

        int x = (int) (e.getX()/zoomFactor); int y = (int) (e.getY()/zoomFactor);
        Region r = FaceDetector.getFaceCornerPoints(new Point(x,y), CorrectorOp.getTH(gray));

        IntStream.range(r.northW().x(), r.southE().x()).forEach(i -> {
            IntStream.range(r.northW().y(), r.southE().y()).forEach(j -> {
                gray.getPixelWriter().setArgb(i, j, gauss.getPixelReader().getArgb(i, j));
            });
        });

        //gray.getPixelWriter().setArgb(x, r.northW().y(), 0xFF00FF00);
        //gray.getPixelWriter().setArgb(r.northW().x(), y, 0xFF0000FF);
        //gray.getPixelWriter().setArgb(r.southE().x(), y, 0xFFFF0000);

        controller.updateCanvas(gray);
        controller.applyChanges();

    }

    public void detectBlemishes(MouseEvent e){

        if(!isTabActive){
            return;
        }

        if(!isAdvModeEnabled){
            return;
        }

        if(controller.isOGImageNull()){
            return;
        }

        if(!controller.isBWImage()){
            return;
        }

        int x = (int) (e.getX()/zoomFactor); int y = (int) (e.getY()/zoomFactor);

        controller.updateCanvas(CorrectorOp.patchImageAt(controller.getOGImage(), new Point(x, y), (int)radio));
        controller.applyChanges();

    }

    private void handleMouseSelection(MouseEvent event) {
		
		if (!isTabActive){
            return;
        }

        if(!isAdvModeEnabled){
            return;
        }

        if (controller.isOGImageNull()){
            return;
        }

        if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED)){
            brush.setRadius(drawRadio);
            brush.setOpacity(1);
        }

        if(event.getEventType().equals(MouseEvent.MOUSE_EXITED)){
            brush.setRadius(0);
            brush.setOpacity(0);
        }

        if(event.getEventType().equals(MouseEvent.MOUSE_MOVED)){
            brush.setTranslateX(event.getX() - drawRadio);
            brush.setTranslateY(event.getY() - drawRadio);
        }

	}

    private void seeIfActive(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue){

        isTabActive = tabPane.getSelectionModel().getSelectedIndex() == 4; 
        
        if (!isTabActive){
            brush.setRadius(0);
            brush.setOpacity(0);
        }
    }

    private void updateSelectedRadio(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		
        radio = newValue.doubleValue();
        drawRadio = radio *zoomFactor/2;
		
	}

    private void updateZoomLevel(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		
	    zoomFactor = (double) newValue / 100d;
        drawRadio = radio * zoomFactor/2;
        brush.setRadius(drawRadio);
		
	}
}
