package com.aquirozc.jimp.engine;

import java.util.stream.IntStream;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class SpatialOP {

    public static Image rotate90DClockwise(Image src){

        int w = (int) src.getWidth(); int h = (int) src.getHeight();
        WritableImage img = new WritableImage(h, w);
        
        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                img.getPixelWriter().setArgb(h - j - 1, i, src.getPixelReader().getArgb(i, j));

            });
        });

        return img;

    }

    public static Image rotate90DCounterClockwise(Image src){

        int w = (int) src.getWidth(); int h = (int) src.getHeight();
        WritableImage img = new WritableImage(h, w);
        
        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                img.getPixelWriter().setArgb(j, w - i - 1, src.getPixelReader().getArgb(i, j));

            });
        });

        return img;

    }

    public static Image mirrorYAxis(Image src){

        int w = (int) src.getWidth(); int h = (int) src.getHeight();
        WritableImage img = new WritableImage(w, h);
        
        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                img.getPixelWriter().setArgb(w-i-1, j ,src.getPixelReader().getArgb(i, j));

            });
        });

        return img;

    }

    public static Image mirrorXAxis(Image src){

        int w = (int) src.getWidth(); int h = (int) src.getHeight();
        WritableImage img = new WritableImage(w, h);
        
        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                img.getPixelWriter().setArgb(i, h - j - 1 ,src.getPixelReader().getArgb(i, j));

            });
        });

        return img;

    }
    
}
