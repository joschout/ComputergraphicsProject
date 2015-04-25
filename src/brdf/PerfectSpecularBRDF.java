package brdf;

import util.RGBColor;
import util.ShadeRec;
import math.Vector;

public class PerfectSpecularBRDF extends BRDF {

	private double kr;
	private RGBColor color;
	
	public PerfectSpecularBRDF() {
		kr = 0;
		color = RGBColor.WHITE;
	}
	
	@Override
	public RGBColor f(ShadeRec sr, Vector wo, Vector wi) {
		return color.scale(kr);
	}
		
	public void  setKr(double kr){
		this.kr=kr;
	}
	
	public void  setColor(RGBColor color){
		this.color = color;
	}
	
	public RGBColor getReflectance(ShadeRec sr, Vector wo){
		return RGBColor.BLACK;
	}

}
