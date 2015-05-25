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
import sampling.JitteredSampleFactory;
import sampling.RandomSampleFactory;
import sampling.Sample;
import sampling.SquareSampleFactory;
import util.RGBColor;
import util.ShadeRec;

/**
 *
 */
public class Square extends Shape {

	private Transformation transformation;
	public static final double kEpsilon = 1e-5;
	public Material material;
	public Point p0; 
	public Vector v1; //vector a
	public Vector v2; //vector b
	public Vector normal;
	public static double boundingBoxDelta = 0.0001;
	public SquareSampleFactory sampleFactory = new JitteredSampleFactory(0.5, 0.5, 1, 1, 1);//new JitteredSampleFactory(0, 0, 2, 1, 1);

	
	
	public Square(Transformation transformation,Point midPoint){
		this(transformation, new Point(midPoint.x - 0.5, midPoint.y ,midPoint.z-0.5), new Vector(1,0,0),new Vector(0,0,1));
		
	}
	/**
	 * Creates a new parallelogram with points (0,0,0), (0,0,1) and (1,0,0).
	 */
	public Square(){
		this(Transformation.createIdentity(), new Point(0,0,0), new Vector(1,0,0),new Vector(0,0,1));
		
	}
	
	
	/**
	 * Creates a new {@link Square} with the given three points and which is
	 * transformed by the given {@link Transformation}.
	 * 
	 * @param transformation
	 *            the transformation applied to this {@link Square}.
	 * @param point
	 *            a point of this {@link Square}.
	 * @param normal
	 *            the normal of this {@link Square}.            
	 * @throws NullPointerException
	 *             when the transformation, point or normal is null.
	 */
	public Square(Transformation transformation, Point p0, Vector v1, Vector v2) {
		if (transformation == null)
			throw new NullPointerException("the given origin is null!");
		setTransformation(transformation);
		
		if(p0 == null)
			throw new NullPointerException("the given point a is null");
		this.p0 = new Point(p0);
		
		if(v1 == null)
			throw new NullPointerException("the given point b is null");
		this.v1 = new Vector(v1);
		
		if(v2 == null)
			throw new NullPointerException("the given point c is null");
		this.v2 = new Vector(v2);	
		
		normal = v1.cross(v2);
		normal = normal.normalize();
	}
	
	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		Ray transformed = transformation.transformInverse(ray);
		
		// hb pagina 368
		double t = (p0.subtract(transformed.origin)).dot(normal) / (transformed.direction.dot(normal));
		
		if(t < kEpsilon){
			return false;
		}
		
		// als het een hitpunt op het vlak van de square heeft
		if(t > kEpsilon && t < sr.t){
			
			
			//zie handboek pagina 370
			Vector pMinP0 = transformed.origin.add(transformed.direction.scale(t)).subtract(this.p0);
			
			double dotV1 = pMinP0.dot(this.v1);
			if (dotV1 < 0 || dotV1 > this.v1.lengthSquared()) {
				return false;
			}
			
			double dotV2 = pMinP0.dot(this.v2);
			if (dotV2 < 0 || dotV2 > this.v2.lengthSquared()) {
				return false;
			}
			
			
			
			
			sr.t=t;
	
			Matrix transposeOfInverse = this.transformation.getInverseTransformationMatrix().transpose();
			Vector transformedNormal = transposeOfInverse.transform(normal);
			if(ray.direction.scale(-1).dot(transformedNormal) < 0.0){
				transformedNormal = transformedNormal.scale(-1);
			}
			sr.normal = transformedNormal;

			sr.localHitPoint = transformed.origin.add(transformed.direction.scale(t));
			sr.material= this.getMaterial();
			sr.hasHitAnObject = true;
			sr.ray = ray;
			sr.hitPoint = ray.origin.add(ray.direction.scale(sr.t));
			return true;
		}
		return false;
	}

	@Override
	public Material getMaterial() {
		return this.material;
	}

	@Override
	public BoundingBox getBoundingBox() {
		
		Point p1 = p0.add(v1);
		Point p2 = p0.add(v2);
		Point p3 = p1.add(v2);
		
		//p0 should be smaller than p1;
		List<Point> points = new ArrayList<Point>();
		points.add(p0);
		points.add(p1);
		points.add(p2);
		points.add(p3);
		
		
		
		
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
		
		Point point0 = new Point(p0X - boundingBoxDelta, p0Y - boundingBoxDelta, p0Z - boundingBoxDelta);
		Point point1 = new Point(p1X + boundingBoxDelta, p1Y + boundingBoxDelta, p1Z + boundingBoxDelta);
		
//		System.out.println(point0.toString());
//		System.out.println(point1.toString());
		
		return new BoundingBox(point0, point1, transformation);	
	}


	@Override
	public void setTransformation(Transformation transformation) {
		this.transformation = transformation;
	}


	@Override
	public boolean shadowHit(Ray ray, ShadeRec sr) {
		Ray transformed = transformation.transformInverse(ray);
		
		// hb pagina 368
		double t = (p0.subtract(transformed.origin)).dot(normal) / (transformed.direction.dot(normal));
		
		if(t < kEpsilon){
			return false;
		}
		
		// als het een hitpunt op het vlak van de square heeft
		if(t > kEpsilon && t < sr.t){
			
			
			//zie handboek pagina 370
			Vector pMinP0 = transformed.origin.add(transformed.direction.scale(t)).subtract(this.p0);
			
			double dotV1 = pMinP0.dot(this.v1);
			if (dotV1 < 0 || dotV1 > this.v1.lengthSquared()) {
				return false;
			}
			
			double dotV2 = pMinP0.dot(this.v2);
			if (dotV2 < 0 || dotV2 > this.v2.lengthSquared()) {
				return false;
			}
			sr.t=t;
			return true;
		}
		return false;
	}

	@Override
	public boolean isInfinite() {
		return false;
	}
	

	@Override
	public Point sample(){
		Sample sample = this.sampleFactory.getNextSample();
		Point untransformedPoint = this.p0.add(v1.scale(sample.x)).add(v2.scale(sample.y));
		Point transformedPoint = this.transformation.transform(untransformedPoint);
		return transformedPoint;
	}
	
	public Vector getNormal(Point point){
		
		Vector transformedNormal = this.transformation.getInverseTransformationMatrix().transpose().transform(this.normal);
		return transformedNormal;
	}
	
	/**
	 * returns the inverse area of this square
	 * @param si
	 * @return
	 */
	public double pdf(ShadeRec sr){
		/*
		 * the area of a parallelogram defined by two vectors
		 * 	= the cross product between those vectors
		 */
		Vector v1Transf = this.transformation.transform(v1);
		Vector v2Transf = this.transformation.transform(v2);
		
		double area = v1Transf.cross(v2Transf).length();
		return 1.0/area;
	}
	
//	public Point sample(){
//		Sample sample = this.sampleFactory.getNextSample();
//		return this.p0.add(v1.scale((sample.x +1)*0.5)).add(v2.scale((sample.y +1)*0.5));
//	}
//	
//	public Vector getNormal(Point p){
//		return this.normal;
//	}
//	
//	public double pdf(ShadeRec sr){
//		double v1cos = this.v1.dot(this.v2)/this.v2.length();
//		Vector projection = this.v2.scale(v1cos);
//		double height = this.v1.subtract(projection).length();
//		double surface = this.v2.length() * height;
//		return 1/surface;
//	}
	
	
	
}
