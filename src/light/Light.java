package light;

import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public abstract class Light {
	
	protected Boolean shadows;
	
	/**
	 * Gets the direction of the incoming light at a hit point.
	 * @param sr
	 * @return
	 */
	public abstract Vector getDirectionOfIncomingLight(ShadeRec sr);
	
	
	/**
	 * Returns the incident radiance at a hit point.
	 * 
	 * This is the L-function in the handbook
	 * 
	 * @param sr
	 * @return
	 */
	public abstract RGBColor getRadiance(ShadeRec sr);

	public abstract boolean castShadows();

	public abstract boolean inShadow(Ray shadowRay, ShadeRec sr);

}
