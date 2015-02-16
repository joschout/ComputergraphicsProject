package rayTracers;

import math.Ray;
import shape.World;
import util.RGBColor;
import util.ShadeRec;

public class NormalFalseColorImagetracer extends Tracer {

	private World world;
	
	public NormalFalseColorImagetracer(World world) {
		this.world = world;
	}
	
	public RGBColor traceRay(Ray ray){
		ShadeRec sr = world.hitObjects(ray);
		
		if(sr.hasHitAnObject){
			//sr.ray = ray;
			return new RGBColor((float)(sr.normal.x+1)/2, (float)(sr.normal.y+1)/2, (float)(sr.normal.z+1)/2);
		}
		else{
			return world.backgroundColor;
		}
	}
}
