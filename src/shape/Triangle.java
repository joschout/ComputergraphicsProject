package shape;

import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;

public class Triangle implements Shape {
	public Transformation transformation;
	public Point v0; //point a
	public Point v1; // point b
	public Point v2; // point c
	public static final double kEpsilon = 0;
	
	/**
	 * Creates a new {@link Plane} with the given point and normal and which is
	 * transformed by the given {@link Transformation}.
	 * 
	 * @param transformation
	 *            the transformation applied to this {@link Plane}.
	 * @param point
	 *            a point of this {@link Plane}.
	 * @param normal
	 *            the normal of this {@link Plane}.            
	 * @throws NullPointerException
	 *             when the transformation, point or normal is null.
	 */
	public Triangle(Transformation transformation, Point a, Point b, Point c) {
		if (transformation == null)
			throw new NullPointerException("the given origin is null!");
		this.transformation = transformation;
		
		if(a == null)
			throw new NullPointerException("the given point a is null");
		this.v0 = a;
		
		if(b == null)
			throw new NullPointerException("the given point b is null");
		this.v1 = b;
		
		if(c == null)
			throw new NullPointerException("the given point c is null");
		this.v2 = c;	
	}
	
	@Override
	public boolean intersect(Ray ray) {
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
		return true;
	}

}
