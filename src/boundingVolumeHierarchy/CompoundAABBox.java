package boundingVolumeHierarchy;

import java.util.ArrayList;
import java.util.List;

import math.Point;
import math.Ray;
import math.Vector;
import shape.Shape;
import util.ShadeRec;

public class CompoundAABBox extends CompositeAABBox {
	
	private List<CompositeAABBox> compositeAABBoxes;

	public CompoundAABBox(){
		compositeAABBoxes = new ArrayList<CompositeAABBox>();
		p0 =  new Point(Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE);
		p1 = new Point(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
		midpoint = new Point(0,0,0);
	}
	
	
//	public boolean intersect2(Ray ray, ShadeRec sr){
//		if this != hit return false;
//		
//		else
//			verhoog de gehitteboxteller
//			onthoudt de t voor deze box
//			check de subboxes van deze box:
//				voor subbox box:
//					if box != hit return false
//					else verhoog de gehitteboxteller
//					check de subboxes van deze box:
//						voor subbox box:
//							...
//					als er een object in de subbox gehit is
//						check of het object voor de eerder gehitte objecten is
//						sla het object en de t-waarde tijdelijk op in een variabele
//						
//				
//	}
	
	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		sr.bvhCounter ++;
		double thisBoxT = this.getSurroundingBoxIntersectionT(ray);
		if(thisBoxT < kEpsilon ){
			return false;
		}	
		
		if(sr.hasHitAnObject && sr.t < thisBoxT){
			return false;
		}

		double tmin = sr.t;
		//double tmin = Double.MAX_VALUE;
		Vector normal = null;
		Point localHitPoint = null;
		
		//double tSubBoxMin = Double.MAX_VALUE;
		
		// check alle subboxes voor deze box
		for(CompositeAABBox box: compositeAABBoxes){
			//double tSubBox = box.getSurroundingBoxIntersectionT(ray);
			//if(tSubBox < tSubBoxMin){
			
					if(box.intersect(ray, sr)){
						//als sr.t kleiner is dan die tot nu toe gevonden
						if(sr.t < tmin){
							sr.hasHitAnObject = true;
							sr.ray = ray;	
							sr.hitPoint = ray.origin.add(ray.direction.scale(sr.t));
		
							//deze drie worden door de intersect functie van een shape aangepast
							// en moeten we tijdelijk lokaal opslaan
							tmin = sr.t;
							normal = sr.normal;
							localHitPoint = sr.localHitPoint;	
						}
					}
//			}
			
		}
		
		//sr.tbox = tForThisBox;
		if(sr.hasHitAnObject){
			sr.t = tmin;
			sr.normal = normal;
			sr.localHitPoint = localHitPoint;
			return true;
		}
		return false;
	}

	
	@Override
	public boolean shadowHit(Ray shadowRay, ShadeRec sr) {
		sr.bvhCounter ++;
		double thisBoxT = this.getSurroundingBoxIntersectionT(shadowRay);
		if(thisBoxT < kEpsilon ){
			return false;
		}	
		
		if(sr.hasHitAnObject && sr.t < thisBoxT){
			return false;
		}

		boolean shadowhit = false;
		double tmin = sr.t;
		
		// check alle subboxes voor deze box
		for(CompositeAABBox box: compositeAABBoxes){
			if(box.shadowHit(shadowRay, sr)){
				//als sr.t kleiner is dan die tot nu toe gevonden
				if(sr.t < tmin){
					//deze drie worden door de intersect functie van een shape aangepast
					// en moeten we tijdelijk lokaal opslaan
					shadowhit = true;
					tmin = sr.t;
				}
			}
		}
		
		//sr.tbox = tForThisBox;
		if(shadowhit){
			sr.t = tmin;
			return true;
		}
		return false;
	}
	
	
	public void addObject(CompositeAABBox box){
		compositeAABBoxes.add(box);		
	}
	
	public void getBoundingBoxFromScratch(){
		Point p0 = computeMinCoords();
		Point p1 = computeMaxCoords();
		this.p0 = p0;
		this.p1 = p1;
		calculateMidpoint();
	}
	
	private Point computeMinCoords(){
		double p0X = Double.MAX_VALUE;
		double p0Y = Double.MAX_VALUE;
		double p0Z = Double.MAX_VALUE;

		for(CompositeAABBox box: compositeAABBoxes){
			if(box.getP0().x < p0X){
				p0X = box.getP0().x;
			}
			if(box.getP0().y < p0Y){
				p0Y = box.getP0().y;
			}
			if(box.getP0().z < p0Z){
				p0Z = box.getP0().z;
			}	
		}
		return new Point(p0X - kEpsilon, p0Y -kEpsilon, p0Z -kEpsilon);
	}

	private Point computeMaxCoords(){
		double p1X = -Double.MAX_VALUE;
		double p1Y = -Double.MAX_VALUE;
		double p1Z = -Double.MAX_VALUE;

		for(CompositeAABBox box: compositeAABBoxes){
			if(box.getP1().x > p1X){
				p1X = box.getP1().x;
			}
			if(box.getP1().y > p1Y){
				p1Y = box.getP1().y;
			}
			if(box.getP1().z > p1Z){
				p1Z = box.getP1().z;
			}	
		}
		return new Point(p1X + kEpsilon, p1Y + kEpsilon, p1Z + kEpsilon);
	}
	
	@Override
	public boolean isInfinite() {
		return false;
	}	
}
