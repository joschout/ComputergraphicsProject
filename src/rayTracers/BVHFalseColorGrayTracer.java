package rayTracers;

import java.awt.Color;

import main.World;
import math.Ray;
import util.RGBColor;
import util.ShadeRec;

public class BVHFalseColorGrayTracer extends Tracer{

	private World world;
	//private static int maxValueOfIntersectionsClamper = 790;
		
		public BVHFalseColorGrayTracer(World world) {
			this.world = world;
		}
		
		public RGBColor traceRay(Ray ray){
			if(world.intersectablesToIntersect.isEmpty()){
				world.createBVH2();
			}
			
			ShadeRec sr = world.hitObjects(ray);
			
			if(sr.hasHitAnObject){
				//int k = 15;
				int depth = sr.bvhCounter;
				//System.out.println(depth);
				//double grayValue = 1 - depth/ k;
				double grayValue = 1.0/Math.log(depth);
				
				//double grayValue = Math.exp(-depth);
//				//sr.ray = ray;
//				System.out.println(sr.bvhCounter);
//				
//				//BLUE = minimum
//				if(depth < 0){
//					throw new IllegalArgumentException("number of intersections is smaller than 0");
//				}
//				if(depth <maxValueOfIntersectionsClamper/3){
//					
//					int G = (255*3/maxValueOfIntersectionsClamper)*depth;
//					int B = 255 - (765/maxValueOfIntersectionsClamper)*depth;
//					//System.out.println("G: " + G + ", B: " + B);
//					return RGBColor.convertToRGBColor(new Color(0, G, B));
//				}
//				//GREEN = in between
//				if(depth < maxValueOfIntersectionsClamper*2/3){
//					int R = (765/maxValueOfIntersectionsClamper)*(depth-maxValueOfIntersectionsClamper/3);
//					int G =  255 - (765/maxValueOfIntersectionsClamper)*(depth-maxValueOfIntersectionsClamper/3);
//					//System.out.println("R: " + R + ", G: " + G);
//					return RGBColor.convertToRGBColor(new Color(R, G , 0));
//				}else{
//					return RGBColor.convertToRGBColor(
//							new Color(255, 0, 0));
//				}
//				
//				@Override
//				public Color trace(Ray ray) {
//					ShadingInfo si = this.world.intersect(ray);
//					double maxComponentValue = (si.world.numberOfBoxes/200)/3;
//					if(si.numberOfBoxIntersectionTests > maxComponentValue*2)
//						return new Color((si.numberOfBoxIntersectionTests - maxComponentValue*2)/maxComponentValue, 1 - ((si.numberOfBoxIntersectionTests - maxComponentValue*2)/maxComponentValue), 0);
//					else if(si.numberOfBoxIntersectionTests > maxComponentValue)
//						return new Color(0, (si.numberOfBoxIntersectionTests - maxComponentValue)/maxComponentValue, 1 - ((si.numberOfBoxIntersectionTests - maxComponentValue)/maxComponentValue));
//					else
//						return new Color(1 - (si.numberOfBoxIntersectionTests/maxComponentValue), 1 - (si.numberOfBoxIntersectionTests/maxComponentValue), 1);
//				}
				
				
				
				//RED = maximum
				
				
				//
		
				
				return RGBColor.clamp((float)grayValue, (float)grayValue, (float)grayValue);
				//return 
				//return new RGBColor((float) grayValue);
			}
			else{
				return world.backgroundColor;
			}
		}
}
