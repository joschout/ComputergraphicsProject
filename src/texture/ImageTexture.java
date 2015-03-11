package texture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mapping.Mapping;
import util.RGBColor;
import util.ShadeRec;

public class ImageTexture implements Texture {

	private int hres;
	private int vres;
	private BufferedImage image;
	private Mapping mapping;
	
	public ImageTexture(String filename, Mapping mapping){
		if(filename == null){
			throw new IllegalArgumentException("filename for image is null");
		}
		
		setImage(readImage(filename));
		setHres(image.getWidth());
		setVres(image.getHeight());
		setMapping(mapping);
	}

	@Override
	public RGBColor getColor(ShadeRec sr) {
		int yp;
		int xp;
		if(mapping != null){
			TexelCoordinates texCoo = mapping.getTexelCoordinates(sr.localHitPoint, hres, vres);
			xp = texCoo.xp;
			yp = texCoo.yp;
		}else{
			xp = (int) (sr.u *(hres-1));
			yp = (int) (sr.v *(vres-1));			
		}	
//		System.out.println("xp/hres: " + xp + "/"+ hres + ", yp/vres: " + yp + "/"+ vres) ;
		int readColor = image.getRGB(xp, yp);

//		int  red = (color & 0x00ff0000) >> 16;
//		int  green = (color & 0x0000ff00) >> 8;
//		int  blue = color & 0x000000ff;
//		//int alpha = (color>>24) & 0xff;
		
		Color javaColor = new Color(readColor);
		return RGBColor.convertToRGBColor(javaColor);
	}

	public BufferedImage readImage(String filename){
		
		try {
		    File file = new File(filename);
		    if (! file.exists() || file.isDirectory()) {
				file = new File(filename + ".obj");
			}
			if (! file.exists() || file.isDirectory()) {
				//throw new IOException(filename + " is geen geldig bestand.");
				System.out.println(filename + " is geen geldig bestand.");
			}
			image = ImageIO.read(file);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return image;
	}



	public int getHres() {
		return hres;
	}

	public void setHres(int hres) {
		this.hres = hres;
	}

	public int getVres() {
		return vres;
	}

	public void setVres(int vres) {
		this.vres = vres;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public Mapping getMapping() {
		return mapping;
	}

	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}
}
