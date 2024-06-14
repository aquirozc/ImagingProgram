package com.aquirozc.jimp.engine;

import java.util.stream.IntStream;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class HistogramOp {

    public static int[][] getHistogram(Image img){

        int[][] histogram = new int[3][256];
        int w = (int) img.getWidth(); int h = (int) img.getHeight();

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
            	int color = img.getPixelReader().getArgb(i, j);
            	histogram[0][(color & 0xFF0000) >> 16]++;
            	histogram[1][(color & 0xFF00) >> 8]++;
            	histogram[2][color & 0xFF]++;
            });
        });

        return histogram;

    }


    public static ResultSet equalizeHistogram(int[][] histogram, Image img){

        int[][] currentHistogram = new int[3][256]; int[][] cumulativeHistogram = new int[3][256];
        int w = (int) img.getWidth(); int h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);

        cumulativeHistogram[0] = histogram[0];

        IntStream.range(0, 3).forEach(k -> {
        	IntStream.range(1, 256).forEach(i -> {
                cumulativeHistogram[k][i] = histogram[k][i] + cumulativeHistogram[k][i-1];
        	});
        });

        double total = w*h;
        
        IntStream.range(0, 3).forEach(k -> {
        	IntStream.range(1, 256).forEach(i -> {
        		currentHistogram[k][i] = (int) Math.round((cumulativeHistogram[k][i]/total)*255d);
        	});
        });

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
            	
            	int color = img.getPixelReader().getArgb(i, j);
            	int r = currentHistogram[0][(color & 0xFF0000) >> 16];
            	int g = currentHistogram[1][(color & 0xFF00) >> 8];
            	int b = currentHistogram[2][color & 0xFF];

                out.getPixelWriter().setArgb(i, j, (255 << 24) | (r << 16) | (g << 8) | b);
                
            });
        });

        return new ResultSet(getHistogram(out), out);

    }

    public static ResultSet resizeHistogram(int[][] histogram, Image img, int min, int max){

        int[] hMin = new int[3]; int[] hMax = new int[3];

        for(int k = 0; k < 3;k++) {
        	for (int i = 0; i < 256;i ++){

                if (histogram[k][i] != 0){
                    hMin[k] = i;
                    break;
                }

            }
        }
        
        for(int k = 0; k < 3;k++) {
        	for (int i = 255; i >= 0;i --){
                if (histogram[k][i] != 0){
                    hMax[k] = i;
                    break;
                }
            }
        }

        

        int w = (int)img.getWidth(); int h = (int)img.getHeight();
        WritableImage out = new WritableImage(w,h);

        double rMax = hMax[0]; double rMin = hMin[0];
        double gMax = hMax[1]; double gMin = hMin[1];
        double bMax = hMax[2]; double bMin = hMin[2];

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

            	int color = img.getPixelReader().getArgb(i, j);
    			
            	int red = (color & 0xFF0000) >> 16;
            	int green = (color & 0xFF00) >> 8;
            	int blue = color & 0xFF;
            	
            	red = (int)(((max-min)/(rMax-rMin)) * (red - rMin) + min);
            	green = (int)(((max-min)/(gMax-gMin)) * (green - gMin) + min);
            	blue = (int)(((max-min)/(bMax-bMin)) * (blue - bMin) + min);
            	
            	out.getPixelWriter().setArgb(i, j, (255 << 24) | (red << 16) | (green << 8) | blue);

            });
        });

        return new ResultSet(getHistogram(out), out);
    }


    public static record ResultSet(int[][] equalizedHistogram, Image equalizedImage){}
    
}
