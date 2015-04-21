package brdf;

import math.Vector;
import texture.Texture;
import util.RGBColor;
import util.ShadeRec;

/**
 * Spacially varying lambertian BRDF.
 * 
 * The properties depend on the position through storing Textures (instead of RGBColors)
 * @author Jonas
 *
 */
public class SVGlossySpecularBRDF {
	
	private double ks;
	private double phongExponent;
	private Texture glossySpecularTexture;

	
	public double getPhongExponent() {
		return phongExponent;
	}
	public void setPhongExponent(double phongExponent) {
		this.phongExponent = phongExponent;
	}
	public SVGlossySpecularBRDF() {
		ks = 0;
		//color = new RGBColor(1);
	}
	public SVGlossySpecularBRDF(SVGlossySpecularBRDF brdf) {
		ks = brdf.ks;
		phongExponent = brdf.phongExponent;
	}
	
	public RGBColor f(ShadeRec sr, Vector wo, Vector wi) {
	//RGBColor 
		
		//normal times incoming direction
		double ndotwi = sr.normal.dot(wi);
		//the reflected direction: formula 15.4 page 281
		Vector r = wi.scale(-1).add(sr.normal.scale(2.0*ndotwi)).normalize();
		
		double rDotWo = r.dot(wo);
		
		if (rDotWo > 0.0){
			return glossySpecularTexture.getColor(sr).scale(ks*Math.pow(rDotWo, phongExponent));
		}
		return new RGBColor(0);
	}
	
	public void  setKs(double ks){
		this.ks=ks;
	}
	
	public RGBColor getReflectance(ShadeRec sr, Vector wo){
		return new RGBColor(1);
	}
	
	public void setTexture(Texture texture){
		this.glossySpecularTexture = texture;
	}
	
}
