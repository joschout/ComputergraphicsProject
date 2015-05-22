package material;

import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;
import brdf.PerfectSpecularBRDF;
import btdf.PerfectTransmitterBTDF;

public class SimpleTransparentMaterial extends PhongMaterial {
	
	private PerfectSpecularBRDF reflectiveBRDF;
	private PerfectTransmitterBTDF specularBTDF;
	
	public SimpleTransparentMaterial() {
		super();
		this.reflectiveBRDF = new PerfectSpecularBRDF();
		this.specularBTDF = new PerfectTransmitterBTDF();
	}
	
	public void setKr(double kr){
		this.reflectiveBRDF.setKr(kr);
	}
	
	public void setCr(RGBColor color){
		this.reflectiveBRDF.setColor(color);
	}
	
	public void setKt(double kt){
		this.specularBTDF.setKt(kt);
	}
	
	public void  setAbsoluteIndexOfRefraction(double ior){
		this.specularBTDF.setAbsoluteIndexOfRefraction(ior);
	}
	
	@Override
	public RGBColor shade2(ShadeRec sr){
		//de directe belichting
		RGBColor L = super.shade2(sr);
		
		Vector wo = sr.ray.direction.scale(-1);
		double ndotwo = sr.normal.dot(wo);
		//direction of the reflected ray:
		Vector wi = wo.scale(-1).add(sr.normal.scale(2.0*ndotwo));
		RGBColor reflectiveF = reflectiveBRDF.f(sr, wo, wi);
		Ray reflectedRay = new Ray(sr.hitPoint, wi, sr.depth + 1);
		
		if (specularBTDF.checkTotalInternalReflection(sr, wo)) {
			// kr = 1.0
			return L = L.add(sr.tracer.traceRay(reflectedRay));
		}else {
			RGBColor transmissiveF = specularBTDF.sampleF(sr,wo,wi );
			
			Vector wt = sr.wt;
			Ray transmissiveRay = new Ray(sr.hitPoint, wt, sr.depth + 1);
			
			L = L.add(reflectiveF.multiply(sr.tracer.traceRay(reflectedRay)));
			
			L = L.add(transmissiveF.multiply(sr.tracer.traceRay(transmissiveRay)));
			
		}
		
		return L;	
	}

}
