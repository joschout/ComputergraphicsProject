package brdf;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public abstract class BRDF {

	public abstract RGBColor f(ShadeRec sr, Vector wo, Vector wi);
	
	

}
