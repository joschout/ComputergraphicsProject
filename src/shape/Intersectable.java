package shape;

import math.Ray;
import util.ShadeRec;

public interface Intersectable {

	public boolean intersect(Ray ray, ShadeRec sr);
}
