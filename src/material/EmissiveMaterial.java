package material;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class EmissiveMaterial extends Material{

	//radiance scaling factor
	public double ls;
	
	//emitted color
	public RGBColor ce;

	public EmissiveMaterial() {
		super();
		this.ls = 1;
		this.ce = new RGBColor(1);
	}
	
	
	public EmissiveMaterial(double ls, RGBColor ce) {
		super();
		this.ls = ls;
		this.ce = ce;
	}

	/**
	 * If the incoming ray is on the same side of the object as the normal,
	 * 	it returns the emitted radiance.
	 * Else it returns black
	 */
	@Override
	public RGBColor shade(ShadeRec sr) {
		if (sr.normal.scale(-1).dot(sr.ray.direction) > 0.0) {
			return ce.scale(ls);
		}
		else {
			return new RGBColor(0);
		}
	}

	public RGBColor getLe(ShadeRec sr){
		return ce.scale(ls);
	}
	
	public double getLs() {
		return ls;
	}

	public void setLs(double ls) {
		this.ls = ls;
	}

	public RGBColor getCe() {
		return ce;
	}

	public void setCe(RGBColor ce) {
		this.ce = ce;
	}

	@Override
	public RGBColor totalBRDF(ShadeRec sr, Vector wo, Vector wi) {
		return new RGBColor(0);
	}


	@Override
	public RGBColor shade2(ShadeRec sr) {
		if (sr.normal.scale(-1).dot(sr.ray.direction) > 0.0) {
			return ce.scale(ls);
		}
		else {
			return new RGBColor(0);
		}
	}
	
}
