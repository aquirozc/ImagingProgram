package com.aquirozc.jimp.engine;

import java.util.stream.IntStream;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ColorOp {

    public static WritableImage toGrayScale(Image src) {
		
		int w = (int) src.getWidth(); int h = (int) src.getHeight();
		
		WritableImage image = new WritableImage(src.getPixelReader(), w, h);
		
		IntStream.range(0, w).forEach(i -> {
			IntStream.range(0, h).forEach(j -> {
				
				int color = src.getPixelReader().getArgb(i, j);
				
				// Demasiado lento
				//int brightness = (int) IntStream.range(0, 3).map(n -> (color & 0xFF << 8*n) >> 8*n).average().getAsDouble();
				
				int red = (color & 0xFF0000) >> 16;
				int green = (color & 0xFF00) >> 8;
				int blue = color & 0xFF;
				
				int brightness = (int)((red + green + blue)/3d);
				
				image.getPixelWriter().setArgb(i, j, (255 << 24) | (brightness << 16) | (brightness << 8) | brightness);
				
			});
		});
		
		return image;		
	}
    
}
