package brdf;

import util.RGBColor;
import util.ShadeRec;
import math.Vector;

/**
 * a BRDF to handle the reflection
 * @author Jonas
 *
 */
public class FresnelReflectorBRDF extends BRDF{
	
	private RGBColor color;
	
	private double etaIncoming;
	private double etaOutgoing;
	
	public FresnelReflectorBRDF() {
		color = RGBColor.WHITE;
		etaIncoming = 1.0;
		etaOutgoing = 1.0;
	}
	
	/**
	 * Computes the reflectance
	 * @param sr
	 * @return
	 */
	public double getFresnelReflectance(ShadeRec sr){
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
		
//		System.out.println("eta: "+eta);
		double cosOfThetaIncident = nDotD;
		double temp = 1.0 - (1.0 - cosOfThetaIncident * cosOfThetaIncident)/ (eta*eta);
//		System.out.println("temp: "+temp);
		double cosOfThetaTransmitted = Math.sqrt(temp);
//		System.out.println("cosOfThetaIncident: "+cosOfThetaIncident);
//		System.out.println("cosOfThetaTransmitted: "+cosOfThetaTransmitted);
//		
		double reflectanceParallel = (eta*cosOfThetaIncident - cosOfThetaTransmitted)
							/(eta*cosOfThetaIncident + cosOfThetaTransmitted);
	//	System.out.println("reflectanceParallel: "+reflectanceParallel);
		
		double reflectancePerpendicular = (cosOfThetaIncident - eta*cosOfThetaTransmitted)
										/(cosOfThetaIncident + eta*cosOfThetaTransmitted);
	//	System.out.println("reflectancePerpendicular"+reflectancePerpendicular);
		double reflectance = 0.5 * (reflectanceParallel*reflectanceParallel
				+ reflectancePerpendicular*reflectancePerpendicular);
	//	System.out.println("reflectance: " +reflectance);
		return reflectance;
	}

	@Override
	public RGBColor f(ShadeRec sr, Vector wo, Vector wi) {
		return color.scale(getFresnelReflectance(sr));
	}
	
	public void  setColor(RGBColor color){
		this.color = color;
	}

	@Override
	public RGBColor getReflectance(ShadeRec sr, Vector wo) {
		return RGBColor.BLACK;
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

	public RGBColor getColor() {
		return color;
	}
	
	
}
