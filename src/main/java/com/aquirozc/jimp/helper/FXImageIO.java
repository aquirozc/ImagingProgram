package com.aquirozc.jimp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.glavo.png.javafx.PNGJavaFXUtils;

import com.aquirozc.jimp.data.HuffmanImage;
import com.aquirozc.jimp.engine.ColorOp;
import com.aquirozc.jimp.engine.HuffmanCode;

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

		File file = fChooser.showOpenDialog(owner);

		if (file == null){
			return image;
		}

		if(file.getName().endsWith(".fck")){
			try(FileInputStream fStream = new FileInputStream(file); ObjectInputStream oStream = new ObjectInputStream(fStream)){
				return HuffmanCode.decodeImage((HuffmanImage)oStream.readObject());
			}catch (Exception e){
				e.printStackTrace();
				return image;
			}
		}

        try(FileInputStream stream = new FileInputStream(file)){
            image = new Image(stream);
        } catch (Exception e) {}

        return image;

    }

    public void saveImageToDisk(Image img) {
		
		File out = fChooser.showSaveDialog(owner);

		if (out == null || img == null){
			return;
		}

		String name = out.getName();

		if(name.endsWith(".fck")){

			try(FileOutputStream fStream = new FileOutputStream(out); ObjectOutputStream oStream = new ObjectOutputStream(fStream)){
				oStream.writeObject(HuffmanCode.encodeImage(ColorOp.toGrayScale(img)));
				return;
			}catch (Exception e){
				return;
			}
		}

		if (!name.endsWith(".png")){
			out = new File(out.getParent(), name + ".png");
		}
		
		try {
			PNGJavaFXUtils.writeImage(img, out.toPath());
		} catch (IOException | NullPointerException e) {}
		   	
	}
    
}
