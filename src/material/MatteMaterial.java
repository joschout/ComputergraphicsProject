package material;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;
import brdf.LambertianBRDF;

/**
 * Models a material with perfect diffuse reflection with ambient and diffuse shading
 * 
 * A matte material has: 
 * 	- an ambient reflection coefficient ka
 * 	- a diffuse reflection coeficient kd
 *  - a diffuse color cd
 * These are stored in the BRDFs instead of the materials.
 * @author Jonas
 *
 */
public class MatteMaterial extends Material {

	private LambertianBRDF ambientBRDF;
	private LambertianBRDF diffuseBRDF;
	
	public MatteMaterial(){
		ambientBRDF = new LambertianBRDF();
		diffuseBRDF = new LambertianBRDF();
	}
	
	
	public void setKa(double ka){
		ambientBRDF.setKd(ka);
	}
	
	public void setKd(double kd){
		diffuseBRDF.setKd(kd);
	}
	
	public void setCd(RGBColor color){
		ambientBRDF.setColor(color);
		diffuseBRDF.setColor(color);
	}
	
	public RGBColor shade(ShadeRec sr){
		
		//hb p 271
		Vector wo = sr.ray.direction.scale(-1);
		RGBColor L = ambientBRDF.getReflectance(sr, wo).multiply(sr.world.ambientLight.getRadiance(sr));
		int nbLights = sr.world.lights.size();
		
		for(int j = 0; j< nbLights; j++){
			Vector wi = sr.world.lights.get(j).getDirectionOfIncomingLight(sr);
			double ndotwi = sr.normal.dot(wi);
			
			if(ndotwi > 0.0){
				 L = L.add(diffuseBRDF.f(wo, wi).multiply(sr.world.lights.get(j).getRadiance(sr).scale(ndotwi)));
			}
		}
	
		return L;
	}
	
	
}
