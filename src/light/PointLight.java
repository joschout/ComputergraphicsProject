package light;

import math.Point;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class PointLight extends Light {
	
	
	//radiance scaling factor ls, where ls is between [0, infinity).
	private double ls;
	
	private RGBColor color;
	
	private Point location;
	
	public PointLight(){
		super();
		this.ls = 1.0;
		this.color = new RGBColor(1);
		this.location = new Point();
	}
	
	
	
	/**
	 * Gets the direction of the incoming light at a hit point.
	 * @param sr
	 * @return
	 */
	public Vector getDirectionOfIncomingLight(ShadeRec sr){
		return location.subtract(sr.localHitPoint).normalize();
	}
	
	/**
	 * Returns the incident radiance at a hit point.
	 * @param sr
	 * @return
	 */
	public RGBColor getRadiance(ShadeRec sr){
		return color.scale(ls);
	}
	
	public void scaleRadiance(double ls){
		this.ls=ls;
	}
	
	public void setLocation(Point location){
		this.location = location;
	}

}
