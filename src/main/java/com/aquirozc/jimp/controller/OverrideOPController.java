package com.aquirozc.jimp.controller;

import com.aquirozc.jimp.engine.OverrideOp;
import com.aquirozc.jimp.init.FXApp;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane; 
import javafx.scene.shape.Rectangle;

public class OverrideOPController {

    private Parent parent = FXApp.MAIN_EDITOR;

    private TabPane tabPane = (TabPane) parent.lookup("#tab_pane");
    private StackPane canvas = (StackPane) parent.lookup("#canvas_vb");
    private Slider zoomBar = (Slider) parent.lookup("#zoom_bar");
    private ImageView targetVW = (ImageView) parent.lookup("#target_vw");

    private RadioButton dragModeRB = (RadioButton) parent.lookup("#override_tab").lookup("#dragmode_rb");
	private RadioButton advModeRB = (RadioButton) parent.lookup("#override_tab").lookup("#advmode_rb");
	private TextField x0TF = (TextField) parent.lookup("#override_tab").lookup("#x0_tf");
	private TextField y0TF = (TextField) parent.lookup("#override_tab").lookup("#y0_tf");
	private TextField x1TF = (TextField) parent.lookup("#override_tab").lookup("#x1_tf");
	private TextField y1TF = (TextField) parent.lookup("#override_tab").lookup("#y1_tf");

    private Button replaceBtn = (Button) parent.lookup("#override_tab").lookup("#replace_btn");
	
	private Rectangle selectedArea = new Rectangle(0,0,0,0);
    private boolean isAdvModeEnabled = false;
    private boolean isDragModeEnabled = true;
    private boolean isTabActive = false;
    private double zoomFactor = 1;

    private MainController controller;

    public OverrideOPController(MainController controller){

        this.controller = controller;

        tabPane.getSelectionModel().selectedItemProperty().addListener(this::closeAllPanes);
        canvas.getChildren().add(selectedArea);
        targetVW.boundsInParentProperty().addListener(this::updateCanvasSize);

        zoomBar.valueProperty().addListener(this::updateZoomLevel);
        canvas.addEventFilter(MouseEvent.ANY, this::handleMouseSelection);

        x0TF.textProperty().addListener(this::validateNumInput);
		y0TF.textProperty().addListener(this::validateNumInput);
		x1TF.textProperty().addListener(this::validateNumInput);
		y1TF.textProperty().addListener(this::validateNumInput);

		selectedArea.getStyleClass().add("selected-area");

        dragModeRB.setSelected(isDragModeEnabled);
        advModeRB.selectedProperty().addListener(this::enableAdvancedMode);
		dragModeRB.selectedProperty().addListener(this::enableDragMode);
        replaceBtn.setOnMouseClicked(this::replaceArea);
		
		this.toggleTextInputs(false);
    }

    private void closeAllPanes(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue){
        isTabActive = tabPane.getSelectionModel().getSelectedIndex() == 5;   

        if (!isTabActive){
            resetInput();
            resetSelection();
        }

    }

    private void enableAdvancedMode(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		
		if(!newValue) {
			return;
		}
		
		isDragModeEnabled = false;
		isAdvModeEnabled = true;
		
		resetSelection();
		toggleTextInputs(true);
		
	}
	
	private void enableDragMode(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		
		if(!newValue) {
			return;
		}
		
		isDragModeEnabled = true;
		isAdvModeEnabled = false;
		
		resetInput();
        resetSelection();
		toggleTextInputs(false);
		
	}

    private void handleMouseSelection(MouseEvent event) {
		
		if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            selectedArea.setTranslateX(event.getX());
            selectedArea.setTranslateY(event.getY());
            selectedArea.setWidth(0);
            selectedArea.setHeight(0);
         }

        if(event.getEventType() == MouseEvent.MOUSE_DRAGGED  && (isDragModeEnabled && isTabActive)){

        	int width = (int) (event.getX() - selectedArea.getTranslateX());
        	int height = (int) (event.getY() - selectedArea.getTranslateY());
        	
        	
            selectedArea.setWidth(Math.min(width, canvas.getMaxWidth() - (selectedArea.getTranslateX())));
            selectedArea.setHeight(Math.min(height, canvas.getMaxHeight() - (selectedArea.getTranslateY())));   
        }
		
	}

    private void resetSelection() {
		selectedArea.setWidth(0);
		selectedArea.setHeight(0);
	};
	
	private void resetInput() {
		x0TF.setText("");
		y0TF.setText("");
		x1TF.setText("");
		y1TF.setText("");
	}

    private void replaceArea(MouseEvent event) {

        if(controller.isOGImageNull()){
            return;
        }
		
		int x0 = (int) (selectedArea.getTranslateX() / zoomFactor);
		int x1 = (int) (x0 + selectedArea.getWidth() / zoomFactor);
		
		int y0 = (int) (selectedArea.getTranslateY() / zoomFactor);
		int y1 = (int) (y0 + selectedArea.getHeight() / zoomFactor);
		
		if (isAdvModeEnabled) {
			x0 = Integer.parseInt("0".concat(x0TF.getText()));
			y0 = Integer.parseInt("0".concat(y0TF.getText()));
			x1 = Integer.parseInt("0".concat(x1TF.getText()));
			y1 = Integer.parseInt("0".concat(y1TF.getText()));
		}
		
		try {
			controller.updateCanvas(OverrideOp.overrideRegionRandom(controller.getOGImage(), x0, x1, y0, y1));
            controller.applyChanges();
		} catch (Exception e) {
			new Alert(AlertType.ERROR,"Los limites de la selección deben estar dentro de la imagen").showAndWait();
		}
		
		resetSelection();
		resetInput();
		
	}

    public void onRefresh(){
        resetInput();
        resetSelection();
    }

    private void toggleTextInputs(boolean v) {
		
		x0TF.setEditable(v);
		y0TF.setEditable(v);
		x1TF.setEditable(v);
		y1TF.setEditable(v);
		
	}
	
	private void validateNumInput(ObservableValue<? extends String> observable, String oldValue, String newValue)  {
		
		if (newValue.matches("\\d*")) {
	        return;
	    }
		
		((StringProperty)observable).setValue(oldValue);
		
		
	}

    private void updateCanvasSize(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
		canvas.setMaxWidth(newValue.getWidth());
        canvas.setMaxHeight(newValue.getHeight());
	}

    private void updateZoomLevel(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		
		if (controller.isOGImageNull()) {
	        return;
	    }

	    double newFactor = (double) newValue / 100d;
	    double zoomDelta = newFactor / zoomFactor;

	    selectedArea.setTranslateX(selectedArea.getTranslateX() * zoomDelta);
	    selectedArea.setTranslateY(selectedArea.getTranslateY() * zoomDelta);
	    selectedArea.setWidth(selectedArea.getWidth() * zoomDelta);
	    selectedArea.setHeight(selectedArea.getHeight() * zoomDelta);

	    zoomFactor = newFactor;
		
	}
    
}
