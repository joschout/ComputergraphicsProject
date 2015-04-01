package shape;

import java.util.ArrayList;
import java.util.List;

import boundingVolumeHierarchy.AABBox;
import boundingVolumeHierarchy.BoundingBox;
import boundingVolumeHierarchy.CompositeAABBox;
import material.Material;
import math.Matrix;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;
import sampling.Sample;
import util.RGBColor;
import util.ShadeRec;

/**
 *
 */
public class Parallelogram extends Shape {

	private Transformation transformation;
	public static final double kEpsilon = 1e-5;
	public RGBColor color;
	public Material material;
	public Point v0; //point a
	public Point v1; // point b
	public Point v2; // point c
	public Vector normal;
	public static double boundingBoxDelta = 0.0001;

	/**
	 * Creates a new parallelogram with points (0,0,0), (0,0,1) and (1,0,0).
	 */
	public Parallelogram(){
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
	public Parallelogram(Transformation transformation, Point a, Point b, Point c) {
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
		
		double numeratorBeta =d*m - b*n -c*p;
		double beta = numeratorBeta *  inverseDenominator;
		
		if(beta < 0.0 || beta > 1.0){
			return false;
		}
		
		double r = e*l - h*i;
		double numeratorGamma = a*n + d*q + c*r;
		double gamma = numeratorGamma * inverseDenominator;
		
		if(gamma < 0.0 || gamma > 1.0){
			return false;
		}
		
		if(beta + gamma < 0 || beta + gamma > 2){
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
	public BoundingBox getBoundingBox() {
		Vector v1Minv0 = v1.subtract(v0);
		Point v3 = v2.add(v1Minv0);
		
		//p0 should be smaller than p1;
		List<Point> points = new ArrayList<Point>();
		points.add(v0);
		points.add(v1);
		points.add(v2);
		points.add(v3);
		
		
		
		
		double p0X = Double.MAX_VALUE;
		double p1X = -Double.MAX_VALUE;
		for(Point point: points){
			double pX = point.x;
			if(pX < p0X){
				p0X = pX;
			}
			if(pX > p1X){
				p1X = pX;
			}
		}
		
		double p0Y = Double.MAX_VALUE;
		double p1Y = -Double.MAX_VALUE;
		for(Point point: points){
			double pY = point.y;
			if(pY < p0Y){
				p0Y = pY;
			}
			if(pY > p1Y){
				p1Y = pY;
			}
		}
		
		double p0Z = Double.MAX_VALUE;
		double p1Z = -Double.MAX_VALUE;
		for(Point point: points){
			double pZ = point.z;
			if(pZ < p0Z){
				p0Z = pZ;
			}
			if(pZ > p1Z){
				p1Z = pZ;
			}
		}
		
		Point p0 = new Point(p0X - boundingBoxDelta, p0Y - boundingBoxDelta, p0Z - boundingBoxDelta);
		Point p1 = new Point(p1X + boundingBoxDelta, p1Y + boundingBoxDelta, p1Z + boundingBoxDelta);
		
		System.out.println(p0.toString());
		System.out.println(p1.toString());
		
		return new BoundingBox(p0, p1, transformation);
		
		
		
		
		
		
		
		
		
		
		
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
		
		if(beta < 0.0 || beta > 1.0){
			return false;
		}
		
		double r = e*l - h*i;
		double numeratorGamma = a*n + d*q + c*r;
		double gamma = numeratorGamma * inverseDenominator;
		
		if(gamma < 0.0 || gamma > 1.0){
			return false;
		}
		
		if(beta + gamma < 0 || beta + gamma > 2){
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
	public boolean isInfinite() {
		return false;
	}
	
}
