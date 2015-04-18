package light;

import material.EmissiveMaterial;
import material.Material;
import math.Point;
import math.Ray;
import math.Vector;
import shape.Intersectable;
import shape.Shape;
import util.RGBColor;
import util.ShadeRec;

public class AreaLight extends Light {

	private boolean castShadows = false;
	public Point samplePoint; //sample point on light  source
	public Shape lightEmittingShape;
	public EmissiveMaterial emissiveMaterial;
	public Vector normalAtSamplePoint;
	public Vector wi; //unit vector from hitpoint to samplepoint
	
	public static int nbOfShadowRaysPerAreaLight = 4;
	
	
	/**
	 * Returns the vector wi from the point being shaded to the sample point on the light source0
	 * 
	 * Also stores	- the sample point
	 * 				- the normal at the sample point
	 * 				- wi
	 * Why the multitasking?
	 * 	=> a given samplepoint can only be accessed once,
	 * 		and the information is necessary in the othe methods.
	 */
	@Override
	public Vector getDirectionOfIncomingLight(ShadeRec sr) {
		this.samplePoint = this.lightEmittingShape.sample();
		this.normalAtSamplePoint = this.lightEmittingShape.getNormal(this.samplePoint);
		this.wi = this.samplePoint.subtract(sr.hitPoint).normalize();
		return this.wi;
	}

	@Override
	public RGBColor getRadiance(ShadeRec sr) {
		double ndotd = normalAtSamplePoint.scale(-1).dot(this.wi);
		
		if (ndotd > 0.0) {
			return emissiveMaterial.getLe(sr);
		}else {
			return new RGBColor(0);
		}
	}

	public void setCastShadows(boolean castShadows){
		this.castShadows = castShadows;
	}
	public boolean castShadows() {
		return castShadows;
	}
	
	public double pdf(ShadeRec sr){
		return lightEmittingShape.pdf(sr);
	}
	
	public double G(ShadeRec sr) {
		double ndotd = normalAtSamplePoint.scale(-1).dot(wi);
		double d2 = samplePoint.subtract(sr.hitPoint).lengthSquared();
		return ndotd;
		//return ndotd/d2;
	}

	/**
	 * test for this light source if there are objects between the hit point and the
	 * location of the light source
	 */
	@Override
	public boolean inShadow(Ray shadowRay, ShadeRec sr) {
		// d = the length between the hit point and the sample point on the light source
				double d = samplePoint.subtract(shadowRay.origin).length();
				
				/*
				 * check for all objects in the scene:
				 * if the shadow ray in the direction to the light source starting from the hitpoint
				 * 		hits another object
				 * and if the distance to that object is a positive number t smaller than the distance d
				 * then the hit point lies in the shadow of that object under this light source.
				 */
				for(Intersectable intersectable: sr.world.intersectablesToIntersect){
					if(intersectable.shadowHit(shadowRay, sr) && sr.t < d){
						return true;
					}
				}
				return false;
	}

	@Override
	public RGBColor handleMaterialShading(Material material, ShadeRec sr,
			Vector wo) {

		RGBColor partialLOfThisLightSource = new RGBColor(0);	
//		System.out.println("//============== START SAMPLING =============//");	
		for (int i = 0; i < nbOfShadowRaysPerAreaLight; i++) {
			Vector wi = this.getDirectionOfIncomingLight(sr);
			double ndotwi = sr.normal.dot(wi);
			double ndotwo = sr.normal.dot(wo);
			if(ndotwi > 0.0 && ndotwo > 0.0){
				//shadow testing: does the hitpoint lie in the shadow of light source j ?
				boolean inShadow = false;
				if(this.castShadows()){// if light source j casts shadows
					// new shadow ray with as origin the hit point and as direction 
					// the direction of the incoming light
					Ray shadowRay = new Ray(sr.hitPoint, wi); 
					/*
					 * test for this light source if there are objects between the hit point and the
					 * location of the light source
					 */
					inShadow = this.inShadow(shadowRay, sr);
				}
				if(! inShadow){
					/*
					 * if the point doesn't lie in the shadow of light source j
					 * then add the radiance of light source j to the total radiance.
					 */
					RGBColor temp = material.totalBRDF(sr, wo, wi)
							.multiply(this.getRadiance(sr).scale(this.G(sr)*ndotwi/this.pdf(sr)));
							
					partialLOfThisLightSource = partialLOfThisLightSource.unboundedAdd(temp);
					
//					System.out.println("material.totalBRDF(sr, wo, wi); "+material.totalBRDF(sr, wo, wi));
//					System.out.println("this.getRadiance(sr); " +this.getRadiance(sr));
//					System.out.println("this.G(sr); "+ this.G(sr));
//					System.out.println("ndotwi; " + ndotwi);
//					System.out.println("1.0/this.pdf(sr); "+ 1.0/this.pdf(sr));
//					System.out.println("temp: " + temp.toString());
//					System.out.println("partialLOfThisLightSource; "+ partialLOfThisLightSource);
//					System.out.println("------------------------------------");
//					
		
//					System.out.println("diffuse color from light " + j  + " + previous color");
//					System.out.println(L.toString());
				 }
			}	
		}
		RGBColor temp2 = partialLOfThisLightSource.scale(1.0/nbOfShadowRaysPerAreaLight);
//		System.out.println("temp2; "+ temp2);
//		System.out.println("//================= End Sampling ===================//");
		return temp2;
	}

}
