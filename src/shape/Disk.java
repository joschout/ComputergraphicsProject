package shape;

import util.RGBColor;
import util.ShadeRec;
import material.Material;
import math.Matrix;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;

public class Disk implements Shape {
	
	private Transformation transformation;
	public Point center;
	public final double radius;
	public Vector normal;
	public static final double kEpsilon = 1e-5;
	public RGBColor color;
	public Material material;
	
	/**
	 * creates a disk in centered in the origin with radius 1 and normal (0, 0, -1).
	 */
	public Disk(){
		this(Transformation.createIdentity(), new Point(), 1, new Vector(0, 0, -1));
	}
	
	/**
	 * Creates a new {@link Disk} with the given radius and which is
	 * transformed by the given {@link Transformation}.
	 * 
	 * @param transformation
	 *            the transformation applied to this {@link Disk}.
	 * @param center
	 * 			  the center of this {@link Disk}.
	 * @param radius
	 *            the radius of this {@link Disk}.
	 * @param normal
	 * 			 the normal of this {@link Disk}.
	 * @throws NullPointerException
	 *             when the transformation is null.
	 * @throws IllegalArgumentException
	 *             when the radius is smaller than zero.
	 */
	public Disk(Transformation transformation, Point center, double radius, Vector normal) {
		if (transformation == null)
			throw new NullPointerException("the given transformation is null!");
		if (radius < 0)
			throw new IllegalArgumentException(
					"the given radius cannot be smaller than zero!");
		setTransformation(transformation);
		this.radius = radius;
		
		if (center == null)
			throw new NullPointerException("the given center is null!");
		this.center = center;
		if (normal == null)
			throw new NullPointerException("the given normal is null!");
		this.normal = normal.normalize();
		
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
//		// hb pagina 368
//		double t = (center.subtract(transformed.origin)).dot(normal) / (transformed.direction.dot(normal));
//		
//		if(t < kEpsilon){
//			return false;
//		}
//		
//		Point p = transformed.origin.add(transformed.direction.scale(t));
//		if(center.subtract(p).lengthSquared() < Math.sqrt(radius)){
//			return true;
//		}
//		
//		return false;
//	}

	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		Ray transformed = transformation.transformInverse(ray);
		
		// hb pagina 368
		double t = (center.subtract(transformed.origin)).dot(normal) / (transformed.direction.dot(normal));
		
		if(t < kEpsilon){
			return false;
		}
		
		Point p = transformed.origin.add(transformed.direction.scale(t));
		if(center.subtract(p).lengthSquared() < Math.sqrt(radius)){
			sr.t = t;
			Matrix transposeOfInverse = this.transformation.getInverseTransformationMatrix().transpose();
			Vector transformedNormal = transposeOfInverse.transform(normal);
			if(ray.direction.scale(-1).dot(transformedNormal) < 0.0){
				transformedNormal = transformedNormal.scale(-1);
			}
			sr.normal = transformedNormal;
			sr.localHitPoint = p;
			return true;
		}
		return false;
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
		
		// hb pagina 368
		double t = (center.subtract(transformed.origin)).dot(normal) / (transformed.direction.dot(normal));
		
		if(t < kEpsilon){
			return false;
		}
		
		Point p = transformed.origin.add(transformed.direction.scale(t));
		if(center.subtract(p).lengthSquared() < Math.sqrt(radius)){
			sr.t = t;
//			sr.localHitPoint = p;
			return true;
		}
		return false;
	}

}
