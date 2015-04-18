package camera;

import math.OrthonormalBasis;
import math.Point;
import math.Ray;
import math.Vector;
import sampling.DiskSampleFactory;
import sampling.Sample;
import sampling.SquareSampleFactory;

public class ThinLensCamera extends Camera {
	
	private final double width;
	private final double height;
	
	private final double lensRadius;
	private final double focalPlaneDistance;
//	private final double zoomFactor;
	private DiskSampleFactory diskSampleFactory;
	
	
	/**
	 * Creates a new {@link ThinLens} for an image with the given
	 * resolution, at the given position, looking into the given direction with
	 * the given up vector as the up direction. The field of view parameter
	 * specifies the horizontal field of view in degrees.
	 * 
	 * @param xResolution
	 *            x resolution of the image this camera is for. (the number of pixels in the horizontal direction)
	 * @param yResolution
	 *            y resolution of the image this camera is for. (the number of pixels in the vertical direction)
	 * @param origin
	 *            origin of the camera.
	 * @param lookat
	 *            direction of the camera.
	 * @param up
	 *            up vector.
	 * @param fov
	 *            horizontal field of view (in degrees).
	 * @throws NullPointerException
	 *             when the origin, look at or up vector is null.
	 * @throws IllegalArgumentException
	 *             when the given horizontal or vertical resolution is smaller
	 *             than one.
	 * @throws IllegalArgumentException
	 *             when the field of view is smaller than or equal to zero.
	 * @throws IllegalArgumentException
	 *             when the field of view is larger than or equal to pi (180
	 *             degrees).
	 */
	public ThinLensCamera(int xResolution, int yResolution, Point origin,
			Vector lookat, Vector up, double fov, double lensRadius, double focalPlaneDistance, DiskSampleFactory diskSampleFactory) 
					throws NullPointerException, IllegalArgumentException {
		if (xResolution < 1)
			throw new IllegalArgumentException("the horizontal resolution "
					+ "cannot be smaller than one!");
		if (yResolution < 1)
			throw new IllegalArgumentException("the vertical resolution "
					+ "cannot be smaller than one!");
		if (fov <= 0)
			throw new IllegalArgumentException("the field of view cannot be "
					+ "smaller than or equal to zero degrees!");
		if (fov >= 180)
			throw new IllegalArgumentException("the field of view cannot be "
					+ "larger than or equal to 180 degrees!");
		if (lensRadius < 0.0) {
			throw new IllegalArgumentException("the radius of the thin lens camera"
					+ " cannot be smaller than 0");
		}
//		if (sampleFactory == null) {
//			throw new IllegalArgumentException("the given SquareSampleFactory is null");
//		}

		this.xResolution = xResolution;
		this.yResolution = yResolution;
		this.origin = origin;
		this.basis = new OrthonormalBasis(lookat, up);
		this.lensRadius = lensRadius;
		this.diskSampleFactory = diskSampleFactory;
		this.focalPlaneDistance = focalPlaneDistance;
		/*
		 * 	cfr handbook p. 65 and p. 156:
		 *  width = s*hres/d  = s*xRes/d
		 *    = Width of the view plane/ distance to view plane
		 */
		width = 2.0 * Math.tan(0.5 * Math.toRadians(fov));
		
		/*
		 * 	cfr handbook p. 65 and p. 156:
		 *  height = s*vres/d = s*yRes/d
		 *   = Height of the view plane/ distance to view plane
		 */
		height = ((double) yResolution * width) / (double) xResolution;
	}
	
	
	
	/**
	 * Creates a new {@link ThinLens} for an image with the given
	 * resolution, at the given position, looking into the given direction with
	 * the given up vector as the up direction. The field of view parameter
	 * specifies the horizontal field of view in degrees.
	 * 
	 * @param xResolution
	 *            x resolution of the image this camera is for. (the number of pixels in the horizontal direction)
	 * @param yResolution
	 *            y resolution of the image this camera is for. (the number of pixels in the vertical direction)
	 * @param origin
	 *            origin of the camera.
	 * @param lookat
	 *            direction of the camera.
	 * @param up
	 *            up vector.
	 * @param fov
	 *            horizontal field of view (in degrees).
	 * @throws NullPointerException
	 *             when the origin, look at or up vector is null.
	 * @throws IllegalArgumentException
	 *             when the given horizontal or vertical resolution is smaller
	 *             than one.
	 * @throws IllegalArgumentException
	 *             when the field of view is smaller than or equal to zero.
	 * @throws IllegalArgumentException
	 *             when the field of view is larger than or equal to pi (180
	 *             degrees).
	 */
	public ThinLensCamera(int xResolution, int yResolution, Point origin,
			Vector lookat, Vector up, double fov, double lensRadius, double focalPlaneDistance, SquareSampleFactory squareSampleFactory) 
					throws NullPointerException, IllegalArgumentException {
		if (xResolution < 1)
			throw new IllegalArgumentException("the horizontal resolution "
					+ "cannot be smaller than one!");
		if (yResolution < 1)
			throw new IllegalArgumentException("the vertical resolution "
					+ "cannot be smaller than one!");
		if (fov <= 0)
			throw new IllegalArgumentException("the field of view cannot be "
					+ "smaller than or equal to zero degrees!");
		if (fov >= 180)
			throw new IllegalArgumentException("the field of view cannot be "
					+ "larger than or equal to 180 degrees!");
		if (lensRadius < 0.0) {
			throw new IllegalArgumentException("the radius of the thin lens camera"
					+ " cannot be smaller than 0");
		}
//		if (sampleFactory == null) {
//			throw new IllegalArgumentException("the given SquareSampleFactory is null");
//		}

		this.xResolution = xResolution;
		this.yResolution = yResolution;
		this.origin = origin;
		this.basis = new OrthonormalBasis(lookat, up);
		this.lensRadius = lensRadius;
		this.diskSampleFactory = new DiskSampleFactory(squareSampleFactory);
		this.focalPlaneDistance = focalPlaneDistance;
		/*
		 * 	cfr handbook p. 65 and p. 156:
		 *  width = s*hres/d  = s*xRes/d
		 *    = Width of the view plane/ distance to view plane
		 */
		width = 2.0 * Math.tan(0.5 * Math.toRadians(fov));
		
		/*
		 * 	cfr handbook p. 65 and p. 156:
		 *  height = s*vres/d = s*yRes/d
		 *   = Height of the view plane/ distance to view plane
		 */
		height = ((double) yResolution * width) / (double) xResolution;
	}
	



	@Override
	public Ray generateRay(Sample sample) throws NullPointerException {

		/*
		 * the Orthonormal basis consists of u, v, x axes
		 * --> we need to calculate the u, v, w coordinates of the ray.
		 * 
		 * 
		 * sample.x in [0, xResolution]
		 * sample.y in [0, yResolution] 
		 * 
		 * sample.x/xResolution in [0, 1]
		 * sample.y/yResolution in [0, 1]
		 * 
		 * (sample.x/xResolution -0.5) in [-0.5, 0.5]
		 * (sample.y/yResolution -0.5) in [-0.5, 0.5]
		 * 
		 * 
		 * width = width of the view plane/ distance to view plane
		 * height =  height of the view plane/ distance to view plane
		 *  
		 */
		double viewPlanePointUCoord = width * ((sample.x / (double) xResolution) - 0.5);
		double viewPlanePointVCoord = height * ((sample.y / (double) yResolution) - 0.5);
		//double wCoordinateOfViewPlanePoint = -1
		
		double focalPlanePointUCoord = viewPlanePointUCoord * focalPlaneDistance;
		double focalPlanePointVCoord = viewPlanePointVCoord * focalPlaneDistance;
		//double wCoordinateOfFocalPlanePoint = -focalPlaneDistance
		
		Sample diskSample = diskSampleFactory.getNextSample();
		double lensPointUCoord = origin.x + diskSample.x * lensRadius;
		double lensPointVCoord = origin.y + diskSample.y * lensRadius;
		Point primaryRayOrigin = new Point(lensPointUCoord, lensPointVCoord, origin.z);
		
		
		double primaryRayUCoord = focalPlanePointUCoord - lensPointUCoord;
		double primaryRayVCoord = focalPlanePointVCoord - lensPointVCoord;
		double primaryRayWcoord = - focalPlaneDistance;

		Vector primaryRayDirection = basis.w.scale(primaryRayWcoord) //in de richting van de negatieve z-as
						.add(basis.u.scale(primaryRayUCoord)
						.add(basis.v.scale(primaryRayVCoord)));

		return new Ray(primaryRayOrigin, primaryRayDirection);	
	}


}
