package rayTracers;

import main.World;
import math.Ray;
import util.RGBColor;
import util.ShadeRec;

public class BVHTracer extends Tracer {

	private World world;
	//public int maxBHVCounter;
	
	
	public BVHTracer(World world) {
		this.world = world;
	}
	
	public RGBColor traceRay(Ray ray){
		if(world.intersectablesToIntersect.isEmpty()){
			world.createBVH2();
		}
		
		ShadeRec sr = world.hitObjects(ray);
		
		if(sr.hasHitAnObject){
			sr.ray = ray;
			
			return sr.material.shade2(sr);
		}
		else{
			return world.backgroundColor;
		}
	}
}
