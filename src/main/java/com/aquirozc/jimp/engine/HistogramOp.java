package com.aquirozc.jimp.engine;

import java.util.stream.IntStream;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class HistogramOp {

    public static int[] getHistogram(Image img){

        int[] histogram = new int[256];
        int w = (int) img.getWidth(); int h = (int) img.getHeight();

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                histogram[img.getPixelReader().getArgb(i, j) & 0xFF]++;
            });
        });

        return histogram;

    }


    public static ResultSet equalizeHistogram(int[] histogram, Image img){

        int[] currentHistogram = new int[256]; int[] cumulativeHistogram = new int[256];
        int w = (int) img.getWidth(); int h = (int) img.getHeight();
        WritableImage out = new WritableImage(w, h);

        cumulativeHistogram[0] = histogram[0];

        IntStream.range(1, 256).forEach(i -> {
                cumulativeHistogram[i] = histogram[i] + cumulativeHistogram[i-1];
        });

        double total = w*h;

        IntStream.range(0, 256).forEach(i -> {
            currentHistogram[i] = (int) Math.round((cumulativeHistogram[i]/total)*255d);
        });

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                int brightness = currentHistogram[img.getPixelReader().getArgb(i, j) & 0xFF];

                out.getPixelWriter().setArgb(i, j, (255 << 24) | (brightness << 16) | (brightness << 8) | brightness);
                
            });
        });


        return new ResultSet(getHistogram(out), out);

    }

    public static ResultSet resizeHistogram(int[] histogram, Image img, int min, int max){

        int hMin = 0; int hMax = 0;

        for (int i = 0; i < 256;i ++){

            if (histogram[i] != 0){
                hMin = i;
                break;
            }

        }

        for (int i = 255; i >= 0;i --){

            if (histogram[i] != 0){
                hMax = i;
                break;
            }

        }

        int w = (int)img.getWidth(); int h = (int)img.getHeight();
        WritableImage out = new WritableImage(w,h);

        double hhMax = hMax;
        double hhMin = hMin;

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                int brightness = (int)(((max-min)/(hhMax-hhMin)) * ((img.getPixelReader().getArgb(i, j) & 0xFF) - hhMin) + min);
                
                out.getPixelWriter().setArgb(i, j, (255 << 24) | (brightness << 16) | (brightness << 8) | brightness);

            });
        });




        return new ResultSet(getHistogram(out), out);
    }


    public static record ResultSet(int[] equalizedHistogram, Image equalizedImage){}
    
}
