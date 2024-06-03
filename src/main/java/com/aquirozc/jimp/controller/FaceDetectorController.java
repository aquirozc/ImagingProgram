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
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class FaceDetectorController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private TabPane tabPane = (TabPane) parent.lookup("#tab_pane");
    private Slider zoomBar = (Slider) parent.lookup("#zoom_bar");
    private StackPane canvas = (StackPane) parent.lookup("#canvas_vb");

    private boolean isTabActive = false;
    private double zoomFactor = 1.0;
    private MainController controller;

    public FaceDetectorController(MainController controller){

        this.controller = controller;
        canvas.addEventFilter(MouseEvent.ANY, this::detectFaces);

        tabPane.getSelectionModel().selectedItemProperty().addListener(this::seeIfActive);
        zoomBar.valueProperty().addListener(this::updateZoomLevel);

    }

    public void detectFaces(MouseEvent e){

        if(!isTabActive){
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

    private void seeIfActive(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue){
        isTabActive = tabPane.getSelectionModel().getSelectedIndex() == 6; 
    }

    private void updateZoomLevel(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		
	    zoomFactor = (double) newValue / 100d;
		
	}
    
}
