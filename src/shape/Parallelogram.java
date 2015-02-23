package shape;

import material.Material;
import math.Matrix;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

/**
 *
 */
public class Parallelogram implements Shape {

	private Transformation transformation;
	public static final double kEpsilon = 1e-5;
	public RGBColor color;
	public Material material;
	public Point v0; //point a
	public Point v1; // point b
	public Point v2; // point c
//	public Point point;
//	public Vector a;
//	public Vector b;
	public Vector normal;


//	public Parallelogram(){
//		this(Transformation.createIdentity(), new Vector(1,0,0), new Vector(0,1,0), new Point());
//	}
//
//	public Parallelogram(Transformation transformation, Vector a, Vector b, Point point) {
//		if (transformation == null)
//			throw new NullPointerException("the given transformation is null!");
//		if (a == null)
//			throw new IllegalArgumentException(
//					"the given vector a is null");
//		if (b == null)
//			throw new IllegalArgumentException(
//					"the given vector a is null");
//		if (point == null)
//			throw new NullPointerException("the given point is null!");
//		this.transformation = transformation;
//		this.a = a;
//		this.b = b;
//		this.point = point;
//		this.normal = a.cross(b).normalize();
//	}

	/**
	 * Creates a new triangle with points (0,0,0), (0,0,1) and (1,0,0)
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
	
//	@Override
//	public boolean intersect(Ray ray) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
//		Ray transformed = transformation.transformInverse(ray);
//
//		//zie handboek pagina 54-56
//		Vector pointMinOrigin = point.subtract(transformed.origin);
//		double numerator = pointMinOrigin.dot(normal);
//		double denominator = transformed.direction.dot(normal);
//
//		double t = numerator / denominator;
//
//		if(t <=kEpsilon){
//			return false;
//		}
//
//		Point p = transformed.origin.add(transformed.direction.scale(t));
//		Vector pMinPoint = p.subtract(point);
//		Vector d = a.add(b);
//		
//		double dToA = d.dot(a);
//		double pMinPointToA = pMinPoint.dot(a);
//		if(pMinPointToA < 0.0 || pMinPointToA > dToA){
//			return false;
//		}
//		
//		double dToB = d.dot(b);
//		double pMinPointToB = pMinPoint.dot(b);
//		if(pMinPointToB < 0.0 || pMinPointToB > dToB){
//			return false;
//		}
//		
//		
//		
//		if(pMinPointToA > a.lengthSquared() && )
//		
//		
//				
//				
//		Matrix transposeOfInverse = this.transformation.getInverseTransformationMatrix().transpose();
//		Vector transformedNormal = transposeOfInverse.transform(normal);
//		sr.normal = transformedNormal;
//		sr.normal = normal;
//		sr.localHitPoint = transformed.origin.add(transformed.direction.scale(t));
//		sr.t=t;

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
	public RGBColor getColor() {
		return this.color;
	}


	@Override
	public BoundingBox getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
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



}
