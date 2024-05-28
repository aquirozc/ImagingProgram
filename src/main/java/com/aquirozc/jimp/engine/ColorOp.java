package com.aquirozc.jimp.engine;

import java.util.stream.IntStream;

import com.aquirozc.jimp.util.ColorConverter;

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

	public static WritableImage toSepia(Image src) {
		
		int w = (int) src.getWidth(); int h = (int) src.getHeight();
		
		WritableImage image = new WritableImage(src.getPixelReader(), w, h);
		
		IntStream.range(0, w).forEach(i -> {
			IntStream.range(0, h).forEach(j -> {
				
				int color = src.getPixelReader().getArgb(i, j);

				int red = (color & 0xFF0000) >> 16;
				int green = (color & 0xFF00) >> 8;
				int blue = color & 0xFF;

				int r = (int) ((red * .393) + (green *.769) + (blue * .189));
				int g= (int) ((red * .349) + (green *.686) + (blue* .168));
				int b = (int) ((red * .272) + (green *.534) + (blue * .131));

				r = r > 255 ? 255 : r;
				g = g > 255 ? 255 : g;
				b = b > 255 ? 255 : b;
				
				image.getPixelWriter().setArgb(i, j, (255 << 24) | (r << 16) | (g << 8) | b);
				
			});
		});
		
		return image;	

	}

	public static WritableImage toBlueish(Image src) {
		
		int w = (int) src.getWidth(); int h = (int) src.getHeight();
		
		WritableImage image = new WritableImage(src.getPixelReader(), w, h);
		
		IntStream.range(0, w).forEach(i -> {
			IntStream.range(0, h).forEach(j -> {
				
				int color = src.getPixelReader().getArgb(i, j);

				int red = (color & 0xFF0000) >> 16;
				int green = (color & 0xFF00) >> 8;
				int blue = color & 0xFF;

				int r = (int) ((red * .393) + (green *.769) + (blue * .189));
				int g= (int) ((red * .349) + (green *.686) + (blue* .168));
				int b = (int) ((red * .272) + (green *.534) + (blue * .131));

				r *= 0.85;
				g *= 0.85;
				b *= 1.45;

				r = r > 255 ? 255 : r;
				g = g > 255 ? 255 : g;
				b = b > 255 ? 255 : b;
				
				image.getPixelWriter().setArgb(i, j, (255 << 24) | (r << 16) | (g << 8) | b);
				
			});
		});
		
		return image;		
	}

	public static WritableImage toPinkish(Image src) {
		
		int w = (int) src.getWidth(); int h = (int) src.getHeight();
		
		WritableImage image = new WritableImage(src.getPixelReader(), w, h);
		
		IntStream.range(0, w).forEach(i -> {
			IntStream.range(0, h).forEach(j -> {
				
				int color = src.getPixelReader().getArgb(i, j);

				int red = (color & 0xFF0000) >> 16;
				int green = (color & 0xFF00) >> 8;
				int blue = color & 0xFF;

				int r = (int) ((red * .393) + (green *.769) + (blue * .189));
				int g= (int) ((red * .349) + (green *.686) + (blue* .168));
				int b = (int) ((red * .272) + (green *.534) + (blue * .131));

				r *= 1.2;
				g *= 0.8;
				b *= 1.2;

				r = r > 255 ? 255 : r;
				g = g > 255 ? 255 : g;
				b = b > 255 ? 255 : b;
				
				image.getPixelWriter().setArgb(i, j, (255 << 24) | (r << 16) | (g << 8) | b);
				
			});
		});
		
		return image;		
	}

	public static WritableImage toPseudoColor(Image src) {

		long l =System.currentTimeMillis();
		
		int w = (int) src.getWidth(); int h = (int) src.getHeight();
		
		WritableImage image = new WritableImage(src.getPixelReader(), w, h);
		
		IntStream.range(0, w).forEach(i -> {
			IntStream.range(0, h).forEach(j -> {
				
				int brightness = image.getPixelReader().getArgb(i, j) & 0xFF;
				int angle = 270 - (int)(brightness / 255d * 270d);

				int[] rgb = ColorConverter.hslToRGB(angle/360d, 0.5d, 0.5d);

				int r = rgb[0];
				int g = rgb[1];
				int b = rgb[2];
			
				image.getPixelWriter().setArgb(i, j, (255 << 24) | (r << 16) | (g << 8) | b);

			});
		});

		System.out.println(System.currentTimeMillis() - l);
		
		return image;		
	}

}
