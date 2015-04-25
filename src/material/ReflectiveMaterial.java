package material;

import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;
import brdf.PerfectSpecularBRDF;

public class ReflectiveMaterial extends PhongMaterial {
	
	private PerfectSpecularBRDF reflectiveBRDF;

	public ReflectiveMaterial() {
		super();
		this.reflectiveBRDF = new PerfectSpecularBRDF();
	}
	
	public void setKr(double kr){
		this.reflectiveBRDF.setKr(kr);
	}
	
	public void setCr(RGBColor color){
		this.reflectiveBRDF.setColor(color);
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
		
		return L = L.add(reflectiveF.multiply(sr.tracer.traceRay(reflectedRay)));
	}

}
