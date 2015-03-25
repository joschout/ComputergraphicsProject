package shape;

import math.Ray;
import util.ShadeRec;

public interface Intersectable {

	public boolean intersect(Ray ray, ShadeRec sr);
	
	public boolean shadowHit(Ray shadowRay, ShadeRec sr);
}
