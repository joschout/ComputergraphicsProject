package material;

import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;
import brdf.FresnelReflectorBRDF;
import brdf.PerfectSpecularBRDF;
import btdf.FresnelTransmitterBTDF;
import btdf.PerfectTransmitterBTDF;

public class DielectricMaterial extends PhongMaterial{

	//interior filter color
	private RGBColor cf_in;
	//exterior filter color
	private RGBColor cf_out;

	private FresnelReflectorBRDF fresnelBRDF;
	private FresnelTransmitterBTDF fresnelBTDF;
	
	public DielectricMaterial() {
		super();
		this.fresnelBRDF = new FresnelReflectorBRDF();
		this.fresnelBTDF = new FresnelTransmitterBTDF();
		this.cf_in = RGBColor.WHITE;
		this.cf_out = RGBColor.WHITE;
	}
	

	@Override
	public RGBColor shade2(ShadeRec sr){
		//de directe belichting
		RGBColor L = super.shade2(sr);

		Vector wo = sr.ray.direction.scale(-1);
		double nDotWo = sr.normal.dot(wo);
		//direction of the reflected ray:
		Vector wi = wo.scale(-1).add(sr.normal.scale(2.0*nDotWo));
		
		Ray reflectedRay = new Ray(sr.hitPoint, wi, sr.depth + 1);

		double t;
		RGBColor Lr, Lt;
		double nDotWi = sr.normal.dot(wi);

		if (fresnelBTDF.checkTotalInternalReflection(sr, wo)) {//total internal reflection
			if (nDotWi < 0.0) { //reflected ray is inside
				Lr = sr.tracer.traceRaycolorFilter(reflectedRay, sr);
				
				t = sr.t;
				//inside filter color
				L = L.add(this.cf_in.power(t).multiply(Lr));			
			} else { //reflected ray is outside
				Lr = sr.tracer.traceRaycolorFilter(reflectedRay, sr);
				//kr = 1.0
				t = sr.t;
				//outside filter color
				L = L.add(cf_out.power(t).multiply(Lr)); 
			}	
		} else { // no total internal reflection
			
			RGBColor reflectiveF = fresnelBRDF.f(sr, wo, wi);
			RGBColor transmissiveF = fresnelBTDF.sampleF(sr,wo,wi );

			Vector wt = sr.wt;
			Ray transmissiveRay = new Ray(sr.hitPoint, wt, sr.depth + 1);
			double nDotWt = sr.normal.dot(wt);

			if (nDotWi < 0.0) { // reflected ray is inside, transmitted ray is outside
				//reflected ray is inside	
				Lr = reflectiveF.multiply(sr.tracer.traceRaycolorFilter(reflectedRay, sr));
				t = sr.t;
				//inside filter color
				L = L.add(cf_in.power(t).multiply(Lr));

				//transmitted ray is outside	
				Lt = transmissiveF.multiply(sr.tracer.traceRaycolorFilter(transmissiveRay, sr));
				t = sr.t;
				//outside filter color
				L = L.add(cf_out.power(t).multiply(Lt));
			} else { // reflected ray is outside, transmitted ray is inside
				//reflected ray is outside
				Lr = reflectiveF.multiply(sr.tracer.traceRaycolorFilter(reflectedRay, sr));
				t = sr.t;
				//outside filter color
				L = L.add(cf_out.power(t).multiply(Lr));

				//transmitted ray is inside
				Lt = transmissiveF.multiply(sr.tracer.traceRaycolorFilter(transmissiveRay, sr));
				t = sr.t;
				//outside filter color
				L = L.add(cf_in.power(t).multiply(Lt));
			}	
		}
		return L;
	}

	public RGBColor getCf_in() {
		return cf_in;
	}

	public void setCf_in(RGBColor cf_in) {
		this.cf_in = cf_in;
	}

	public RGBColor getCf_out() {
		return cf_out;
	}

	public void setCf_out(RGBColor cf_out) {
		this.cf_out = cf_out;
	}
	
	public void setEtaIncoming(double etaIncoming) {
		fresnelBRDF.setEtaIncoming(etaIncoming);
		fresnelBTDF.setEtaIncoming(etaIncoming);
	}
	
	public void setEtaOutgoing(double etaOutgoing) {
		fresnelBRDF.setEtaOutgoing(etaOutgoing);
		fresnelBTDF.setEtaOutgoing(etaOutgoing);
	}
}
