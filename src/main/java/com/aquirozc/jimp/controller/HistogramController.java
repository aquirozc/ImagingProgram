package com.aquirozc.jimp.controller;

import org.controlsfx.control.RangeSlider;
import com.aquirozc.jimp.engine.HistogramOp;
import com.aquirozc.jimp.engine.HistogramOp.ResultSet;
import com.aquirozc.jimp.init.FXApp;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HistogramController{

    private Parent parent = FXApp.MAIN_EDITOR;
    private TabPane tabPane = (TabPane) parent.lookup("#tab_pane");

    
    private StackedBarChart<String, Number> histogramChart = (StackedBarChart) parent.lookup("#histogram_tab").lookup("#histogram_chart");

    private TextField rMaxTF = (TextField) parent.lookup("#histogram_tab").lookup("#max_tf");
    private TextField rMinTF = (TextField) parent.lookup("#histogram_tab").lookup("#min_tf");

    private Button trimBtn = (Button) parent.lookup("#histogram_tab").lookup("#trim_btn");
    private Button equalizeBtn = (Button) parent.lookup("#histogram_tab").lookup("#equalize_btn");

    private RangeSlider rangeSlider = (RangeSlider) parent.lookup("#histogram_tab").lookup("#range_slider");

    private int[][] histogram;
    private MainController controller;

    public HistogramController(MainController controller){

        this.controller = controller;

        rMaxTF.textProperty().addListener(this::validateNumInput);
        rMinTF.textProperty().addListener(this::validateNumInput);

        rMaxTF.setEditable(false);
        rMinTF.setEditable(false);

        rangeSlider.highValueProperty().addListener(this::onHighValueChanged);
        rangeSlider.lowValueProperty().addListener(this::onLowValueChanged);

        trimBtn.setOnMouseClicked(this::resizeHistogram);
        equalizeBtn.setOnMouseClicked(this::equalizeHistogram);

        tabPane.getSelectionModel().selectedItemProperty().addListener(this::closeAllPanes);

    }

    public void closeAllPanes(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue){

        if(tabPane.getSelectionModel().getSelectedIndex() != 1){
            return;
        }

        if(controller.isOGImageNull()){
            return;
        }

        histogram = HistogramOp.getHistogram(controller.getOGImage());
        updateHistogramChart();
        
    }

    private void equalizeHistogram(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        ResultSet res = HistogramOp.equalizeHistogram(histogram, controller.getOGImage());
        histogram = res.equalizedHistogram();

        updateHistogramChart();
        controller.updateCanvas(res.equalizedImage());
        controller.applyChanges();

    }

    private void onHighValueChanged(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        
        rMaxTF.setText((int)(newValue.doubleValue()*255) + "");

    }

    private void onLowValueChanged(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        

        rMinTF.setText((int)(newValue.doubleValue()*255) + "");

    }

    private void resizeHistogram(MouseEvent e){

        if(controller.isOGImageNull()){
            return;
        }

        int min =  Integer.parseInt("0".concat(rMinTF.getText()));
        int max =  Integer.parseInt("0".concat(rMaxTF.getText()));

        ResultSet res = HistogramOp.resizeHistogram(histogram, controller.getOGImage(),min,max);
        histogram = res.equalizedHistogram();

        updateHistogramChart();
        controller.updateCanvas(res.equalizedImage());
        controller.applyChanges();
        
    }

    private void updateHistogramChart(){
    	
    	histogramChart.getData().clear();
    	
    	for (int k = 0; k < 3; k++) {
    		Series<String, Number> series = new Series<String, Number>();
    		for (int i = 0; i < 256; i++) {
            	series.getData().add(new Data<String, Number>(i + "", Integer.valueOf(histogram[k][i])));
            }
    		histogramChart.getData().addAll(series);
    		
    	}
    	
    }

    private void validateNumInput(ObservableValue<? extends String> observable, String oldValue, String newValue)  {
		
		if (newValue.matches("\\d*")) {
	        return;
	    }
		
		((StringProperty)observable).setValue(oldValue);
		
    }

}