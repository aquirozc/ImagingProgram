package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.data.Point;
import com.aquirozc.jimp.engine.CorrectorOp;
import com.aquirozc.jimp.init.FXApp;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class BlemishesRemoverController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private TabPane tabPane = (TabPane) parent.lookup("#tab_pane");
    private Slider zoomBar = (Slider) parent.lookup("#zoom_bar");
    private StackPane canvas = (StackPane) parent.lookup("#canvas_vb");

    private Slider radioBar = (Slider) parent.lookup("#blemishes_tab").lookup("#radio_bar");
    private Circle brush = new Circle(0, 0, 0);

    private boolean isTabActive = false;
    private double zoomFactor = 1.0;
    private double radio = radioBar.getValue();
    private double drawRadio = radio;
    private MainController controller;

    public BlemishesRemoverController(MainController controller){

        this.controller = controller;
        canvas.setOnMouseClicked(this::detectFaces);

        tabPane.getSelectionModel().selectedItemProperty().addListener(this::seeIfActive);
        radioBar.valueProperty().addListener(this::updateSelectedRadio);
        zoomBar.valueProperty().addListener(this::updateZoomLevel);
        canvas.getChildren().add(brush);
        canvas.addEventFilter(MouseEvent.ANY, this::handleMouseSelection);

        brush.getStyleClass().add("selected-area");
        brush.setRadius(0);
        brush.setOpacity(0);

    }

    public void detectFaces(MouseEvent e){

        if(!isTabActive){
            return;
        }

        if(controller.isOGImageNull()){
            return;
        }

        if(!controller.isBWImage()){
            return;
        }

        System.out.println(drawRadio);
        System.out.println(radio);

        int x = (int) (e.getX()/zoomFactor); int y = (int) (e.getY()/zoomFactor);

        controller.updateCanvas(CorrectorOp.patchImageAt(controller.getOGImage(), new Point(x, y), (int)radio));
        controller.applyChanges();

    }

    private void handleMouseSelection(MouseEvent event) {
		
		if (!isTabActive){
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
        isTabActive = tabPane.getSelectionModel().getSelectedIndex() == 7; 
        
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
