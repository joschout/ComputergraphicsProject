package brdf;

import util.RGBColor;
import util.ShadeRec;
import math.Vector;

public class PerfectSpecularBRDF extends BRDF {

	private double kr;
	private RGBColor color;
	
	
	@Override
	public RGBColor f(ShadeRec sr, Vector w0, Vector w1) {
	
	}
	
	public void  setKr(double kr){
		this.kr=kr;
	}
	
	public void  setColor(RGBColor color){
		this.color = color;
	}
	
	public RGBColor getReflectance(ShadeRec sr, Vector wo){
	
	}

}
