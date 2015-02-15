package material;

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
public class Material {
	
	public RGBColor rayCastShade(ShadeRec sr){
		return new RGBColor(0);	
	}
	
	public RGBColor areaLightShade(ShadeRec sr){
		return new RGBColor(0);	
	}
	
	public RGBColor whittedShade(ShadeRec sr){
		return new RGBColor(0);	
	}
	
	public RGBColor pathShade(ShadeRec sr){
		return new RGBColor(0);	
	}
	
	public RGBColor shade(ShadeRec sr){
		return new RGBColor(0);	
	}

}
