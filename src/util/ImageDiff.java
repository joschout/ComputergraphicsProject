package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageDiff {

	public static void main(String[] args) {

		
		BufferedImage image1 = readImage("buddhaLowPoly normal map.png");
		BufferedImage image2 = readImage("buddhaLowPoly matte.png");
				
		

		BufferedImage diff = getDifferenceImage(image1, image2);
		
		try {
			ImageIO.write(diff, "png", new File("buddha difference normal map - without normal map.png"));
		} catch (IOException e) {
		}

	}
	
	public static BufferedImage readImage(String filename){
		BufferedImage image = null;
		try {
		    File file = new File(filename);
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
	
	
	public static BufferedImage getDifferenceImage(BufferedImage img1, BufferedImage img2) {
	    int width1 = img1.getWidth(); // Change - getWidth() and getHeight() for BufferedImage
	    int width2 = img2.getWidth(); // take no arguments
	    
	    int height1 = img1.getHeight();
	    int height2 = img2.getHeight();
	    
	    if ((width1 != width2) || (height1 != height2)) {
	        System.err.println("Error: Images dimensions mismatch");
	        System.exit(1);
	    }

	    // NEW - Create output Buffered image of type RGB
	    BufferedImage outImg = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);

	    double maxR = 0;
	    double maxG = 0;
	    double maxB = 0;

	    for (int i = 0; i < height1; i++) {
	        for (int j = 0; j < width1; j++) {
	        	
	            int readColor1 = img1.getRGB(j, i);
	            Color javaColor1 = new Color(readColor1);
	    		RGBColor rgbColor1 = RGBColor.convertToRGBColor(javaColor1);
	            
	            int readColor2 = img2.getRGB(j, i);
	            Color javaColor2 = new Color(readColor2);
	    		RGBColor rgbColor2 = RGBColor.convertToRGBColor(javaColor2);
	    		
	    		double R = Math.abs(rgbColor1.R() - rgbColor2.R());
	            double G  = Math.abs(rgbColor1.G() - rgbColor2.G());
	            double B = Math.abs(rgbColor1.B() - rgbColor2.B());
	    		
//	            double R = Math.pow( Math.abs(rgbColor1.R() - rgbColor2.R()) ,2);
//	            double G  = Math.pow(Math.abs(rgbColor1.G() - rgbColor2.G()) ,2);
//	            double B = Math.pow(Math.abs(rgbColor1.B() - rgbColor2.B()) ,2);
	    		
	            if (R > maxR) {
					maxR = R;
				}
	            if (G > maxG) {
					maxG = G;
				}
	            if (B > maxB) {
					maxB = B;
				}
	            Color result = new Color((float)R, (float)G, (float)B);
	            outImg.setRGB(j, i, result.getRGB()); // Set result
	        }
	    }
	    

	    for (int i = 0; i < height1; i++) {
	        for (int j = 0; j < width1; j++) {
	        	
	        	int readColor = outImg.getRGB(j, i);
		        Color javaColor = new Color(readColor);
		    	RGBColor rgbColor = RGBColor.convertToRGBColor(javaColor);
	        	
		    	double newR = rgbColor.R()/maxR;
		    	if (newR > 1) {
					newR = 1;
				}
		    	double newG = rgbColor.G()/maxG;
		    	if (newG > 1) {
					newG = 1;
				}
		    	double newB = rgbColor.B()/maxB;
		    	if (newB > 1) {
					newB = 1;
				}
		    	if (newR < 0 || newR > 1) {
					System.out.println("R: " + newR);
				}
		    	if (newG < 0 || newG > 1) {
		    		System.out.println("rgbColor.G(): " + rgbColor.G());
		    		System.out.println("maxG: " + maxG);
					System.out.println("newG: " + newG);
				}
		    	if (newB < 0 || newB > 1) {
					System.out.println("B: " + newB);
				}
		    	
		    	Color result = new Color((float)newR, (float)newG, (float)newB);
		            //Color result = new Color((float)sum, (float)sum, (float)sum);
		            
		        outImg.setRGB(j, i, result.getRGB());
	        	
	        	
	        }
	    }
	    

	    // Now return
	    return outImg;
	}
	

}
