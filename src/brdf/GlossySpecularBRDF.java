package brdf;

import util.RGBColor;
import util.ShadeRec;
import math.Vector;

public class GlossySpecularBRDF extends BRDF {

	private double ks;
	private RGBColor color;
	private double phongExponent;
	
	
	public double getPhongExponent() {
		return phongExponent;
	}
	public void setPhongExponent(double phongExponent) {
		this.phongExponent = phongExponent;
	}
	public GlossySpecularBRDF() {
		ks = 0;
		color = new RGBColor(1);
	}
	public GlossySpecularBRDF(GlossySpecularBRDF brdf) {
		ks = brdf.ks;
		color = new RGBColor(brdf.color);
	}
	
	/**
	 * hb p 284
	 */
	@Override
	public RGBColor f(ShadeRec sr, Vector wo, Vector wi) {
		//RGBColor 
		
		//normal times incoming direction
		double ndotwi = sr.normal.dot(wi);
		//the reflected direction: formula 15.4 page 281
		Vector r = wi.scale(-1).add(sr.normal.scale(2.0*ndotwi));
		
		double rDotWo = r.dot(wo);
		
		if (rDotWo > 0.0){
			return color.scale(ks*Math.pow(rDotWo, phongExponent));
		}
		return new RGBColor(0);
	}
	
	public GlossySpecularBRDF clone(){
		return new GlossySpecularBRDF(this);
	}
	
	
	
	public void  setKs(double ks){
		this.ks=ks;
	}
	
	public void  setColor(RGBColor color){
		this.color = color;
	}
	
	public RGBColor getReflectance(ShadeRec sr, Vector wo){
		return new RGBColor(1);
	}
	
	
}
