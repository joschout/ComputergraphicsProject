package brdf;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class WardBRDF extends BRDF {

	private double ks;
	private RGBColor specularColor;

	private double alpha;
	
	
	public WardBRDF(double alpha, RGBColor specularColor){
		this.ks = 0;
		this.specularColor = specularColor;
		this.alpha = alpha;
	}
	
	@Override
	public RGBColor f(ShadeRec sr, Vector wo, Vector wi) {
		
		/*
		 * zie ook hb pagina 288
		 * 
		 * l = wi = incoming vector (light)
		 * v = wo = outgoing vector (view)
		 * h = halfvector = the unit vector halfway between l and v
		 * 
		 */
		
		Vector halfVector = wi.halfVector(wo);
		
		double cosOfDelta = sr.normal.dot(halfVector);
		double sinOfDelta = sr.normal.cross(halfVector).length();
		double tanOfDelta = sinOfDelta/cosOfDelta;
	
		double NdotL = sr.normal.dot(wi);
		double NdotV = sr.normal.dot(wo);
		
		double tempLeft = 1.0 /Math.sqrt(NdotL * NdotV);
		double tempRightNumerator = Math.exp( -1.0 * tanOfDelta * tanOfDelta /(this.alpha * this.alpha));
		double tempRightDenominator = 4 * Math.PI * this.alpha * this.alpha;
		
		double scalingFactor = this.ks * tempLeft * tempRightNumerator / tempRightDenominator;
		return specularColor.scale(scalingFactor);	
	
	}

	public void  setKs(double ks){
		this.ks=ks;
	}
	
	public void  setSpecularColor(RGBColor specularColor){
		this.specularColor = specularColor;
	}
	
	@Override
	public RGBColor getReflectance(ShadeRec sr, Vector wo){
		return RGBColor.WHITE;
	}

	public double getKs(){
		return this.ks;
	}
	
	public RGBColor getSpecularColor(){
		return this.specularColor;
	}
	
	public double getAplha(){
		return this.alpha;
	}
	
	public void setAlpha(double alpha){
		this.alpha = alpha;
	}
}
