package util;

import java.awt.Color;

public class ColorUtils {

	
	public static Color multiplyRGBComponentsBy(double scalar, Color color){
		int alpha = color.getAlpha();
		int R = (int)(color.getRed()*scalar);
		int G= (int)(color.getGreen()*scalar);
		int B = (int)(color.getBlue()*scalar);
		return new Color(alpha, R, G, B);
	}
	
	public static Color multiplyEachRGBComponent(Color color1, Color color2){
		int alpha = color1.getAlpha();
		int R = (int)(color1.getRed()*color2.getRed());
		int G= (int)(color1.getGreen()*color2.getGreen());
		int B = (int)(color1.getBlue()*color2.getBlue());
		return new Color(alpha, R, G, B);
	}
	
	public static Color sumEachRGBComponent(Color color1, Color color2){
		int alpha = color1.getAlpha();
		int R = (int)(color1.getRed()+color2.getRed());
		int G= (int)(color1.getGreen()+color2.getGreen());
		int B = (int)(color1.getBlue()+color2.getBlue());
		return new Color(alpha, R, G, B);
	}
	
}
