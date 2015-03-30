package rayTracers;

import main.World;
import math.Ray;
import util.RGBColor;

public abstract class Tracer {

	protected World world;
	
	public Tracer(){
		world = null;
	}
	
	public Tracer(World world){
		this.world = world;
	}
	
	public abstract RGBColor traceRay(Ray ray);
	
}
