package rayTracers;

import math.Ray;
import shape.World;
import util.RGBColor;
import util.ShadeRec;

public class DepthTracer extends Tracer {

	
private World world;
	
	public DepthTracer(World world) {
		this.world = world;
	}
	
	public RGBColor traceRay(Ray ray){
		ShadeRec sr = world.hitObjects(ray);
		
		if(sr.hasHitAnObject){
			int k = 15;
			double depth = world.camera.getOrigin().subtract(sr.hitPoint).length();
			//double grayValue = 1 - depth/ k;
			double grayValue = 1.0/Math.log(depth);
			//double grayValue = Math.exp(-depth);
			//sr.ray = ray;
			return RGBColor.clamp((float)grayValue, (float)grayValue, (float)grayValue);
			//return new RGBColor((float) grayValue);
		}
		else{
			return world.backgroundColor;
		}
	}
}
