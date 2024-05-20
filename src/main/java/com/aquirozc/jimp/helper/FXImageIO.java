package com.aquirozc.jimp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.glavo.png.javafx.PNGJavaFXUtils;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class FXImageIO {
    
	private final FileChooser fChooser = new FileChooser();
	private final Window owner;
	
	public FXImageIO (Window owner) {
		this.owner = owner;
	}

    public Image readImage(){

        Image image = null;

        try(FileInputStream stream = new FileInputStream(fChooser.showOpenDialog(owner))){
            image = new Image(stream);
        } catch (Exception e) {}

        return image;

    }

    public void saveImageToDisk(Image img) {
		
		File out = fChooser.showSaveDialog(owner);
		
		try {
			PNGJavaFXUtils.writeImage(img, out.toPath());
		} catch (IOException | NullPointerException e) {}
		   	
	}
    
}
