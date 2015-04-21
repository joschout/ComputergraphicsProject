package mapping;

import math.Point;
import texture.TexelCoordinates;
/**
 * Mapping of a 2D image onto a 3D object. 
 * Involves mapping the ray-object hit point to a pixel in the 2D image
 * 
 * gebaseerd op handboek pagina 655-661
 * @author Jonas
 *
 */
public class LightProbeMapping extends Mapping {
	
	private LightProbeMapType mapType;
	
	public LightProbeMapping() {
		mapType = LightProbeMapType.LIGHT_PROBE;
	}
	
	@Override
	public TexelCoordinates getTexelCoordinates(Point localHitpoint, int hres,
			int vres) {
		double x = localHitpoint.x;
		double y = localHitpoint.y;
		double z = localHitpoint.z;
		
		/*
		 * zie handboek pagina 656
		 * sin(beta) = y/ sqrt(x^2 + y^2)
		 * cos(beta) = x/ sqrt(x^2 + y^2)
		 */
		double denominator = Math.sqrt(x*x + y*y);
		double sinOfBeta = y/denominator;
		double cosOfbeta = x/denominator;
		
		double alpha = 0;
		if (mapType.equals(LightProbeMapType.LIGHT_PROBE)){ // default
			alpha = Math.acos(z);
		}
		
		if (mapType.equals(LightProbeMapType.PANORAMIC)){
			alpha = Math.acos(-z);
		}
		
		double r = alpha/Math.PI;
		double u = (1.0 + r*cosOfbeta)/2;
		double v = (1.0 + r*sinOfBeta)/2;
		
		//map u and v to the texel coordinates
		int xp = (int) ((hres - 1) * u);
		int yp = (int) ((vres-1) - (vres - 1) * v);
		return new TexelCoordinates(xp, yp);
	}



	public void setMapType(LightProbeMapType mapType) {
		this.mapType = mapType;
	}

}
