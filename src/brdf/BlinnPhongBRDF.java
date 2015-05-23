package brdf;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class BlinnPhongBRDF extends BRDF{
	
	private double ks;
	private RGBColor specularColor;
	private double n;
	
	
	public BlinnPhongBRDF(double n, RGBColor specularColor){
		this.ks = 0;
		this.specularColor = specularColor;
		this.n = n;
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
		double scalingFactor = this.ks * (this.n + 2)/(2*Math.PI) * Math.pow(cosOfDelta, this.n);
		
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
	
	public double getN(){
		return this.n;
	}
	
	public void setN(double n){
		this.n = n;
	}
	

}
