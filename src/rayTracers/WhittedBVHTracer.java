package rayTracers;

import main.World;
import math.Ray;
import util.RGBColor;
import util.ShadeRec;

public class WhittedBVHTracer extends Tracer {

	private double kHugeValue = Double.MAX_VALUE;//1e100;

	public WhittedBVHTracer(World world) {
		super(world);
	}
	
	@Override
	public RGBColor traceRay(Ray ray) {
		if (ray.recursionDepth > world.getMaxRecursionDepth()) {
			return RGBColor.BLACK;
		}
		
		if(world.intersectablesToIntersect.isEmpty()){
			world.createBVH2();
		}
		
		ShadeRec sr = world.hitObjects(ray);
		sr.tracer = this;
		
		if(sr.hasHitAnObject){
			sr.depth = ray.recursionDepth;
			sr.ray = ray;
			
			return sr.material.shade2(sr);
		}
		else{
			return world.backgroundColor;
		}
	}
	
	@Override
	public RGBColor traceRaycolorFilter(Ray ray, ShadeRec srIn) {
		if (ray.recursionDepth > world.getMaxRecursionDepth()) {
			return RGBColor.BLACK;
		}
		
		if(world.intersectablesToIntersect.isEmpty()){
			world.createBVH2();
		}
		
		ShadeRec sr = world.hitObjects(ray);
		sr.tracer = this;
		
		if(sr.hasHitAnObject){
			sr.depth = ray.recursionDepth;
			sr.ray = ray;
			srIn.t = sr.t;
			return sr.material.shade2(sr);
		}
		else{
			srIn.t = kHugeValue ;
			return world.backgroundColor;
		}
	}

}
