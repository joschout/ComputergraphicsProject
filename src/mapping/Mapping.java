package mapping;

import math.Point;
import texture.TexelCoordinates;

public abstract class Mapping {

	public abstract TexelCoordinates getTexelCoordinates(Point localHitpoint, int hres, int vres);
}
