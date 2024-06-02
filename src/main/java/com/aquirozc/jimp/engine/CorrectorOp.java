package com.aquirozc.jimp.engine;

import java.util.stream.IntStream;

import com.aquirozc.jimp.data.Point;

import ij.process.ByteProcessor;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class CorrectorOp {

    public static Image patchImageAt(Image img, Point point,int radius){

        int w = (int) img.getWidth(); int h = (int) img.getHeight();
        WritableImage res = new WritableImage(img.getPixelReader(),w,h);
        Image mask = getMask(res);

        for (int i = 0; i < radius; i++) {
            for (int j = 0; j < radius; j++) {

                int xx = point.x() + (i - radius/2);
                int yy = point.y() + (j - radius/2);

                res.getPixelWriter().setArgb(xx, yy, mask.getPixelReader().getArgb(xx,yy));

            }
        }

        return res;

    }

    private static Image getMask(Image img){

        int w = (int) img.getWidth(); int h = (int) img.getHeight();
        WritableImage res = new WritableImage(w,h);

        ByteProcessor input = new ByteProcessor(w, h);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                input.set(i, j, img.getPixelReader().getArgb(i, j) & 0xFF);
            });
        });
  
        //input.erode();
        //input.dilate();
        input.noise(9);
        input.blurGaussian(3);

        IntStream.range(0, w).forEach(i -> {
            IntStream.range(0, h).forEach(j -> {
                int brightness = input.get(i, j);
                res.getPixelWriter().setArgb(i, j,(255 << 24) | (brightness << 16) | (brightness << 8) | brightness);
            });
        });

        return res;
    }
    
}
