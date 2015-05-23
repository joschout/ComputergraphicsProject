package brdf;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class CookTorranceBRDF extends BRDF{

	private double ks;
	private RGBColor specularColor;

	private double F0;
	private double m;
	
	
	public CookTorranceBRDF(double F0, double m, RGBColor specularColor){
		this.F0 = F0;
		this.m = m;
		this.ks = 0;
		this.specularColor = specularColor;
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
		
		double NdotH = cosOfDelta;
		double NdotV = sr.normal.dot(wo);
		double NdotL = sr.normal.dot(wi);
		double VdotH = wo.dot(halfVector);
		
		double G = Math.max(1,  (2 * NdotH * NdotV)/(VdotH)  );
		
		double D = 1.0 / (m*m* Math.pow(cosOfDelta,4)) * Math.exp(-1.0* Math.pow(tanOfDelta/this.m, 2));
		
//		double fresnel = getFresnelFactorSchlickApproximation(wo, halfVector);
		double fresnel = getFresnelFactorCookTorrance(wo, halfVector);
		
		double scalingFactor = (this.ks * D * G) / (Math.PI * NdotL *NdotV) * fresnel;
		
		return specularColor.scale(scalingFactor);	
	
	}
	
	public double getFresnelFactorSchlickApproximation(Vector v, Vector h){
		double temp = 1.0 - v.dot(h);
		double F = this.F0  + (1 - F0) * Math.pow(temp, 5);
		return F;
	}
	
	public double getFresnelFactorCookTorrance(Vector v, Vector h){
		double rho = (1 + Math.sqrt(F0)) / (1 - Math.sqrt(F0));
		double c = v.dot(h);
		double g = Math.sqrt(rho*rho + c*c - 1.0);
		
		double tempLeft = Math.pow((g-c)/(g+c),2);
		double tempRight = Math.pow(1.0 + ((g+c)*c - 1.0)/((g-c)*c + 1.0), 2);
		double F = 0.5 * tempLeft * tempRight;
		return F;
	}
	

	public void  setKs(double ks){
		this.ks=ks;
	}
	
	public void setSpecularColor(RGBColor specularColor){
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
	
	public double getM(){
		return this.m;
	}
	
	public void setM(double m){
		this.m = m;
	}
	
	public double getF0(){
		return this.F0;
	}
	
	public void setF0(double F0){
		this.F0 = F0;
	}
	
}
