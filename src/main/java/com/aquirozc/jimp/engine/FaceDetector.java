package com.aquirozc.jimp.engine;

import com.aquirozc.jimp.util.StatHelper;
import com.aquirozc.jimp.data.Point;
import com.aquirozc.jimp.data.Region;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class FaceDetector {

    public static final int BRIGHTNESS_THRESHOLD = 66;
    public static final int SAMPLE_RATE = 2;

    public static Region getFaceCornerPoints(Point center, Image img){

        int x = center.x();
        int y = center.y();

        int intensity = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                intensity += (img.getPixelReader().getArgb(x + (i - 1), y + (j - 1)) & 0xFF)/9;
            }
        }

        List<Integer> xOffSample = new ArrayList<>();

        for (int k = 0; k <= SAMPLE_RATE; k++) {
            for (int i = x + (k-1); i >= 0; i--){
                if (Math.abs(intensity - img.getPixelReader().getArgb(i, y) & 0xFF) <= BRIGHTNESS_THRESHOLD){
                    xOffSample.add(i);
                }
            }
        }   
        
        int xOff = (int) (StatHelper.getAverage(xOffSample) - 2 * StatHelper.getStandardDeviation(xOffSample));

        List<Integer> xTopSample = new ArrayList<>();

        for (int k = 0; k <= SAMPLE_RATE; k++) {
            for (int i = x + (k-1); i < img.getWidth(); i++){
                if (Math.abs(intensity - img.getPixelReader().getArgb(i, y) & 0xFF) <= BRIGHTNESS_THRESHOLD){
                    xTopSample.add(i);
                }
            }
        }   
        
        int xTop = (int) (StatHelper.getAverage(xTopSample) + 2 * StatHelper.getStandardDeviation(xTopSample));

        List<Integer> yOffSample = new ArrayList<>();

        for (int k = 0; k <= SAMPLE_RATE; k++) {
            for (int i = y + (k-1); i >= 0; i--){
                if (Math.abs(intensity - img.getPixelReader().getArgb(x, i) & 0xFF) <= BRIGHTNESS_THRESHOLD){
                    yOffSample.add(i);
                }
            }
        }   
        
        int yOff = (int) (StatHelper.getAverage(yOffSample) - 2 * StatHelper.getStandardDeviation(yOffSample));

        int yTop = y + (y-yOff);
        
        return new Region(new Point(xOff, yOff), new Point(xTop,yTop));

    }
    
}
