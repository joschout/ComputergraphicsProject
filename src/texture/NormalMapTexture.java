package texture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import math.Matrix;
import math.Transformation;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class NormalMapTexture {

	private int hres;
	private int vres;
	private BufferedImage normalMapImage;
	private Transformation objectTransformation;
	
	
	public NormalMapTexture(String fileName, Transformation objectTransformation){
		if(fileName == null){
			throw new IllegalArgumentException("filename for normal map image is null");
		}
		setNormalMapImage(readImage(fileName));
		setHres(normalMapImage.getWidth());
		setVres(normalMapImage.getHeight());
		setObjectTransformation(objectTransformation);
	}

	public Transformation getObjectTransformation() {
		return objectTransformation;
	}

	public void setObjectTransformation(Transformation objectTransformation) {
		this.objectTransformation = objectTransformation;
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
			normalMapImage = ImageIO.read(file);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return normalMapImage;
	}
	
	public Vector getGeometricNormal(ShadeRec sr){
		
		int xp = (int)(sr.u *(hres-1));
		int yp = (vres-1) - (int) (sr.v *(vres-1));
		
		int readColor = normalMapImage.getRGB(xp, yp);
		
		Color javaColor = new Color(readColor);
		
		RGBColor rgbColor = RGBColor.convertToRGBColor(javaColor);
		
		double shadingNormalXCoord = 2*rgbColor.R() - 1;
		double shadingNormalYCoord = 2*rgbColor.G() - 1;
		double shaidingNormalZCoord = 2*rgbColor.B() - 1;
		
		Vector untransformedShadingNormal = new Vector(shadingNormalXCoord, shadingNormalYCoord, shaidingNormalZCoord);
		
		Matrix transposeOfInverse = this.objectTransformation.getInverseTransformationMatrix().transpose();
		Vector transformedShadingNormal = transposeOfInverse.transform(untransformedShadingNormal);	
		
		return transformedShadingNormal;
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
	
	
	public BufferedImage getNormalMapImage() {
		return normalMapImage;
	}

	public void setNormalMapImage(BufferedImage image) {
		this.normalMapImage = image;
	}
	
}
