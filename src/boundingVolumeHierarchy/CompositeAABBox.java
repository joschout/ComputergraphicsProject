package boundingVolumeHierarchy;
import math.Point;
import math.Ray;
import math.Vector;
import shape.Intersectable;

public abstract  class CompositeAABBox implements Intersectable{
	
	public static final double kEpsilon = 1e-5;
	public Point p0;
	public Point p1;
	public Point midpoint;

	
	public Point getP0() {
		return this.p0;
	}

	
	public Point getP1() {
		return this.p1;
	}
	
	
	public void calculateMidpoint(){
		double x;
		if(getP0().x < getP1().x){
			x = getP0().x + (getP1().x - getP0().x) /2;
		}else{
			x = getP1().x + (getP0().x - getP1().x)/2;
		}
		
		double y;
		if(getP0().y < getP1().y){
			y = getP0().y +(getP1().y - getP0().y) /2;
		}else{
			y = getP1().y + (getP0().y - getP1().y)/2;
		}
		
		double z;
		if(getP0().z < getP1().z){
			z = getP0().z  + (getP1().z - getP0().z) /2;
		}else{
			z = getP1().z + (getP0().z - getP1().z)/2;
		}
	
		midpoint = new Point(x, y, z);
		
	}
	public Point getMidpoint(){
		if(midpoint == null){
			calculateMidpoint();
			return midpoint;
		} else{
			return midpoint;
		}
	}
	
	/**
	 * returns -1 if this box IS NOT intersected by the given ray
	 *  --> test if bigger than 0 or kEpsilon
	 * 
	 * @param ray
	 * @return
	 */
	public double getSurroundingBoxIntersectionT(Ray ray){
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
		
		if(t0 < t1 && t1 > kEpsilon){//condition for a hit
//			if(t0 > kEpsilon){
				return t0;
//			}else{
//				return t1;
//				
//			}
		}
		return -1;
	}
	
	public boolean isInside(Point p){
		return (((p.x > p0.x && p.x < p1.x) || ( p.x > p1.x && p.x < p0.x))
				&& (p.y > p0.y && p.y < p1.y) || ( p.y > p1.y && p.y < p0.y)
				&& (p.z > p0.z && p.z < p1.z) || ( p.z > p1.z && p.z < p0.z));
	}
	
	public String toString(){ 
		return "CompositeAABox with: \n"
				+ "   P0: (" + getP0().x + ", " + getP0().y + ", " + getP0().z+ ") \n"
				+ "   P1: (" + getP1().x + ", " + getP1().y + ", " + getP1().z+ ") \n"
				+  "   midpoint: (" + midpoint.x + ", " + midpoint.y + ", " + midpoint.z+ ") \n";
	}
}
