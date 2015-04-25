package light;

import material.Material;
import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public abstract class Light {
	
	private boolean castShadows = false;
	
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

	public void setCastShadows(boolean castShadows){
		this.castShadows = castShadows;
	}
	
	public boolean castShadows() {
		return castShadows;
	}

	/**
	 * test for this light source if there are objects between the hit point and the
	 * location of the light source
	 */
	public abstract boolean inShadow(Ray shadowRay, ShadeRec sr);
	
	
	public abstract RGBColor handleMaterialShading(Material material, ShadeRec sr, Vector wo);

}
