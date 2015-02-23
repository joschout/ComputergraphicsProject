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
	
	public boolean inShadow(Ray shadowRay, ShadeRec sr) {
		double d = location.subtract(shadowRay.origin).length();
		
		for(int j = 0; j < sr.world.shapes.size(); j++){
			if(sr.world.shapes.get(j).shadowHit(shadowRay, sr) && sr.t < d){
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
