package light;

import material.EmissiveMaterial;
import material.Material;
import material.SVEmissiveMaterial;
import material.SVMatteMaterial;
import math.Point;
import math.Ray;
import math.Vector;
import sampling.HemisphereSampleFactory;
import shape.Shape;
import texture.Texture;
import util.RGBColor;
import util.ShadeRec;

public class EnvironmentLight extends Light {
	
	
	public Point hemisphereSamplePoint; //sample point on light  source
//	public Sphere lightEmittingShape;
	public SVEmissiveMaterial emissiveMaterial;
	public Vector normalAtSamplePoint;
	public Vector wi; //unit vector from hitpoint to samplepoint
	public int nbOfShadowRaysPerEnvironmentLight;
	public HemisphereSampleFactory hemisphereSampleFactory;
	
	
	/**
	 * Returns the vector wi from the point being shaded to the sample point on the light source
	 * 
	 * Also stores	- the sample point
	 * 				- the normal at the sample point
	 * 				- wi
	 * Why the multitasking?
	 * 	=> a given sample point can only be accessed once,
	 * 		and the information is necessary in the other methods.
	 */
	@Override
	public Vector getDirectionOfIncomingLight(ShadeRec sr) {
		this.wi = this.hemisphereSampleFactory.getNextSample();
		this.normalAtSamplePoint = wi.scale(-1);
		return this.wi;
	}

	@Override
	public RGBColor getRadiance(ShadeRec sr) {
		return emissiveMaterial.getLe(sr);
	}

	/**
	 * test for this light source if there are objects between the hit point and the
	 * location of the light source
	 */
	@Override
	public boolean inShadow(Ray shadowRay, ShadeRec sr) {
		// d = the length between the hit point and the sample point on the light source
		// d = the length between the hit point and the sample point on the light source
		double d = Double.MAX_VALUE;
		return sr.world.shadowHitObjects(shadowRay, d);
	}

	@Override
	public RGBColor handleMaterialShading(Material material, ShadeRec sr,
			Vector wo) {
		
		this.hemisphereSampleFactory = new HemisphereSampleFactory(sr.normal);

		RGBColor partialLOfThisLightSource = RGBColor.BLACK;	
//		System.out.println("//============== START SAMPLING =============//");	
		for (int i = 0; i < nbOfShadowRaysPerEnvironmentLight; i++) {
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
							.multiply(this.getRadiance(sr).scale(Math.PI));
							
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
		RGBColor temp2 = partialLOfThisLightSource.scale(1.0/nbOfShadowRaysPerEnvironmentLight);
//		System.out.println("temp2; "+ temp2);
//		System.out.println("//================= End Sampling ===================//");
		return temp2;
	}

}
