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
public class SVLambertianBRDF extends BRDF{

	private static final double inversePi = 1.0 / Math.PI;
	private double kd;
	private Texture diffuseTexture;
	
	public SVLambertianBRDF() {
		kd = 0;
		//color = new RGBColor(1);
	}
	
	public SVLambertianBRDF(SVLambertianBRDF brdf) {
			kd = brdf.kd;
			//color = new RGBColor(brdf.color);
		}
	
	@Override
	public RGBColor f(ShadeRec sr, Vector wo, Vector wi) {
		return diffuseTexture.getColor(sr).scale(kd * inversePi);
				
	}
	
	public void  setKd(double kd){
		this.kd=kd;
	}
	
	public void  setTexture(Texture texture){
		this.diffuseTexture = texture;
	}
	
	public RGBColor getReflectance(ShadeRec sr, Vector wo){
		return diffuseTexture.getColor(sr).scale(kd);
	}

}
