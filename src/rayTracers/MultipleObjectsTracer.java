package rayTracers;

import java.awt.Color;

import shape.Shape;
import util.ShadeRec;
import math.Ray;

public class MultipleObjectsTracer extends Tracer {

	
	public Color traceRay(Ray ray){
		ShadeRec sr = world.hitObjects(ray);
		
		if(sr.hasHitAnObject){
			sr.ray = ray;
			return sr.material.shade(sr);
			
			
		}
		else{
			return world.backgroundColor;
		}
		
		
		
//		boolean hit = false;
//		for (Shape shape : super.world.shapes){
//			if (shape.intersect(ray)) {
//				hit = true;
//				return shape.color;
//			}
//		}
		
		
		
		return Color.BLACK;
	}
}
