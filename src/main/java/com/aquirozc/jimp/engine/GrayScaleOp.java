package com.aquirozc.jimp.engine;

import java.util.stream.IntStream;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class GrayScaleOp {

    public static Image adjustBrightness(Image src, int beta){

        int w = (int) src.getWidth(); int h = (int) src.getHeight();
        WritableImage img = new WritableImage(w, h);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                int brightness = (src.getPixelReader().getArgb(i, j) & 0x0000FF) + beta;

                brightness = brightness < 0 ? 0 : (brightness > 255 ? 255 : brightness);

                img.getPixelWriter().setArgb(i, j, (255 << 24) | (brightness << 16) | (brightness << 8) | brightness);

            });
        });
        

        return img;

    }

    public static Image adjustContrast(Image src, double gamma, double beta){

        int w = (int) src.getWidth(); int h = (int) src.getHeight();

        WritableImage img = new WritableImage(w, h);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                int brightness = (int) (gamma * ((src.getPixelReader().getArgb(i, j)) & 0x0000FF) + beta);

                brightness = brightness < 0 ? 0 : (brightness > 255 ? 255 : brightness);

                img.getPixelWriter().setArgb(i, j, (255 << 24) | (brightness << 16) | (brightness << 8) | brightness);

            });
        });

        return img;

    }

    public static Image makeNegative(Image src){

        int w = (int) src.getWidth(); int h = (int) src.getHeight();

        WritableImage img = new WritableImage(w, h);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                int brightness = 255 - (src.getPixelReader().getArgb(i, j) & 0x0000FF);

                img.getPixelWriter().setArgb(i, j, (255 << 24) | (brightness << 16) | (brightness << 8) | brightness);

            });
        });

        return img;

    }
    
}
