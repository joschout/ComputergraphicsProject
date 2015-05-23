package brdf;

import java.math.BigDecimal;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class LafortuneBRDF extends BRDF{
	

	private double ks;
	private RGBColor specularColor;

	private double Cxy;
	private double Cz;
	private double n;
	
	
	
	public LafortuneBRDF(double Cxy, double Cz, double n, RGBColor specularColor){
		this.ks = 0;
		this.specularColor = specularColor;
		this.Cxy = Cxy;
		this.Cz = Cz;
		this.n = n;
	}
	
	@Override
	public RGBColor f(ShadeRec sr, Vector wo, Vector wi) {
		
		/*
		 * l = wi = incoming vector (light)
		 * v = wo = outgoing vector (view)
		 */
		
		Vector l = wi;
		Vector v = wo;
		
		
		double sumWithoutPower = this.Cxy*(l.x*v.x + l.y*v.y) + this.Cz * l.z * v.z;
		double partialDenominator = Math.max(Math.abs(Cz), Math.abs(Cxy));
		double tempFractionWithoutPower = sumWithoutPower/partialDenominator;
		
		double tempFractionPowered = Math.pow( tempFractionWithoutPower, n);
				
		double scalingFactor = this.ks * (n + 2)  * tempFractionPowered/(2 * Math.PI );
			
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
	
	public double getCxy(){
		return this.Cxy;
	}
	
	public void setCxy(double Cxy){
		this.Cxy = Cxy;
	}
	
	public double getCz(){
		return this.Cz;
	}
	
	public void setCz(double Cz){
		this.Cz = Cz;
	}
	
	public double getN(){
		return this.n;
	}
	
	public void setN(double n){
		this.n = n;
	}
	

}
