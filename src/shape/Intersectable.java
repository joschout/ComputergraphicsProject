package shape;

import math.Ray;
import util.ShadeRec;

public interface Intersectable {

	/**
	 * Returns whether the given {@link Ray} intersects this {@link Intersectable}.
	 * False when the given ray is null.
	 * 
	 * @param ray
	 *            the ray to intersect with.
	 * @return true when the given {@link Ray} intersects this {@link Intersectable}.
	 */
	public boolean intersect(Ray ray, ShadeRec sr);
	
	public boolean shadowHit(Ray shadowRay, ShadeRec sr);
	
	public boolean isInfinite();
}
