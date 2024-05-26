package com.aquirozc.jimp.controller;

import java.util.stream.IntStream;

import com.aquirozc.jimp.engine.GrayScaleOp;
import com.aquirozc.jimp.engine.HistogramOp;
import com.aquirozc.jimp.engine.HistogramOp.ResultSet;
import com.aquirozc.jimp.init.FXApp;
import com.aquirozc.jimp.strings.Strings;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HistogramController{

    private Parent parent = FXApp.MAIN_EDITOR;
    private TabPane tabPane = (TabPane) parent.lookup("#tab_pane");

    private TitledPane equalizePane = ((Accordion)parent.lookup("#histogram_tab")).getPanes().get(0);
    private Button equalizeBtn = (Button) equalizePane.lookup("#equalize_btn");
    private BarChart<String ,Integer> equalizeChart = (BarChart) equalizePane.lookup("#equalize_chart");

    private TitledPane resizePane = ((Accordion)parent.lookup("#histogram_tab")).getPanes().get(1);
    private Button resizeBtn = (Button) resizePane.lookup("#resize_btn");
    private BarChart<String ,Integer> resizeChart = (BarChart) resizePane.lookup("#resize_chart");
    private TextField rMaxTF = (TextField) resizePane.lookup("#max_tf");
    private TextField rMinTF = (TextField) resizePane.lookup("#min_tf");

    private TitledPane offsetPane = ((Accordion)parent.lookup("#histogram_tab")).getPanes().get(2);
    private Button offsetBtn = (Button) offsetPane.lookup("#offset_btn");
    private BarChart<String ,Integer> offsetChart = (BarChart) offsetPane.lookup("#offset_chart");
    private TextField offsetTF = (TextField) offsetPane.lookup("#offset_tf");

    private Alert boundsError = new Alert(AlertType.ERROR,Strings.OUTOFBOUNDS_ERROR);

    private int[] histogram;
    private MainController controller;

    public HistogramController(MainController controller){

        this.controller = controller;

        tabPane.getSelectionModel().selectedItemProperty().addListener(this::closeAllPanes);
        
        equalizePane.expandedProperty().addListener(this::undoUnsavedChanges);
        equalizeBtn.setOnMouseClicked(this::equalizeHistogram);

        resizePane.expandedProperty().addListener(this::undoUnsavedChanges);
        resizeBtn.setOnMouseClicked(this::resizeHistogram);

        rMaxTF.textProperty().addListener(this::validateNumInput);
        rMinTF.textProperty().addListener(this::validateNumInput);

        offsetPane.expandedProperty().addListener(this::undoUnsavedChanges);
        offsetBtn.setOnMouseClicked(this::offsetHistogram);
        
        offsetTF.textProperty().addListener(this::validateNegativeNumInput);

    }

    private void closeAllPanes(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue){

        if(tabPane.getSelectionModel().getSelectedIndex() != 2){return;}

        equalizePane.setExpanded(false);
        resizePane.setExpanded(false);
        offsetPane.setExpanded(false);
        
    }

    private void launchEqualizePane(boolean isOpen){

        if(!isOpen){
            return;
        }

        if(controller.isOGImageNull()){
            return;
        }

        if(!controller.isBWImage()){
            equalizePane.setExpanded(false);
            return;
        }

        histogram = HistogramOp.getHistogram(controller.getOGImage());
        updateEqualizeChart();

    }

    private void launchResizePane(boolean isOpen){

        if(!isOpen){
            return;
        }

        if(controller.isOGImageNull()){
            return;
        }

        if(!controller.isBWImage()){
            return;
        }


        histogram = HistogramOp.getHistogram(controller.getOGImage());
        updateResizeChart();

    }

    private void launchOffsetPane(boolean isOpen){

        if(!isOpen){
            return;
        }

        if(controller.isOGImageNull()){
            return;
        }

        if(!controller.isBWImage()){
            return;
        }

        histogram = HistogramOp.getHistogram(controller.getOGImage());
        updateOffsetChart();

    }

    private void equalizeHistogram(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        ResultSet res = HistogramOp.equalizeHistogram(histogram, controller.getOGImage());
        histogram = res.equalizedHistogram();

        updateEqualizeChart();
        controller.updateCanvas(res.equalizedImage());
        controller.applyChanges();
        
    }

    private void resizeHistogram(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        int min =  Integer.parseInt("0".concat(rMinTF.getText()));
        int max =  Integer.parseInt("0".concat(rMaxTF.getText()));

        if (min < 0 || max > 255){
            boundsError.showAndWait();
        }

        if (!(min<max)){
            throw new IllegalArgumentException();
        }

        ResultSet res = HistogramOp.resizeHistogram(histogram, controller.getOGImage(),min,max);
        histogram = res.equalizedHistogram();

        updateResizeChart();
        controller.updateCanvas(res.equalizedImage());
        controller.applyChanges();
        
    }

    private void offsetHistogram(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        String input = "0".concat(offsetTF.getText());
        input = input .matches("0-\\d*") ? input.substring(1) : input;
        int offset =  Integer.parseInt(input);

        Image res = GrayScaleOp.adjustBrightness(controller.getOGImage(), offset);
        histogram = HistogramOp.getHistogram(res);

        updateOffsetChart();
        controller.updateCanvas(res);
        controller.applyChanges();
        
    }

    private void updateEqualizeChart(){

       Series<String,Integer> series = new Series<>();

       IntStream.range(0, 256).forEach(i ->{
            series.getData().add(new Data<String,Integer>(i + "", histogram[i]));
       });

       equalizeChart.getData().clear();
       equalizeChart.getData().add(series);

    }

    private void updateResizeChart(){

        Series<String,Integer> series = new Series<>();
 
        IntStream.range(0, 256).forEach(i ->{
             series.getData().add(new Data<String,Integer>(i + "", histogram[i]));
        });
 
        resizeChart.getData().clear();
        resizeChart.getData().add(series);
 
    }

    private void updateOffsetChart(){

        Series<String,Integer> series = new Series<>();
 
        IntStream.range(0, 256).forEach(i ->{
             series.getData().add(new Data<String,Integer>(i + "", histogram[i]));
        });
 
        offsetChart.getData().clear();
        offsetChart.getData().add(series);
 
    }

    public void onRefresh(){
        launchEqualizePane(equalizePane.isExpanded());
        launchResizePane(resizePane.isExpanded());
        launchOffsetPane(offsetPane.isExpanded());
    }

    private void undoUnsavedChanges(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
        controller.refreshCanvas();
    }

    private void validateNumInput(ObservableValue<? extends String> observable, String oldValue, String newValue)  {
		
		if (newValue.matches("\\d*")) {
	        return;
	    }
		
		((StringProperty)observable).setValue(oldValue);
		
	}

    private void validateNegativeNumInput(ObservableValue<? extends String> observable, String oldValue, String newValue)  {
		
		if (observable.getValue().matches("(\\d*|-\\d*)|")) {
	        return;
	    }
		
		((StringProperty)observable).setValue(oldValue);
		
	}

}