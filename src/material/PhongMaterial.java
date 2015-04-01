package material;

import light.Light;
import math.Ray;
import math.Vector;
import brdf.GlossySpecularBRDF;
import brdf.LambertianBRDF;
import util.RGBColor;
import util.ShadeRec;

public class PhongMaterial extends Material {
	private LambertianBRDF ambientBRDF;
	private LambertianBRDF diffuseBRDF;
	private GlossySpecularBRDF specularBRDF;
	
	public PhongMaterial(){
		ambientBRDF = new LambertianBRDF();
		diffuseBRDF = new LambertianBRDF();
		specularBRDF = new GlossySpecularBRDF();
	}
	

	public RGBColor shade(ShadeRec sr){
		//hb p 285
		Vector wo = sr.ray.direction.scale(-1);
		RGBColor L = ambientBRDF.getReflectance(sr, wo).multiply(sr.world.ambientLight.getRadiance(sr));
		
		for(Light light: sr.world.lights){
			Vector wi = light.getDirectionOfIncomingLight(sr);
			double ndotwi = sr.normal.dot(wi);
			
			if(ndotwi > 0.0){
				boolean inShadow = false;
				if(light.castShadows()){
					Ray shadowRay = new Ray(sr.hitPoint, wi);
					inShadow = light.inShadow(shadowRay, sr);
				}
				
				if(! inShadow){
					L = L.add(totalBRDF(sr, wo, wi)
							.multiply(light.getRadiance(sr).scale(ndotwi)));
				}
			}	
		}
		return L;
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
	
	
	@Override
	public RGBColor totalBRDF(ShadeRec sr, Vector wo, Vector wi) {
		return diffuseBRDF.f(sr, wo, wi).add(specularBRDF.f(sr, wo, wi));
	}
	
	public void setKa(double ka){
		ambientBRDF.setKd(ka);
	}
	
	public void setKd(double kd){
		diffuseBRDF.setKd(kd);
	}
	
	public void setKs(double ks){
		specularBRDF.setKs(ks);
	}
	
	public void setCd(RGBColor color){
		ambientBRDF.setColor(color);
		diffuseBRDF.setColor(color);
	}
	
	public void setCs(RGBColor color){
		specularBRDF.setColor(color);
	}
	
	public void setPhongExponent(double e){
		specularBRDF.setPhongExponent(e);
	}
	
	
}
