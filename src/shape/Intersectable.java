package shape;

import math.Ray;
import util.ShadeRec;

public abstract class Intersectable {
	
	private Boolean isLightSource = false;
	
	public boolean isLightSource(){
		return this.isLightSource;
	}
	
	public void setLightSource(boolean isLightSource){
		this.isLightSource = isLightSource;
	}
	

	/**
	 * Returns whether the given {@link Ray} intersects this {@link Intersectable}.
	 * False when the given ray is null.
	 * 
	 * @param ray
	 *            the ray to intersect with.
	 * @return true when the given {@link Ray} intersects this {@link Intersectable}.
	 */
	public abstract boolean intersect(Ray ray, ShadeRec sr);
	
	public abstract boolean shadowHit(Ray shadowRay, ShadeRec sr);
	
	public abstract boolean isInfinite();
	
	
}
