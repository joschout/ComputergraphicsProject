package shape;

import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;

public class Disk implements Shape {
	
	public Transformation transformation;
	public Point center;
	public final double radius;
	public Vector normal;
	public static final double kEpsilon = 0;
	

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
		this.transformation = transformation;
		this.radius = radius;
		
		if (center == null)
			throw new NullPointerException("the given center is null!");
		this.center = center;
		if (normal == null)
			throw new NullPointerException("the given normal is null!");
		this.normal = normal.normalize();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shape.Shape#intersect(geometry3d.Ray3D)
	 */
	@Override
	public boolean intersect(Ray ray) {
		Ray transformed = transformation.transformInverse(ray);
		
		// hb pagina 368
		double t = (center.subtract(transformed.origin)).dot(normal) / (transformed.direction.dot(normal));
		
		if(t < kEpsilon){
			return false;
		}
		
		Point p = transformed.origin.add(transformed.direction.scale(t));
		if(center.subtract(p).lengthSquared() < Math.sqrt(radius)){
			return true;
		}
		
		return false;
	}

}
