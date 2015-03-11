package rayTracers;

import main.World;
import math.Ray;
import util.RGBColor;
import util.ShadeRec;

public class NormalBVHTracer extends Tracer{
private World world;
	
	public NormalBVHTracer(World world) {
		this.world = world;
	}
	
	public RGBColor traceRay(Ray ray){
		if(world.bvh == null){
			world.setBVH(world.createBVH());
		}
		
		ShadeRec sr = world.hitBVH(ray);
		
		if(sr.hasHitAnObject){
			sr.ray = ray;
			//return sr.material.shade(sr);
			return new RGBColor((float)(sr.normal.x+1)/2, (float)(sr.normal.y+1)/2, (float)(sr.normal.z+1)/2);
		}
		else{
			return world.backgroundColor;
		}
	}
}
