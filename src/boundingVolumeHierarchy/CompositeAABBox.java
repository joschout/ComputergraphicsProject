package boundingVolumeHierarchy;
import math.Point;
import shape.Intersectable;

public interface CompositeAABBox extends Intersectable{

	public Point getP0();
	
	public Point getP1();
}
