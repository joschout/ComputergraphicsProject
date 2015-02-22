package shape;

import util.RGBColor;
import util.ShadeRec;
import material.Material;
import math.Ray;
import math.Transformation;

/**
 * Interface which should be implemented by all {@link Shape}s.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public interface Shape {
	
	/**
	 * Returns whether the given {@link Ray} intersects this {@link Shape}.
	 * False when the given ray is null.
	 * 
	 * @param ray
	 *            the ray to intersect with.
	 * @return true when the given {@link Ray} intersects this {@link Shape}.
	 */
	//public boolean intersect(Ray ray);
	
	public boolean intersect(Ray ray, ShadeRec sr);
	
	public Material getMaterial();
	
	public RGBColor getColor();

	public BoundingBox getBoundingBox();
	
	public void setTransformation(Transformation transformation);
}
