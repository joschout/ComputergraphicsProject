package brdf;

import math.Vector;

import util.RGBColor;

public abstract class BRDF {

	public abstract RGBColor f(Vector w0, Vector w1);
	
	

}
