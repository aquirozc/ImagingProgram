package com.aquirozc.jimp.engine;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class OverrideOp {

    private static Random random = ThreadLocalRandom.current();

    public static Image overrideRegionRandom(Image src, int x0, int x1, int y1, int y2) throws IndexOutOfBoundsException {

		WritableImage img = new WritableImage(src.getPixelReader(), (int) src.getWidth(), (int) src.getHeight());
		
		IntStream.rangeClosed(x0, x1).forEach(i -> {
			IntStream.rangeClosed(y1, y2).forEach(j -> {
				
				int rgba =  (255 << 24) | ( random.nextInt(256) << 16) | (random.nextInt(256) << 8) | random.nextInt(256);
			
				img.getPixelWriter().setArgb(i, j, rgba);
				
			});
		});

        return img;
		
		
	}
    
}
