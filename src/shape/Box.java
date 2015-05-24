package shape;

import boundingVolumeHierarchy.AABBox;
import boundingVolumeHierarchy.BoundingBox;
import boundingVolumeHierarchy.CompositeAABBox;
import material.Material;
import math.Matrix;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class Box extends Shape {

	private Transformation transformation;
	public Point p0;
	public Point p1;
	public static final double kEpsilon = 1e-5;
	public RGBColor color;
	public Material material;
	public static final double boundingBoxDelta = 1e-4;

	/**
	 * Creates a new {@link Box} with the given two points and which is
	 * transformed by the given {@link Transformation}.
	 * 
	 * @param transformation
	 *            the transformation applied to this {@link Box}.
	 * @throws NullPointerException
	 *             when the transformation or points are null.
	 */
	public Box(Transformation transformation, Point p0, Point p1) {
		if (transformation == null)
			throw new NullPointerException("the given transformation is null!");
		this.transformation = transformation;
		if(p0 == null){
			throw new NullPointerException("the given point p0 is null");
		}
		this.p0 = p0;
		if(p1 == null){
			throw new NullPointerException("the given point p1 is null");
		}
		if(p0.x >= p1.x){
			throw new IllegalArgumentException("p1X should be bigger than p0X");
		}
		if(p0.y >= p1.y){
			throw new IllegalArgumentException("p1Y should be bigger than p0Y");
		}
		if(p0.z >= p1.z){
			throw new IllegalArgumentException("p1Z should be bigger than p0Z");
		}
		this.p1 = p1;
	}


	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		//inverse transform the ray
		Ray transformed = transformation.transformInverse(ray);
		Point rayOrigin = transformed.origin;
		Vector rayDirection = transformed.direction;
		
//		Point rayOrigin = ray.origin;
//		Vector rayDirection = ray.direction;
		
		
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
		int faceIn, faceOut;
		
		// find largest entering t value
		
		if (tx_min > ty_min){
			t0 = tx_min;
			if(a >= 0.0){
				faceIn = 0;
			}else{
				faceIn = 3;
			}
		}else{
			t0 = ty_min;
			if(b >= 0.0){
				faceIn = 1;
			}else{
				faceIn = 4;
			}
			
		}	
		if (tz_min > t0){
			t0 = tz_min;
			if (c >= 0.0) {
				faceIn = 2;
			}else {
				faceIn = 5;
			}
		}
		// find smallest exiting t value
			
		if (tx_max < ty_max){
			t1 = tx_max;
			if(a >= 0.0){
				faceOut = 3;
			}else{
				faceOut = 0;
			}
		}else{
			t1 = ty_max;
			if(b >= 0.0){
				faceOut = 4;
			}else{
				faceOut = 1;
			}
		}
		if (tz_max < t1){
			t1 = tz_max;
			if(c >= 0.0){
				faceOut = 5;
			}else{
				faceOut = 2;
			}
		}
		
		if(t0 < t1 && t1 > kEpsilon){ //condition for a hit
			Vector localNormal;
			if(t0 > kEpsilon){
				sr.t = t0;
				localNormal = getNormal(faceIn);
			}else{
				sr.t = t1;
				localNormal = getNormal(faceOut);
			}
			
			sr.material= this.getMaterial();
			sr.hasHitAnObject = true;
			sr.ray = ray;
			sr.hitPoint = ray.origin.add(ray.direction.scale(sr.t));
			
			
			
			Point localHitPoint = transformed.origin.add(transformed.direction.scale(t0));
			Matrix transposeOfInverse = this.transformation.getInverseTransformationMatrix().transpose();
			Vector transformedNormal = transposeOfInverse.transform(localNormal);
			sr.normal = transformedNormal;
			sr.localHitPoint = localHitPoint;
			return true;		
		}
		return false;
	}

	private Vector getNormal(int faceHit) {
		switch (faceHit) {
		case 0:
			return new Vector(-1, 0, 0);
		case 1:
			return new Vector(0, -1, 0);
		case 2:
			return new Vector(0, 0, -1);
		case 3:
			return new Vector(1, 0, 0);
		case 4:
			return new Vector(0, 1, 0);
		case 5:
			return new Vector(0, 0, 1);
		default:
			return null;
		}
	}


	@Override
	public Material getMaterial() {
		return this.material;
	}


	public static Box aaBoundingBoxtoBox(AABBox boundingBox){
		return new Box(Transformation.createIdentity(), boundingBox.getP0(), boundingBox.getP1());
	}

	@Override
	public void setTransformation(Transformation transformation) {
		this.transformation = transformation;
		
	}



	@Override
	public boolean shadowHit(Ray ray, ShadeRec sr) {
		//inverse transform the ray
				Ray transformed = transformation.transformInverse(ray);
				Point rayOrigin = transformed.origin;
				Vector rayDirection = transformed.direction;
				
//				Point rayOrigin = ray.origin;
//				Vector rayDirection = ray.direction;
				
				
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
				
				if(t0 < t1 && t1 > kEpsilon){ //condition for a hit
//					Point localHitPoint = transformed.origin.add(transformed.direction.scale(t0));
//					sr.localHitPoint = localHitPoint;
					sr.t = t0;
					return true;		
				}
				return false;
	}


	public static Box boundingBoxtoBox(BoundingBox boundingBox){
		return new Box(boundingBox.getTransformation(), boundingBox.p0, boundingBox.p1);
	}


	@Override
	public BoundingBox getBoundingBox() {
		// TODO Auto-generated method stub
		return new BoundingBox(new Point(p0.x - boundingBoxDelta, p0.y - boundingBoxDelta, p0.z - boundingBoxDelta),
							new Point(p1.x - boundingBoxDelta, p1.y - boundingBoxDelta, p1.z - boundingBoxDelta), transformation);
	}

	@Override
	public boolean isInfinite() {
		return false;
	}
}
