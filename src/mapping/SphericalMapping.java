package mapping;

import math.Point;
import texture.TexelCoordinates;

public class SphericalMapping extends Mapping{

	@Override
	public TexelCoordinates getTexelCoordinates(Point localHitpoint, int hres, int vres) {
		
		//compute theta and phi
		double theta = Math.acos(localHitpoint.y);
		double phi = Math.atan2(localHitpoint.x, localHitpoint.z);
		if(phi < 0.0){
			phi = phi + (Math.PI*2);
		}
		
		//map theta an phi to (u, v) in [0,1]x[0,1]
		double u = phi/(2*Math.PI);
		double v = 1 - theta/Math.PI;
		
		//map u and v to the texel coordinates
		int xp = (int) ((hres - 1) * u);
		int yp = (int) ((vres - 1) * v);
		return new TexelCoordinates(xp, yp);
	}

}
