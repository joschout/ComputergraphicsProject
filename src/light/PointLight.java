package light;

import material.Material;
import math.Point;
import math.Ray;
import math.Vector;
import shape.Intersectable;
import util.RGBColor;
import util.ShadeRec;

public class PointLight extends Light {
	
	//radiance scaling factor ls, where ls is between [0, infinity).
	private double ls;
	
	private RGBColor color;
	
	private Point location;
	
	public PointLight(){
		this(1.0, RGBColor.WHITE, new Point());
	}
	
	public PointLight(double ls, RGBColor rgbColor, Point location){
		super();
		this.ls = ls;
		this.color = rgbColor;
		this.location = location;
	}
	
	/**
	 * Gets the direction of the incoming light at a hit point.
	 * @param sr
	 * @return
	 */
	public Vector getDirectionOfIncomingLight(ShadeRec sr){
		return location.subtract(sr.hitPoint).normalize();
	}
	
	/**
	 * Returns the incident radiance at a hit point.
	 * @param sr
	 * @return
	 */
	public RGBColor getRadiance(ShadeRec sr){
		//return color.scale(ls/location.subtract(sr.hitPoint).lengthSquared());
		return color.scale(ls);
	}
	
	public void scaleRadiance(double ls){
		this.ls=ls;
	}
	
	public void setLocation(Point location){
		this.location = location;
	}
	
	/**
	 * test for this light source if there are objects between the hit point and the
	 * location of the light source
	 */
	public boolean inShadow(Ray shadowRay, ShadeRec sr) {
		// d = the length between the hit point and the light source
		double d = location.subtract(shadowRay.origin).length();
		return sr.world.shadowHitObjects(shadowRay, d);
	}	
	
	public RGBColor handleMaterialShading(Material material, ShadeRec sr, Vector wo){
		Vector wi = this.getDirectionOfIncomingLight(sr);
		double ndotwi = sr.normal.dot(wi);
		double ndotwo = sr.normal.dot(wo);
				
		RGBColor partialLOfThisLightSource = new RGBColor(0);
		
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
				partialLOfThisLightSource = partialLOfThisLightSource.add(material.totalBRDF(sr, wo, wi)
						.multiply(this.getRadiance(sr).scale(ndotwi)));
			
//				partialLOfThisLightSource = partialLOfThisLightSource.add(material.totalBRDF(sr, wo, wi)
//						.multiply(this.getRadiance(sr)));
//				System.out.println("diffuse color from light " + j  + " + previous color");
//				System.out.println(L.toString());
			 }
		}
		return partialLOfThisLightSource;
	}

}
