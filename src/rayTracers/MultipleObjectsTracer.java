package rayTracers;

import util.RGBColor;
import util.ShadeRec;
import main.World;
import math.Ray;

public class MultipleObjectsTracer extends Tracer {

	private World world;
	
	public MultipleObjectsTracer(World world) {
		this.world = world;
	}
	
	public RGBColor traceRay(Ray ray){
		ShadeRec sr = world.hitObjects(ray);
		
		if(sr.hasHitAnObject){
			sr.ray = ray;
			return sr.material.shade(sr);
		}
		else{
			return world.backgroundColor;
		}
	}
}
