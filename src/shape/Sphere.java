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

/**
 * Represents a three dimensional sphere.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public class Sphere extends Shape {
	private Transformation transformation;
	public final double radius;
	public static final double kEpsilon = 0.00001;
	public RGBColor color;
	public Material material;
	public static double boundingBoxDelta = 0.0001;

	/**
	 * Creates a new {@link Sphere} with the given radius and which is
	 * transformed by the given {@link Transformation}.
	 * 
	 * @param transformation
	 *            the transformation applied to this {@link Sphere}.
	 * @param radius
	 *            the radius of this {@link Sphere}..
	 * @throws NullPointerException
	 *             when the transformation is null.
	 * @throws IllegalArgumentException
	 *             when the radius is smaller than zero.
	 */
	public Sphere(Transformation transformation, double radius) {
		if (transformation == null)
			throw new NullPointerException("the given transformation is null!");
		if (radius < 0)
			throw new IllegalArgumentException(
					"the given radius cannot be smaller than zero!");
		setTransformation(transformation);
		this.radius = radius;
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see shape.Shape#intersect(geometry3d.Ray3D)
//	 */
//	@Override
//	public boolean intersect(Ray ray) {
//		//inverse transform the ray
//		Ray transformed = transformation.transformInverse(ray);
//
//		//zie handboek pagina 57
//		Vector o = transformed.origin.toVector3D();
//
//		double a = transformed.direction.dot(transformed.direction);
//		double b = 2.0 * (transformed.direction.dot(o));
//		double c = o.dot(o) - radius * radius;
//
//		double d = b * b - 4.0 * a * c;
//
//		if (d < 0)
//			return false;
//		double dr = Math.sqrt(d);
//		double q = b < 0 ? -0.5 * (b - dr) : -0.5 * (b + dr);
//
//		double t0 = q / a;
//		double t1 = c / q;
//
//		return t0 >= kEpsilon || t1 >= kEpsilon;
//	}

	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		//inverse transform the ray
		Ray transformed = transformation.transformInverse(ray);

		//zie handboek pagina 57
		Vector o = transformed.origin.toVector3D();

		double a = transformed.direction.dot(transformed.direction);
		double b = 2.0 * (transformed.direction.dot(o));
		double c = o.dot(o) - radius * radius;

		double d = b * b - 4.0 * a * c;

		if (d < 0)
			return false;
		double dr = Math.sqrt(d);
		double q = b < 0 ? -0.5 * (b - dr) : -0.5 * (b + dr);

		double t0 = q / a;
		double t1 = c / q;

		if( (t0 >= kEpsilon && t1 >= kEpsilon && t1 >= t0) || (t0 >= kEpsilon && t1 < kEpsilon)){
			sr.t = t0;
			Point localHitPoint = transformed.origin.add(transformed.direction.scale(t0));
			Vector localNormal = localHitPoint.toVector3D().normalize();
			Matrix transposeOfInverse = this.transformation.getInverseTransformationMatrix().transpose();
			Vector transformedNormal = transposeOfInverse.transform(localNormal);
			
			sr.material= this.getMaterial();
			sr.hasHitAnObject = true;
			sr.ray = ray;
			sr.hitPoint = ray.origin.add(ray.direction.scale(sr.t));
			
			sr.normal = transformedNormal;
			sr.localHitPoint = localHitPoint;
			return true;
			
		}
		if( (t0 >= kEpsilon && t1 >= kEpsilon && t0 > t1) || (t1 >= kEpsilon && t0 < kEpsilon)){
			sr.t = t1;
			Point localHitPoint = transformed.origin.add(transformed.direction.scale(t1));
			Vector localNormal = localHitPoint.toVector3D().normalize();
			Matrix transposeOfInverse = this.transformation.getInverseTransformationMatrix().transpose();
			Vector transformedNormal = transposeOfInverse.transform(localNormal);
			
			
			sr.material= this.getMaterial();
			sr.hasHitAnObject = true;
			sr.ray = ray;
			sr.hitPoint = ray.origin.add(ray.direction.scale(sr.t));
			sr.normal = transformedNormal;
			sr.localHitPoint = localHitPoint;
			return true;
		}
		
		return false;
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
		//inverse transform the ray
				Ray transformed = transformation.transformInverse(ray);

				//zie handboek pagina 57
				Vector o = transformed.origin.toVector3D();

				double a = transformed.direction.dot(transformed.direction);
				double b = 2.0 * (transformed.direction.dot(o));
				double c = o.dot(o) - radius * radius;

				double d = b * b - 4.0 * a * c;

				if (d < 0)
					return false;
				double dr = Math.sqrt(d);
				double q = b < 0 ? -0.5 * (b - dr) : -0.5 * (b + dr);

				double t0 = q / a;
				double t1 = c / q;

				if( (t0 >= kEpsilon && t1 >= kEpsilon && t1 >= t0) || (t0 >= kEpsilon && t1 < kEpsilon)){
					sr.t = t0;
//					Point localHitPoint = transformed.origin.add(transformed.direction.scale(t0));
//					sr.localHitPoint = localHitPoint;
					return true;
					
				}
				if( (t0 >= kEpsilon && t1 >= kEpsilon && t0 > t1) || (t1 >= kEpsilon && t0 < kEpsilon)){
					sr.t = t1;
//					Point localHitPoint = transformed.origin.add(transformed.direction.scale(t1));
//					sr.localHitPoint = localHitPoint;
					return true;
				}
				
				return false;
	}

	@Override
	public BoundingBox getBoundingBox() {
		//create bounding box around untransformed sphere in the origin:
		Point p0 = new Point(- this.radius - boundingBoxDelta, -this.radius - boundingBoxDelta, -this.radius - boundingBoxDelta);
		Point p1 = new Point(this.radius + boundingBoxDelta, this.radius + boundingBoxDelta, this.radius + boundingBoxDelta);
		return new BoundingBox(p0, p1, this.transformation);
	}
	
	@Override
	public boolean isInfinite() {
		return infinite;
	}
	
	public boolean infinite = false;
}
