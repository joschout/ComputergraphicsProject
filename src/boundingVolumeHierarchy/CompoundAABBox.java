package boundingVolumeHierarchy;

import java.util.ArrayList;
import java.util.List;

import math.Point;
import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class CompoundAABBox implements CompositeAABBox {

	public static final double kEpsilon = 1e-5;
	private List<CompositeAABBox> compositeAABBoxes;
	public Point p0;
	public Point p1;

	//public AABBox boundingBox;
	

	public CompoundAABBox(){
		compositeAABBoxes = new ArrayList<CompositeAABBox>();
		p0 =  new Point(Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE);
		p1 = new Point(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
	}
	
	
	

	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		if(this.intersectSurroundingBox(ray, sr) == false){
			return false;
		}
		
		double tmin = Double.MAX_VALUE;
		Vector normal = null;
		Point localHitPoint = null;

		for(CompositeAABBox box: compositeAABBoxes){
			if(box.intersect(ray, sr)){
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
		}
		if(sr.hasHitAnObject){
			sr.t = tmin;
			sr.normal = normal;
			sr.localHitPoint = localHitPoint;
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
		//return new AABBox(p0, p1, null);
		this.p0 = p0;
		this.p1 = p1;
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
	public Point getP0() {
		return this.p0;
	}

	@Override
	public Point getP1() {
		return this.p1;
	}
	
	public boolean intersectSurroundingBox(Ray ray, ShadeRec sr) {
		Point rayOrigin = ray.origin;
		Vector rayDirection = ray.direction;
		
		double tx_min, ty_min, tz_min;
		double tx_max, ty_max, tz_max; 

		double a = 1.0 / rayDirection.x;
		if (a >= 0) {
			tx_min = (p0.x - rayOrigin.x) * a;
			tx_max = (p1.x - rayOrigin.x) * a;
		}
		else {
			tx_min = (p1.x - rayOrigin.x) * a;
			tx_max = (p0.x - rayOrigin.x) * a;
		}
		
		double b = 1.0 / rayDirection.y;
		if (b >= 0) {
			ty_min = (p0.y - rayOrigin.y) * b;
			ty_max = (p1.y - rayOrigin.y) * b;
		}
		else {
			ty_min = (p1.y - rayOrigin.y) * b;
			ty_max = (p0.y - rayOrigin.y) * b;
		}
		
		double c = 1.0 / rayDirection.z;
		if (c >= 0) {
			tz_min = (p0.z - rayOrigin.z) * c;
			tz_max = (p1.z - rayOrigin.z) * c;
		}
		else {
			tz_min = (p1.z - rayOrigin.z) * c;
			tz_max = (p0.z - rayOrigin.z) * c;
		}
		
		double t0, t1;
	
		// find largest entering t value
		
		if (tx_min > ty_min){
			t0 = tx_min;
		}else{
			t0 = ty_min;	
		}
		if (tz_min > t0){
			t0 = tz_min;	
		}
		// find smallest exiting t value
			
		if (tx_max < ty_max){
			t1 = tx_max;
		}else{
			t1 = ty_max;
		}	
		if (tz_max < t1){
			t1 = tz_max;
		}	
		
		return (t0 < t1 && t1 > kEpsilon);
	}
}
