package btdf;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class PerfectTransmitterBTDF extends BTDF {
	
	/**
	 * transmission coefficient, as defined in the book on p.570
	 * kt is element of [0, 1]
	 * conservation of energy: kr + kt = 1
	 * 
	 */
	private double kt;
	private double absoluteIndexOfRefraction;
	
	public PerfectTransmitterBTDF() {
		super();
		this.kt = 0;
		this.absoluteIndexOfRefraction = 1.0;
	}
	
	public PerfectTransmitterBTDF(double kt, double absoluteIndexOfRefraction) {
		super();
		this.kt = kt;
		this.absoluteIndexOfRefraction = absoluteIndexOfRefraction;
	}

	/**
	 * Checks for total internal reflection.
	 * Total internal reflection occurs when:
	 * 		1 - (1-cos theta_i)^2/ eta < 0
	 * 		where eta = eta_transmitted/eta_incident
	 * 
	 */
	@Override
	public boolean  checkTotalInternalReflection(ShadeRec sr){
		Vector wo = sr.ray.direction.scale(-1);
		double cosOfThetaIncoming = sr.normal.dot(wo);
		double eta = this.absoluteIndexOfRefraction;
		
		/*
		 * check if the ray hits the INSIDE surface of an object
		 *=> condition: ray hits the inside if:
		 *	cos(theta_i) = nDotWo < 0
		 *
		 *In dat geval moet eta geinverteerd worden
		 */
		if (cosOfThetaIncoming < 0.0) {
			eta = 1.0/eta;
		}
		
		return (1.0 - (1.0 - cosOfThetaIncoming * cosOfThetaIncoming)/ (eta*eta) < 0.0);
	}
	
	/**
	 * Computes the transmitted ray direction.
	 * Should only be called when there is no total internal reflection.
	 * As there is no color involved with transmission in this chapter, 
	 * 	the return value is multiplied by white so that it will return an RGBColor.
	 * @return
	 */
	public RGBColor sampleF(ShadeRec sr, Vector wo, Vector wi){
		
		Vector normal = new Vector(sr.normal);
		double cosOfThetaIncoming  = normal.dot(wo);
		double eta = this.absoluteIndexOfRefraction;
			
		/*
		 * check if the ray hits the INSIDE surface of an object
		 *=> condition: ray hits the inside if:
		 *	cos(theta_i) = nDotWo < 0
		 *
		 *In dat geval moet eta geinverteerd worden
		 */

		if (cosOfThetaIncoming < 0.0) {
			cosOfThetaIncoming = - cosOfThetaIncoming;
			normal = normal.scale(-1);		
			eta = 1.0/eta;
		}

		
		double temp = 1.0 - (1.0 - cosOfThetaIncoming * cosOfThetaIncoming)/ (eta*eta);
		
		
		double cosOfThetaTransmitted = Math.sqrt(temp);
		Vector wt = wo.scale(-1.0/eta).subtract(normal.scale(cosOfThetaTransmitted - cosOfThetaIncoming/eta)).normalize();
		sr.wt = wt;

		double scalingFactor = kt/eta*eta;
		return new RGBColor((float)scalingFactor);
		
	}
	
	public void  setKt(double kt){
		this.kt=kt;
	}
	
	public void  setAbsoluteIndexOfRefraction(double ior){
		this.absoluteIndexOfRefraction = ior;
	}
	
	public RGBColor getReflectance(ShadeRec sr, Vector wo){
		return RGBColor.BLACK;
	}

	
}
