package camera;

import math.OrthonormalBasis;
import math.Point;
import math.Ray;
import sampling.Sample;

/**
 * Interface which all {@link Camera} subclasses should implement.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public abstract class Camera {
	
	/**
	 * x resolution of the image this camera is for. 
	 * (the number of pixels in the horizontal direction)
	 */
	public int xResolution;
	/**
	 * y resolution of the image this camera is for. 
	 * (the number of pixels in the vertical direction)
	 */
	public int yResolution;
	/**
	 * origin of the camera.
	 */
	public Point origin;
	
	/**
	 * 
	 */
	public OrthonormalBasis basis;
	
	/**
	 * Generates a new {@link Ray} from the given {@link Sample}.
	 * 
	 * @param sample
	 *            sample to construct the {@link Ray} from.
	 * @throws NullPointerException
	 *             when the given {@link Sample} is null.
	 * @return a new {@link Ray} from the given {@link Sample}.
	 */
	public abstract Ray generateRay(Sample sample) throws NullPointerException;
	
	public Point getOrigin(){
		return this.origin;
	}
}
