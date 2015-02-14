package shape;

import math.Ray;
import math.Point;
import math.Transformation;
import math.Vector;

public class Plane implements Shape {
	public Transformation transformation;
	public Point point;
	public Vector normal;
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
	public Plane(Transformation transformation, Point point, Vector normal) {
		if (transformation == null)
			throw new NullPointerException("the given origin is null!");
		this.transformation = transformation;
		if(point == null)
			throw new NullPointerException("the given point is null");
		this.point = point;
		if(normal == null)
			throw new NullPointerException("the vector for given as normal is null");
		this.normal = normal;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see shape.Shape#intersect(geometry3d.Ray3D)
	 */
	@Override
	public boolean intersect(Ray ray) {
		Ray transformed = transformation.transformInverse(ray);
		
		//zie handboek pagina 54-56
		Vector aMinO = point.subtract(transformed.origin);
		double numerator = aMinO.dot(normal);
		double denominator = transformed.direction.dot(normal);
		
		double t = numerator / denominator;
		
		return t > kEpsilon;
	}


}