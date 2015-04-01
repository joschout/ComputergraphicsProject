package material;

import math.Vector;
import util.RGBColor;
import util.ShadeRec;

/**
 * Material superclass. 
 * Each material stores the required BRDFs and defines a number of shading functions.
 * There is a shading function per tracer type.
 * 
 * NOTE: the shade functions only handle directional and point lights.
 * 
 * @author Jonas
 *
 */
public abstract class Material {
	
//	public RGBColor rayCastShade(ShadeRec sr){
//		return new RGBColor(0);	
//	}
//	
//	public RGBColor areaLightShade(ShadeRec sr){
//		return new RGBColor(0);	
//	}
//	
//	public RGBColor whittedShade(ShadeRec sr){
//		return new RGBColor(0);	
//	}
//	
//	public RGBColor pathShade(ShadeRec sr){
//		return new RGBColor(0);	
//	}
	
	public abstract RGBColor shade(ShadeRec sr);
	
	public abstract RGBColor shade2(ShadeRec sr);
	
	public abstract RGBColor totalBRDF(ShadeRec sr, Vector wo, Vector wi) ;

}
