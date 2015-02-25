package shape;

import util.RGBColor;
import util.ShadeRec;
import material.Material;
import math.Matrix;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;

public class Cylinder implements Shape {

	private Transformation transformation;
	public final double radius;
	public final double ySmall;
	public final double yLarge;
	public static final double kEpsilon = 1e-5;
	public RGBColor color;
	public Material material;

	/**
	 * Creates a cylinder with radius 1, centered around the y-axis,
	 * with y-values ranging from -1 to 1.
	 */
	public Cylinder(){
		this(Transformation.IDENTITY, 1, 1, -1);
	}

	/**
	 * Creates a new {@link Cylinder} with the given radius centered around the y-axis, 
	 * with y-values between the given interval
	 *  and which is transformed by the given {@link Transformation}.
	 * 
	 * @param transformation
	 *            the transformation applied to this {@link Cylinder}.
	 * @param radius
	 *            the radius of this {@link Cylinder}.
	 * @param y0
	 *            endpoint of the interval of the y-values of this {@link Cylinder}.
	 * @param y1
	 *            endpoint of the interval of the y-values of  {@link Cylinder}.           
	 * @throws NullPointerException
	 *             when the transformation is null.
	 * @throws IllegalArgumentException
	 *             when the radius is smaller than zero.
	 */
	public Cylinder(Transformation transformation, double radius, double y0, double y1) {
		if (transformation == null)
			throw new NullPointerException("the given origin is null!");
		if (radius < 0)
			throw new IllegalArgumentException(
					"the given radius cannot be smaller than zero!");
		setTransformation(transformation);
		this.radius = radius;
		if(y0 == y1)
			throw new IllegalArgumentException("the given endpoints of the y-interval cannot be equal");
		if(y0 < y1){
			this.ySmall = y0;
			this.yLarge = y1;
		}else{
			this.ySmall = y1;
			this.yLarge = y0;
		}
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see shape.Shape#intersect(geometry3d.Ray3D)
//	 */
//
//	public boolean intersect(Ray ray) {
//		//inverse transform the ray
//		Ray transformed = transformation.transformInverse(ray);
//
//		//zie handboek pagina 373
//
//		double a = Math.pow(transformed.direction.get(0),2) + Math.pow(transformed.direction.get(2),2);
//		double b = 2.0 * (transformed.origin.get(0)*transformed.direction.get(0) 
//				+ transformed.origin.get(2)*transformed.direction.get(2));
//		double c = Math.pow(transformed.origin.get(0),2) + Math.pow(transformed.origin.get(2),2) - Math.pow(radius, 2);
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
//		//check of voor deze t's y wel in het juiste interval zit
//
//		Point p0 = transformed.origin.add(transformed.direction.scale(t0));
//		Point p1 = transformed.origin.add(transformed.direction.scale(t1));
//
//		if (p0.get(1) >= ySmall && p0.get(1)<= yLarge &&  t0 >= kEpsilon){
//			return true;
//		}
//
//		if (p1.get(1) >= ySmall && p1.get(1)<= yLarge &&  t1 >= kEpsilon){
//			return true;
//		}
//		return false;
//	}

	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		//inverse transform the ray
		Ray transformed = transformation.transformInverse(ray);

		//zie handboek pagina 373

		double a = Math.pow(transformed.direction.get(0),2) + Math.pow(transformed.direction.get(2),2);
		double b = 2.0 * (transformed.origin.get(0)*transformed.direction.get(0) 
				+ transformed.origin.get(2)*transformed.direction.get(2));
		double c = Math.pow(transformed.origin.get(0),2) + Math.pow(transformed.origin.get(2),2) - Math.pow(radius, 2);

		double d = b * b - 4.0 * a * c;

		if (d < 0){
			return false;
		}
		double dr = Math.sqrt(d);
		double q = b < 0 ? -0.5 * (b - dr) : -0.5 * (b + dr);

		double t0 = q / a;
		double t1 = c / q;

		// zorg dat t0 de kleinste waarde heeft.
		if(t0 > t1){
			double t3 = t0;
			t0 = t1;
			t1 = t3;
		}

		if(t0 > kEpsilon ){
			Point p0 = transformed.origin.add(transformed.direction.scale(t0));

			if ( p0.y >=  ySmall && p0.y <= yLarge){
				//if (p0.get(1) >= ySmall && p0.get(1)<= yLarge &&  t0 >= kEpsilon && t0 < t1){
				sr.t = t0;
				//Point localHitPoint = p0;
				Point pointOnYAxis = new Point(0, p0.y, 0);
				Vector localNormal = p0.subtract(pointOnYAxis).normalize();
				Matrix transposeOfInverse = this.transformation.getInverseTransformationMatrix().transpose();
				Vector transformedNormal = transposeOfInverse.transform(localNormal);
				// als normaal dot Wo kleiner dan 0, draai normaal om
				if(ray.direction.scale(-1).dot(transformedNormal) < 0.0){
					transformedNormal = transformedNormal.scale(-1);
				}
				sr.normal = transformedNormal;
				sr.localHitPoint = p0;
				return true;
			}
		}

		if ( t1 > kEpsilon){
			Point p1 = transformed.origin.add(transformed.direction.scale(t1));

			if( p1.y >=  ySmall && p1.y <= yLarge){
				sr.t = t1;
				//Point localHitPoint = p1;
				Point pointOnYAxis = new Point(0, p1.y, 0);
				Vector localNormal = p1.subtract(pointOnYAxis).normalize();
				Matrix transposeOfInverse = this.transformation.getInverseTransformationMatrix().transpose();
				Vector transformedNormal = transposeOfInverse.transform(localNormal);
				if(ray.direction.scale(-1).dot(transformedNormal) < 0.0){
					transformedNormal = transformedNormal.scale(-1);
				}
				sr.normal = transformedNormal;
				sr.localHitPoint = p1;
				return true;
			}
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
		//inverse transform the ray
				Ray transformed = transformation.transformInverse(ray);

				//zie handboek pagina 373

				double a = Math.pow(transformed.direction.get(0),2) + Math.pow(transformed.direction.get(2),2);
				double b = 2.0 * (transformed.origin.get(0)*transformed.direction.get(0) 
						+ transformed.origin.get(2)*transformed.direction.get(2));
				double c = Math.pow(transformed.origin.get(0),2) + Math.pow(transformed.origin.get(2),2) - Math.pow(radius, 2);

				double d = b * b - 4.0 * a * c;

				if (d < 0){
					return false;
				}
				double dr = Math.sqrt(d);
				double q = b < 0 ? -0.5 * (b - dr) : -0.5 * (b + dr);

				double t0 = q / a;
				double t1 = c / q;

				// zorg dat t0 de kleinste waarde heeft.
				if(t0 > t1){
					double t3 = t0;
					t0 = t1;
					t1 = t3;
				}

				if(t0 > kEpsilon ){
					Point p0 = transformed.origin.add(transformed.direction.scale(t0));

					if ( p0.y >=  ySmall && p0.y <= yLarge){
						//if (p0.get(1) >= ySmall && p0.get(1)<= yLarge &&  t0 >= kEpsilon && t0 < t1){
						sr.t = t0;
//						sr.localHitPoint = p0;
						return true;
					}
				}

				if ( t1 > kEpsilon){
					Point p1 = transformed.origin.add(transformed.direction.scale(t1));

					if( p1.y >=  ySmall && p1.y <= yLarge){
						sr.t = t1;						
//						sr.localHitPoint = p1;
						return true;
					}
				}
				return false;
	}




}
