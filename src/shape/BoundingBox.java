package shape;

import util.RGBColor;
import util.ShadeRec;
import material.Material;
import math.Point;
import math.Ray;
import math.Vector;

public class BoundingBox implements Shape{
	public Point p0;
	public Point p1;
	public static final double kEpsilon = 0;
	
	public BoundingBox(Point p0, Point p1){
		if(p0 == null){
			throw new NullPointerException("the given point p0 is null");
		}
		this.p0 = p0;
		if(p1 == null){
			throw new NullPointerException("the given point p1 is null");
		}
		this.p1 = p1;
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		Point rayOrigin = ray.origin;
		Vector rayDirection = ray.direction;
		
		double txMin, tyMin, tzMin;
		double txMax, tyMax, tzMax;
		
		double a = 1.0/ rayDirection.x;
		if( a>= 0){
			txMin = (p0.x-rayOrigin.x) * a;
			txMax = (p1.x-rayOrigin.x) * a;
		}else{
			txMin = (p1.x-rayOrigin.x) * a;
			txMax = (p0.x-rayOrigin.x) * a;
		}
		
		double b = 1.0/ rayDirection.y;
		if( b>= 0){
			tyMin = (p0.y-rayOrigin.y) * b;
			tyMax = (p1.y-rayOrigin.y) * b;
		}else{
			tyMin = (p1.y-rayOrigin.y) * b;
			tyMax = (p0.y-rayOrigin.y) * b;
		}
		
		double c = 1.0/ rayDirection.z;
		if( c>= 0){
			tzMin = (p0.z-rayOrigin.z) * c;
			tzMax = (p1.z-rayOrigin.z) * c;
		}else{
			tzMin = (p1.z-rayOrigin.z) * c;
			tzMax = (p0.z-rayOrigin.z) * c;
		}

		double t0, t1;
		
		// find the largest entering t value
		if(txMin > tyMin){
			t0 = txMin;
		}else{
			t0 = tyMin;
		}
		if(tzMin > t0){
			t0 = tzMin;
		}
		
		// find the smallest exiting t value
		if(txMax < tyMax){
			t1 = txMax;
		}else{
			t1 = tyMax;
		}
		if(tzMax < t1){
			t1 = tzMax;
		}
		
		
		return (t0 < t1 || t1 > kEpsilon);
		
	}
	@Override
	public Material getMaterial() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RGBColor getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return this;
	}
	
	public boolean isInside(Point p){
			return ((p.x > p0.x && p.x < p1.x) && (p.y > p0.y && p.y < p1.y) && (p.z > p0.z && p.z < p1.z));
	}
	

}
