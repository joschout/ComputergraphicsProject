package shape;

import boundingVolumeHierarchy.AABBox;
import boundingVolumeHierarchy.BoundingBox;
import boundingVolumeHierarchy.CompositeAABBox;
import util.RGBColor;
import util.ShadeRec;
import material.Material;
import math.Matrix;
import math.Ray;
import math.Point;
import math.Transformation;
import math.Vector;

public class Plane implements Shape {
	private Transformation transformation;
	public Point point;
	public Vector normal;
	public static final double kEpsilon = 1e-5;
	public Material material;
	public RGBColor color;
	
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
	public Plane(Transformation transformation, Point point, Vector normal) {
		if (transformation == null)
			throw new NullPointerException("the given origin is null!");
		setTransformation(transformation);
		if(point == null)
			throw new NullPointerException("the given point is null");
		this.point = point;
		if(normal == null)
			throw new NullPointerException("the vector for given as normal is null");
		this.normal = normal;
	}
	
	
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see shape.Shape#intersect(geometry3d.Ray3D)
//	 */
//	@Override
//	public boolean intersect(Ray ray) {
//		
//		Ray transformed = transformation.transformInverse(ray);
//		
//		//zie handboek pagina 54-56
//		Vector aMinO = point.subtract(transformed.origin);
//		double numerator = aMinO.dot(normal);
//		double denominator = transformed.direction.dot(normal);
//		
//		double t = numerator / denominator;
//		
//		return t > kEpsilon;
//	}
	
	public boolean intersect(Ray ray, ShadeRec sr) {
		
		Ray transformed = transformation.transformInverse(ray);
		
		//zie handboek pagina 54-56
		Vector aMinO = point.subtract(transformed.origin);
		double numerator = aMinO.dot(normal);
		double denominator = transformed.direction.dot(normal);
		
		double t = numerator / denominator;
		
		if(t > kEpsilon){
			sr.t=t;
			
			Matrix transposeOfInverse = this.transformation.getInverseTransformationMatrix().transpose();
			Vector transformedNormal = transposeOfInverse.transform(normal);
			sr.normal = transformedNormal;
			sr.normal = normal;
			sr.localHitPoint = transformed.origin.add(transformed.direction.scale(t));
			
			return true;
		}
		return false;
	}
	
	public boolean shadowHit(Ray ray, ShadeRec sr) {

		Ray transformed = transformation.transformInverse(ray);
		
		//zie handboek pagina 54-56
		Vector aMinO = point.subtract(transformed.origin);
		double numerator = aMinO.dot(normal);
		double denominator = transformed.direction.dot(normal);
		
		double t = numerator / denominator;
		
		if(t > kEpsilon){
			sr.t=t;
			//sr.localHitPoint = transformed.origin.add(transformed.direction.scale(t));
			
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
		return new BoundingBox(Point.MIN_MAXVALUES, Point.MAXVALUES, transformation);
	}


	@Override
	public void setTransformation(Transformation transformation) {
		this.transformation = transformation;
		
	}


	@Override
	public AABBox getAABoundingBox() {
		return new AABBox(Point.MIN_MAXVALUES, Point.MAXVALUES, this);
	}


	@Override
	public CompositeAABBox getBoundingVolumeHierarchy() {
		return getAABoundingBox();
	}


	@Override
	public boolean isInfinite() {
		return true;
	}


}
