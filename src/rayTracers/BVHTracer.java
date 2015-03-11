package rayTracers;

import main.World;
import math.Ray;
import util.RGBColor;
import util.ShadeRec;

public class BVHTracer extends Tracer {

	private World world;
	public int maxBHVCounter;
	
	
	public BVHTracer(World world) {
		this.world = world;
	}
	
	public RGBColor traceRay(Ray ray){
		if(world.bvh == null){
			world.setBVH(world.createBVH());
		}
		
		ShadeRec sr = world.hitBVH(ray);
		
		if(sr.bvhCounter > world.maxBVHCounter){
			world.maxBVHCounter = sr.bvhCounter;
		}
		
		if(sr.hasHitAnObject){
			sr.ray = ray;
			
			return sr.material.shade(sr);
		}
		else{
			return world.backgroundColor;
		}
	}
}
