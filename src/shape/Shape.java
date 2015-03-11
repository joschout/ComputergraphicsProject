package shape;

import boundingVolumeHierarchy.AABBox;
import boundingVolumeHierarchy.BoundingBox;
import boundingVolumeHierarchy.CompositeAABBox;
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
public interface Shape extends Intersectable {
	
	/**
	 * Returns whether the given {@link Ray} intersects this {@link Shape}.
	 * False when the given ray is null.
	 * 
	 * @param ray
	 *            the ray to intersect with.
	 * @return true when the given {@link Ray} intersects this {@link Shape}.
	 */
	//public boolean intersect(Ray ray);
	
	//public boolean intersect(Ray ray, ShadeRec sr);
	
	public Material getMaterial();

	public BoundingBox getBoundingBox();
	
	public AABBox getAABoundingBox();
	
	public void setTransformation(Transformation transformation);
	
	public boolean shadowHit(Ray ray, ShadeRec sr);
	
	public CompositeAABBox getBoundingVolumeHierarchy();

}
