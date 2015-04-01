package material;

import light.AreaLight;
import light.Light;
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
		
//		System.out.println("==============");
//		System.out.println("Ambient color:");
//		System.out.println(L.toString());
//		
		for(Light light: sr.world.lights){
			Vector wi = light.getDirectionOfIncomingLight(sr);
			double ndotwi = sr.normal.dot(wi);
			
			if(ndotwi > 0.0){
				
				//shadow testing: does the hitpoint lie in the shadow of light source j ?
				boolean inShadow = false;
				
				if(light.castShadows()){// if light source j casts shadows
					// new shadow ray with as origin the hit point and as direction 
					// the direction of the incoming light
					Ray shadowRay = new Ray(sr.hitPoint, wi); 
					/*
					 * test for this light source if there are objects between the hit point and the
					 * location of the light source
					 */
					inShadow = light.inShadow(shadowRay, sr);
				}
				if(! inShadow){
					/*
					 * if the point doesn't lie in the shadow of light source j
					 * then add the radiance of light source j to the total radiance.
					 */
					L = L.add(totalBRDF(sr, wo, wi)
							.multiply(light.getRadiance(sr).scale(ndotwi)));
//					System.out.println("diffuse color from light " + j  + " + previous color");
//					System.out.println(L.toString());
				 }
			}
		}
//		System.out.println("final color: "+ L.toString());
		return L;
	}
	
	@Override
	public RGBColor totalBRDF(ShadeRec sr, Vector wo, Vector wi) {
		return diffuseBRDF.f(sr, wo, wi);
	}
	
	public RGBColor shade2(ShadeRec sr){
		
		//hb p 271
		Vector wo = sr.ray.direction.scale(-1);
		RGBColor L = ambientBRDF.getReflectance(sr, wo).multiply(sr.world.ambientLight.getRadiance(sr));
	
		for(Light light: sr.world.lights){
			RGBColor partialLOfThisLightSource = light.handleMaterialShading(this, sr, wo);
			L = L.add(partialLOfThisLightSource);
		}
		return L;
	}

//	public RGBColor shade3(ShadeRec sr){
//		
//		//hb p 271
//		Vector wo = sr.ray.direction.scale(-1);
//		RGBColor L = ambientBRDF.getReflectance(sr, wo).multiply(sr.world.ambientLight.getRadiance(sr));
//		
////		System.out.println("==============");
////		System.out.println("Ambient color:");
////		System.out.println(L.toString());
////		
//		for(Light light: sr.world.lights){
//			if (light.getClass() == AreaLight.class) {
//				RGBColor additionalColor = new RGBColor(0);
//				for (int i = 0; i < AreaLight.nbOfShadowRaysPerAreaLight; i++) {
//					Vector wi = light.getDirectionOfIncomingLight(sr);
//					double ndotwi = sr.normal.dot(wi);
//					double ndotwo = sr.normal.dot(wo);
//					if(ndotwi > 0.0 && ndotwo > 0.0){
//						boolean inShadow = false;
//						if(light.castShadows()){
//							Ray shadowRay = new Ray(sr.hitPoint, wi);
//							inShadow = light.inShadow(shadowRay, si);
//						}
//						if(!inShadow){
//							additionalColor = this.diffuseBRDF.f(sr, wi, wo).multiply(light.getRadiance(sr)
//									.scale(((AreaLight)light).G(sr)/((AreaLight)light).pdf(sr))).scale(ndotwi).add(additionalColor);
//						}
//					}
//					
//					L = additionalColor.scale(1.0/AreaLight.nbOfShadowRaysPerAreaLight).add(L);
//				}				
//			}else {
//				
//			}
//
////		System.out.println("final color: "+ L.toString());
//		return L;
//	}

	
}
