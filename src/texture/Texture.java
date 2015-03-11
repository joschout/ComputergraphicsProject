package texture;

import util.RGBColor;
import util.ShadeRec;

public interface Texture {

	/**
	 * Returns an RGBColor given a ray-object hit point.
	 * Takes a ShadeRec reference as a parameter,
	 * because the getColor-method of implementing subclass ImageTexture
	 *  uses the (u,v)-texture coordinates at the hitpoint, which are stored in the SHadeRec object.
	 * @param sr
	 * @return
	 */
	public RGBColor getColor(ShadeRec sr);
}
