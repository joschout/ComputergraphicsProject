package btdf;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class FresnelTransmitterBTDF extends BTDF{

	

	private double etaIncoming;
	private double etaOutgoing;
	/**
	 * Computes the transmittance
	 * @param sr
	 * @return
	 */
	public double getFresnelTransmittance(ShadeRec sr){
		Vector normal = new Vector(sr.normal);
		double nDotD = normal.scale(-1.0).dot(sr.ray.direction);
		
		double eta;
		/*
		 * check if the ray hits the inside surface
		 */
		if (nDotD < 0.0) {//ray hits the inside of the surface
			normal  = normal.scale(-1.0);
			eta = etaIncoming/etaOutgoing;	
		}else {// ray hits the outside of the surface
			eta = etaOutgoing/etaIncoming;
		}
		
		double cosOfThetaIncident = nDotD;
		double temp = 1.0 - (1.0 - cosOfThetaIncident * cosOfThetaIncident)/ (eta*eta);
		double cosOfThetaTransmitted = Math.sqrt(temp);
		
		double reflectanceParallel = (eta*cosOfThetaIncident - cosOfThetaTransmitted)
							/(eta*cosOfThetaIncident + cosOfThetaTransmitted);
		double reflectancePerpendicular = (cosOfThetaIncident - eta*cosOfThetaTransmitted)
										/(cosOfThetaIncident + eta*cosOfThetaTransmitted);
		
		double reflectance = 0.5 * (reflectanceParallel*reflectanceParallel
				+ reflectancePerpendicular*reflectancePerpendicular);
		return 1.0 - reflectance;
	}
	
	/**
	 * Checks for total internal reflection.
	 * Total internal reflection occurs when:
	 * 		1 - (1-cos theta_i)^2/ eta < 0
	 * 		where eta = eta_transmitted/eta_incident
	 * 
	 */
	@Override
	public boolean  checkTotalInternalReflection(ShadeRec sr, Vector wo){
		double cosOfThetaIncident = sr.normal.dot(wo);
		double eta ;
		
		/*
		 * check if the ray hits the inside surface
		 */
		if (cosOfThetaIncident < 0.0) {//ray hits the inside of the surface
			eta = etaIncoming/etaOutgoing;	
		}else {// ray hits the outside of the surface
			eta = etaOutgoing/etaIncoming;
		}
		
		return (1.0 - (1.0 - cosOfThetaIncident * cosOfThetaIncident)/ (eta*eta) < 0.0);
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
		double eta;
			
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
			eta = etaIncoming/etaOutgoing;
		}else {// ray hits the outside of the surface
			eta = etaOutgoing/etaIncoming;
		}

		
		double temp = 1.0 - (1.0 - cosOfThetaIncoming * cosOfThetaIncoming)/ (eta*eta);
		
		
		double cosOfThetaTransmitted = Math.sqrt(temp);
		Vector wt = wo.scale(-1.0/eta).subtract(normal.scale(cosOfThetaTransmitted - cosOfThetaIncoming/eta)).normalize();
		sr.wt = wt;

		double scalingFactor = Math.abs(getFresnelTransmittance(sr))/(eta*eta);
	//	System.out.println("scalingFactor: "+scalingFactor);
		//return new RGBColor((float)scalingFactor);
		return RGBColor.WHITE.unboundedScale(scalingFactor);
	}

	public double getEtaIncoming() {
		return etaIncoming;
	}

	public void setEtaIncoming(double etaIncoming) {
		this.etaIncoming = etaIncoming;
	}

	public double getEtaOutgoing() {
		return etaOutgoing;
	}

	public void setEtaOutgoing(double etaOutgoing) {
		this.etaOutgoing = etaOutgoing;
	}
}
