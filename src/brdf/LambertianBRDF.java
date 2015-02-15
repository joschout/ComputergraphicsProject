package brdf;

import util.RGBColor;
import util.ShadeRec;
import math.Vector;

public class LambertianBRDF extends BRDF {

	private static final double inversePi = 1.0 / Math.PI;
	private double kd;
	private RGBColor color;
	@Override
	public RGBColor f(Vector w0, Vector w02) {
		return color.scale(kd * inversePi);
				
	}
	
	public void  setKd(double kd){
		this.kd=kd;
	}
	
	public void  setColor(RGBColor color){
		this.color = color;
	}
	
	public RGBColor getReflectance(ShadeRec sr, Vector wo){
		return color.scale(kd);
	}

}
