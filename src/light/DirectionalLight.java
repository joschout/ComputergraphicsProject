//package light;
//
//import math.Ray;
//import math.Vector;
//import util.RGBColor;
//import util.ShadeRec;
//
//public class DirectionalLight extends Light {
//	
//	//radiance scaling factor ls, where ls is between [0, infinity).
//	private double ls;
//	
//	private RGBColor color;
//	
//	private Vector direction;
//	
//	/**
//	 * Gets the direction of the incoming light at a hit point.
//	 * @param sr
//	 * @return
//	 */
//	public Vector getDirectionOfIncomingLight(ShadeRec sr){
//		return direction;
//	}
//	
//	/**
//	 * Returns the incident radiance at a hit point.
//	 * @param sr
//	 * @return
//	 */
//	public RGBColor getRadiance(ShadeRec sr){
//		return color.scale(ls);
//	}
//
//	@Override
//	public boolean castShadows() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean inShadow(Ray shadowRay, ShadeRec sr) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//}
