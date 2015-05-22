package rayTracers;

import main.World;
import math.Ray;
import util.RGBColor;
import util.ShadeRec;

public abstract class Tracer {

	protected World world;
	
	public Tracer(){
		world = null;
	}
	
	public Tracer(World world){
		this.world = world;
	}
	
	public abstract RGBColor traceRay(Ray ray);
	
	public RGBColor traceRaycolorFilter(Ray ray, ShadeRec srIn){
		return RGBColor.BLACK;
	}
	
}
