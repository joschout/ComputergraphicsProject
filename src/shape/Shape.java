package shape;

import boundingVolumeHierarchy.AABBox;
import boundingVolumeHierarchy.BoundingBox;
import boundingVolumeHierarchy.CompositeAABBox;
import sampling.SampleFactory;
import util.ShadeRec;
import material.Material;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;

/**
 * Interface which should be implemented by all {@link Shape}s.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public abstract class Shape implements Intersectable {

	public abstract Material getMaterial();

	public abstract BoundingBox getBoundingBox();
	
	
	public AABBox getAABoundingBox() {
		return AABBox.boundingBoxToAABoundingBox(getBoundingBox(), this);
	}

	public CompositeAABBox getBoundingVolumeHierarchy() {
		return getAABoundingBox();
	}
		
	public abstract void setTransformation(Transformation transformation);
	
	public Point sample(){
		throw new UnsupportedOperationException("shape cannot be an arealight");
	}
	
	public void setSampler(SampleFactory sampleFactory){
		throw new UnsupportedOperationException("shape cannot be an arealight");
	}
	
	public double pdf(ShadeRec sr){
		throw new UnsupportedOperationException("shape cannot be an arealight");
	}
	
	public Vector getNormal(Point point){
		throw new UnsupportedOperationException("shape cannot be an arealight");
	}
	

}
