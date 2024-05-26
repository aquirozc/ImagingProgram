package com.aquirozc.jimp.engine;

import java.util.stream.IntStream;

import com.aquirozc.jimp.data.AWTKernel;

import ij.plugin.filter.Convolver;
import ij.process.ByteProcessor;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ConvolveOp {

    public static Image applyFilter(Image img, AWTKernel kernel){

        int w = (int) img.getWidth(); int h = (int) img.getHeight();
        WritableImage res = new WritableImage(w,h);

        ByteProcessor input = new ByteProcessor(w, h);
        float[] mask = kernel.getInstance();
        int maskSize = (int) Math.sqrt(mask.length);
        Convolver convolver = new Convolver();

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                input.set(i, j, img.getPixelReader().getArgb(i, j) & 0xFF);
            });
        });

        convolver.setNormalize(true);
        convolver.convolve(input, mask, maskSize, maskSize);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                int brightness = input.get(i, j);
                res.getPixelWriter().setArgb(i, j,(255 << 24) | (brightness << 16) | (brightness << 8) | brightness);
            });
        });

        return res;
    }
    
}
