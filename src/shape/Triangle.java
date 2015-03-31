package shape;

import boundingVolumeHierarchy.AABBox;
import boundingVolumeHierarchy.BoundingBox;
import boundingVolumeHierarchy.CompositeAABBox;
import util.RGBColor;
import util.ShadeRec;
import material.Material;
import math.Matrix;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;

public class Triangle implements Shape {
	private Transformation transformation;
	public Point v0; //point a
	public Point v1; // point b
	public Point v2; // point c
	public Vector normal;
	public static final double kEpsilon = 1e-5;
	public static final double boundingBoxDelta = 1e-4;
	public Material material;
	public RGBColor color;
	
	/**
	 * Creates a new triangle with points (0,0,0), (0,0,1) and (1,0,0)
	 */
	public Triangle(){
		this(Transformation.createIdentity(), new Point(0,0,0), new Point(0,0,1),new Point(1,0,0));
		
	}
	
	
	/**
	 * Creates a new {@link Triangle} with the given three points and which is
	 * transformed by the given {@link Transformation}.
	 * 
	 * @param transformation
	 *            the transformation applied to this {@link Triangle}.
	 * @param point
	 *            a point of this {@link Triangle}.
	 * @param normal
	 *            the normal of this {@link Triangle}.            
	 * @throws NullPointerException
	 *             when the transformation, point or normal is null.
	 */
	public Triangle(Transformation transformation, Point a, Point b, Point c) {
		if (transformation == null)
			throw new NullPointerException("the given origin is null!");
		setTransformation(transformation);
		
		if(a == null)
			throw new NullPointerException("the given point a is null");
		this.v0 = new Point(a);
		
		if(b == null)
			throw new NullPointerException("the given point b is null");
		this.v1 = new Point(b);
		
		if(c == null)
			throw new NullPointerException("the given point c is null");
		this.v2 = new Point(c);	
		
		normal = (v1.subtract(v0)).cross(v2.subtract(v0));
		normal = normal.normalize();
	}
	
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see shape.Shape#intersect(geometry3d.Ray3D)
//	 */
//	@Override
//	public boolean intersect(Ray ray) {
//		Ray transformed = transformation.transformInverse(ray);
//		
//		Vector v0Minv1 = v0.subtract(v1);
//		Vector v0Minv2 = v0.subtract(v2);
//		Vector v0MinRayOrigin = v0.subtract(transformed.origin);
//		
//		double a = v0Minv1.get(0);
//		double b = v0Minv2.get(0);
//		double c = transformed.direction.get(0);
//		double d = v0MinRayOrigin.get(0);
//		
//		double e = v0Minv1.get(1);
//		double f = v0Minv2.get(1);
//		double g = transformed.direction.get(1);
//		double h = v0MinRayOrigin.get(1);
//		
//		double i = v0Minv1.get(2);
//		double j = v0Minv2.get(2);
//		double k = transformed.direction.get(2);
//		double l = v0MinRayOrigin.get(2);
//		
//		double m = f*k - g*j;
//		double n = h*k - g*l;
//		double p = f*l - h*j;
//		double q = g*i - e*k;
//		double s = e*j - f*i;
//		
//		double inverseDenominator = 1.0 / (a*m + b*q + c*s);
//		
//		double numeratorBeta =d*m - b*n -c*p;
//		double beta = numeratorBeta *  inverseDenominator;
//		
//		if(beta < 0.0){
//			return false;
//		}
//		
//		double r = e*l - h*i;
//		double numeratorGamma = a*n + d*q + c*r;
//		double gamma = numeratorGamma * inverseDenominator;
//		
//		if(gamma < 0.0){
//			return false;
//		}
//		
//		if(beta + gamma > 1){
//			return false;
//		}
//		
//		double numeratorT = a*p - b*r +d*s;
//		double t = numeratorT * inverseDenominator;
//		if(t < kEpsilon){
//			return false;
//		}
//		return true;
//	}




	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		Ray transformed = transformation.transformInverse(ray);
		
		Vector v0Minv1 = v0.subtract(v1);
		Vector v0Minv2 = v0.subtract(v2);
		Vector v0MinRayOrigin = v0.subtract(transformed.origin);
		
		double a = v0Minv1.get(0);
		double b = v0Minv2.get(0);
		double c = transformed.direction.get(0);
		double d = v0MinRayOrigin.get(0);
		
		double e = v0Minv1.get(1);
		double f = v0Minv2.get(1);
		double g = transformed.direction.get(1);
		double h = v0MinRayOrigin.get(1);
		
		double i = v0Minv1.get(2);
		double j = v0Minv2.get(2);
		double k = transformed.direction.get(2);
		double l = v0MinRayOrigin.get(2);
		
		double m = f*k - g*j;
		double n = h*k - g*l;
		double p = f*l - h*j;
		double q = g*i - e*k;
		double s = e*j - f*i;
		
		double inverseDenominator = 1.0 / (a*m + b*q + c*s);
		
		double numeratorBeta = d*m - b*n -c*p;
		double beta = numeratorBeta *  inverseDenominator;
		
		if(beta < 0.0){
			return false;
		}
		
		double r = e*l - h*i;
		double numeratorGamma = a*n + d*q + c*r;
		double gamma = numeratorGamma * inverseDenominator;
		
		if(gamma < 0.0){
			return false;
		}
		
		if(beta + gamma > 1){
			return false;
		}
		
		double numeratorT = a*p - b*r +d*s;
		double t = numeratorT * inverseDenominator;
		if(t < kEpsilon){
			return false;
		}
		
		sr.t = t;
		
		
		sr.material= this.getMaterial();
		sr.hasHitAnObject = true;
		sr.ray = ray;
		sr.hitPoint = ray.origin.add(ray.direction.scale(sr.t));
		
		
		Matrix transposeOfInverse = this.transformation.getInverseTransformationMatrix().transpose();
		Vector transformedNormal = transposeOfInverse.transform(normal);
		sr.normal = transformedNormal;
		sr.localHitPoint = transformed.origin.add(transformed.direction.scale(t)) ;
		return true;
	}




	@Override
	public Material getMaterial() {
		return this.material;
	}

	@Override
	public void setTransformation(Transformation transformation) {
		this.transformation = transformation;	
	}


	@Override
	public boolean shadowHit(Ray ray, ShadeRec sr) {

		Ray transformed = transformation.transformInverse(ray);
		
		Vector v0Minv1 = v0.subtract(v1);
		Vector v0Minv2 = v0.subtract(v2);
		Vector v0MinRayOrigin = v0.subtract(transformed.origin);
		
		double a = v0Minv1.get(0);
		double b = v0Minv2.get(0);
		double c = transformed.direction.get(0);
		double d = v0MinRayOrigin.get(0);
		
		double e = v0Minv1.get(1);
		double f = v0Minv2.get(1);
		double g = transformed.direction.get(1);
		double h = v0MinRayOrigin.get(1);
		
		double i = v0Minv1.get(2);
		double j = v0Minv2.get(2);
		double k = transformed.direction.get(2);
		double l = v0MinRayOrigin.get(2);
		
		double m = f*k - g*j;
		double n = h*k - g*l;
		double p = f*l - h*j;
		double q = g*i - e*k;
		double s = e*j - f*i;
		
		double inverseDenominator = 1.0 / (a*m + b*q + c*s);
		
		double numeratorBeta =d*m - b*n -c*p;
		double beta = numeratorBeta *  inverseDenominator;
		
		if(beta < 0.0){
			return false;
		}
		
		double r = e*l - h*i;
		double numeratorGamma = a*n + d*q + c*r;
		double gamma = numeratorGamma * inverseDenominator;
		
		if(gamma < 0.0){
			return false;
		}
		
		if(beta + gamma > 1){
			return false;
		}
		
		double numeratorT = a*p - b*r +d*s;
		double t = numeratorT * inverseDenominator;
		if(t < kEpsilon){
			return false;
		}
		
		sr.t = t;

//		sr.localHitPoint = transformed.origin.add(transformed.direction.scale(t)) ;
		return true;
	}


	@Override
	public BoundingBox getBoundingBox() {
			
		double p0X = Math.min(Math.min(v0.x, v1.x), v2.x) - boundingBoxDelta;
		double p0Y = Math.min(Math.min(v0.y, v1.y), v2.y) - boundingBoxDelta;
		double p0Z = Math.min(Math.min(v0.z, v1.z), v2.z) - boundingBoxDelta;
		
		double p1X = Math.max(Math.max(v0.x, v1.x), v2.x) + boundingBoxDelta;
		double p1Y = Math.max(Math.max(v0.y, v1.y), v2.y) + boundingBoxDelta;
		double p1Z = Math.max(Math.max(v0.z, v1.z), v2.z) + boundingBoxDelta;
		
		return new BoundingBox(p0X, p0Y, p0Z, p1X, p1Y, p1Z, transformation);
	}



	@Override
	public AABBox getAABoundingBox() {
		return AABBox.boundingBoxToAABoundingBox(getBoundingBox(), this);
	}

	@Override
	public CompositeAABBox getBoundingVolumeHierarchy() {
		return getAABoundingBox();
	}

}
