package material;

import math.Ray;
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
		
//		System.out.println("==============");
//		System.out.println("Ambient color:");
//		System.out.println(L.toString());
//		
		for(int j = 0; j< nbLights; j++){
			Vector wi = sr.world.lights.get(j).getDirectionOfIncomingLight(sr);
			double ndotwi = sr.normal.dot(wi);
			
			if(ndotwi > 0.0){
				
				//shadow testing: does the hitpoint lie in the shadow of light source j ?
				boolean inShadow = false;
				if(sr.world.lights.get(j).castShadows()){// if light source j casts shadows
					// new shadow ray with as origin the hit point and as direction 
					// the direction of the incoming light
					Ray shadowRay = new Ray(sr.hitPoint, wi); 
					/*
					 * test for this light source if there are objects between the hit point and the
					 * location of the light source
					 */
					inShadow = sr.world.lights.get(j).inShadow(shadowRay, sr);
				}
				if(! inShadow){
					/*
					 * if the point doesn't lie in the shadow of light source j
					 * then add the radiance of light source j to the total radiance.
					 */
					L = L.add(diffuseBRDF.f(sr, wo, wi).multiply(sr.world.lights.get(j).getRadiance(sr).scale(ndotwi)));
//					System.out.println("diffuse color from light " + j  + " + previous color");
//					System.out.println(L.toString());
				 }
			}
		}
//		System.out.println("final color: "+ L.toString());
		return L;
	}
	
	
}
