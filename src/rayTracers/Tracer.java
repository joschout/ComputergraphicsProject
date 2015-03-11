package rayTracers;

import main.World;
import math.Ray;
import util.RGBColor;

public class Tracer {

	protected World world;
	
	public Tracer(){
		world = null;
	}
	
	public Tracer(World world){
		this.world = world;
	}
	
	public RGBColor traceRay(Ray ray){
		return new RGBColor(0);
	}
	
}
