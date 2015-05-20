package brdf;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public abstract class BRDF {

	public abstract RGBColor f(ShadeRec sr, Vector wo, Vector wi);
	
	
	/**
	 * cfr rho function in the book
	 * @param sr
	 * @param wo
	 * @return
	 */
	public abstract RGBColor getReflectance(ShadeRec sr, Vector wo);
	
	

	
}
