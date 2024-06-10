package com.aquirozc.jimp.engine;

import java.util.stream.IntStream;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ToneOp {

    public static Image adjustBrightnessAndContrast(Image src, int alfa, int beta, double gamma){

        int w = (int) src.getWidth(); int h = (int) src.getHeight();
        WritableImage img = new WritableImage(w, h);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {

                int rgb = src.getPixelReader().getArgb(i, j);

                int r = ((int)(gamma * (rgb >> 16 & 0xFF)) + beta) + alfa;
                int g = ((int)(gamma * (rgb >> 8 & 0xFF)) + beta) + alfa;
                int b = ((int)(gamma * (rgb & 0xFF)) + beta) + alfa;

                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);

                img.getPixelWriter().setArgb(i, j, (255 << 24) | (r << 16) | (g << 8) | b);

            });
        });
        
        return img;

    }
    
}
