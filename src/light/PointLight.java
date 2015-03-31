package light;

import math.Point;
import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class PointLight extends Light {
	
	private boolean castShadows = false;
	
	
	//radiance scaling factor ls, where ls is between [0, infinity).
	private double ls;
	
	private RGBColor color;
	
	private Point location;
	
	public PointLight(){
		this(1.0, new RGBColor(1), new Point());
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
		
		/*
		 * check for all objects in the scene:
		 * if the shadow ray in the direction to the light source starting from the hitpoint
		 * 		hits another object
		 * and if the distance to that object is a positive number t smaller than the distance d
		 * then the hit point lies in the shadow of that object under this light source.
		 */
		for(int j = 0; j < sr.world.intersectablesToIntersect.size(); j++){
			if(sr.world.intersectablesToIntersect.get(j).shadowHit(shadowRay, sr) && sr.t < d){
				return true;
			}
		}
		return false;
	}
	
	public void setCastShadows(boolean castShadows){
		this.castShadows = castShadows;
	}
	public boolean castShadows() {
		return castShadows;
	}

}
