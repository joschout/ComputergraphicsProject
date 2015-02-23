package light;

import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class Light {
	
	protected Boolean shadows;
	
	/**
	 * Gets the direction of the incoming light at a hit point.
	 * @param sr
	 * @return
	 */
	public Vector getDirectionOfIncomingLight(ShadeRec sr){
		return new Vector();
	}
	
	/**
	 * Returns the incident radiance at a hit point.
	 * 
	 * This is the L-function in the handbook
	 * 
	 * @param sr
	 * @return
	 */
	public RGBColor getRadiance(ShadeRec sr){
		return new RGBColor(0);
	}

	public boolean castShadows() {
		return false;
	}

	public boolean inShadow(Ray shadowRay, ShadeRec sr) {
		// TODO Auto-generated method stub
		return false;
	}

}
