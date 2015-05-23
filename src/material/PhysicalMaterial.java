package material;

import light.Light;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;
import brdf.BRDF;
import brdf.LambertianBRDF;

public class PhysicalMaterial extends Material {
	
	private LambertianBRDF ambientBRDF;
	private LambertianBRDF diffuseBRDF;
	private BRDF physicalBRDF;
	
	public PhysicalMaterial(BRDF specularBRDF){
		ambientBRDF = new LambertianBRDF();
		diffuseBRDF = new LambertianBRDF();
		physicalBRDF = specularBRDF;
	}
	
	@Override
	public RGBColor shade2(ShadeRec sr) {
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
		return diffuseBRDF.f(sr, wo, wi).add(physicalBRDF.f(sr, wo, wi));
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

	@Override
	public RGBColor shade(ShadeRec sr) {
		// TODO Auto-generated method stub
		return null;
	}
}
