package light;


import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class AmbientLight extends Light {
	
	//radiance scaling factor ls, where ls is between [0, infinity).
	private double ls;
	
	private RGBColor color;

	/**
	 * Creates a default ambient light source
	 */
	public AmbientLight(){
		super();
		this.ls = 1.0;
		this.color = new RGBColor(1);
	}
	
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
	 * @param sr
	 * @return
	 */
	public RGBColor getRadiance(ShadeRec sr){
		return color.scale(ls);
	}

	@Override
	public boolean castShadows() {
		return false;
	}

	@Override
	public boolean inShadow(Ray shadowRay, ShadeRec sr) {
		return false;
	}
}
