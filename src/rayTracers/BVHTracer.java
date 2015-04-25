package rayTracers;

import main.World;
import math.Ray;
import util.RGBColor;
import util.ShadeRec;

public class BVHTracer extends Tracer {

	public BVHTracer(World world) {
		super(world);
	}
	
	public RGBColor traceRay(Ray ray){
		if(world.intersectablesToIntersect.isEmpty()){
			world.createBVH2();
		}
		
		ShadeRec sr = world.hitObjects(ray);
		sr.tracer = this;
		
		if(sr.hasHitAnObject){
			sr.ray = ray;
			
			return sr.material.shade2(sr);
		}
		else{
			return world.backgroundColor;
		}
	}
}
