package mapping;

import math.Point;
import shape.Cylinder;
import texture.TexelCoordinates;

public class CylindricalMapping extends Mapping {
	
	private Cylinder cylinder;
	
	public CylindricalMapping(Cylinder cylinder) {
		if (cylinder == null){
			throw new IllegalArgumentException("the given cylinder is null");
		}
		this.cylinder = cylinder;
	}
	
	public CylindricalMapping() {
		this.cylinder = new Cylinder();
	}

	@Override
	public TexelCoordinates getTexelCoordinates(Point localHitpoint, int hres,
			int vres) {
		double phi = Math.atan2(localHitpoint.x, localHitpoint.z);
		if(phi < 0.0){
			phi = phi + (Math.PI*2);
		}
		
		//map theta an phi to (u, v) in [0,1]x[0,1]
		double u = phi/(2*Math.PI);
		double v = (localHitpoint.y - cylinder.ySmall)/(cylinder.yLarge	- cylinder.ySmall);
		
		
		//map u and v to the texel coordinates
		int xp = (int) ((hres - 1) * u);
		int yp = (int) ((vres-1) -(vres - 1) * v);
		return new TexelCoordinates(xp, yp);
		
		
	}
}
