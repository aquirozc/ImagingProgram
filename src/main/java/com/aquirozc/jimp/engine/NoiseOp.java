package com.aquirozc.jimp.engine;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import ij.process.ColorProcessor;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class NoiseOp {

    private static ThreadLocalRandom rn = ThreadLocalRandom.current();
    private static final int PEPPER_NOISE = 0;
    private static final int SALT_NOISE = -1;

    public static Image addNoise(Image img, int sigma){

        int w = (int) img.getWidth(); int h = (int) img.getHeight();
        WritableImage res = new WritableImage(w,h);

        ColorProcessor input = new ColorProcessor(w, h);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                input.set(i, j, img.getPixelReader().getArgb(i, j) & 0xFFFFFF);
            });
        });
  
        input.noise(sigma);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                int brightness = input.get(i, j);
                res.getPixelWriter().setArgb(i, j,(255 << 24) | brightness);
            });
        });

        return res;
    }

    public static Image addSaltPepperNoise(Image img){

        int w = (int) img.getWidth(); int h = (int) img.getHeight();
        WritableImage res = new WritableImage(img.getPixelReader(),w,h);
        int limit = (int) (w * h * 2.5d / 100d);

        IntStream.range(0, limit).forEach(i -> {

            int x1 = rn.nextInt(w);
            int y1 = rn.nextInt(h);
            int x2 = rn.nextInt(w);
            int y2 = rn.nextInt(h);

            res.getPixelWriter().setArgb(x1, y1, SALT_NOISE);
            res.getPixelWriter().setArgb(x2, y2, PEPPER_NOISE);

        });

        return res;
    }

    public static Image  reduceNoise(Image img){

        int w = (int) img.getWidth(); int h = (int) img.getHeight();
        WritableImage res = new WritableImage(w,h);

        ColorProcessor input = new ColorProcessor(w, h);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                input.set(i, j, img.getPixelReader().getArgb(i, j) & 0xFFFFFF);
            });
        });
  
        input.blurGaussian(1.5);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                int brightness = input.get(i, j);
                res.getPixelWriter().setArgb(i, j,(255 << 24) | brightness);
            });
        });

        return res;

    }
    
}
