package com.aquirozc.jimp.engine;

import java.util.stream.IntStream;

import com.aquirozc.jimp.data.AWTKernel;

import ij.process.ColorProcessor;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ConvolveOp {

    public static Image applyFilter(Image img, AWTKernel kernel){

        int w = (int) img.getWidth(); int h = (int) img.getHeight();
        WritableImage res = new WritableImage(w,h);

        ColorProcessor input = new ColorProcessor(w, h);
        float[] mask = kernel.getInstance();
        int maskSize = (int) Math.sqrt(mask.length);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                input.set(i, j, img.getPixelReader().getArgb(i, j) & 0xFFFFFF);
            });
        });

        input.convolve(mask, maskSize, maskSize);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                int brightness = input.get(i, j);
                res.getPixelWriter().setArgb(i, j,(255 << 24) | brightness);
            });
        });

        return res;
    }
    
}
