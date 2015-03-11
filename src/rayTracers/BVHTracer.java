package rayTracers;

import main.World;
import math.Ray;
import util.RGBColor;
import util.ShadeRec;

public class BVHTracer extends Tracer {

	private World world;
	
	public BVHTracer(World world) {
		this.world = world;
	}
	
	public RGBColor traceRay(Ray ray){
		if(world.bvh == null){
			world.setBVH(world.createBVH());
		}
		
		ShadeRec sr = world.hitBVH(ray);
		
		if(sr.hasHitAnObject){
			sr.ray = ray;
			return sr.material.shade(sr);
			
			
//			//==================//
//			
//			int k = 15;
//			double depth = world.camera.getOrigin().subtract(sr.hitPoint).length();
//			//double grayValue = 1 - depth/ k;
//			double grayValue = 1.0/Math.log(depth);
//			//double grayValue = Math.exp(-depth);
//			//sr.ray = ray;
//			return RGBColor.clamp((float)grayValue, (float)grayValue, (float)grayValue);
//			//return new RGBColor((float) grayValue);
//			//==================//
		}
		else{
			return world.backgroundColor;
		}
	}
}
