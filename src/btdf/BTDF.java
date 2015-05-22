package btdf;

import math.Vector;
import util.ShadeRec;

/**
 * Class for all bidirectional transmission distribution functions.
 * 
 * See book page 573 for the class diagram.
 * 
 * @author Jonas
 *
 */
public abstract class BTDF {

	/**
	 * cfr tir function in the book, page 
	 * @param sr
	 * @return
	 */
	public boolean checkTotalInternalReflection(ShadeRec sr, Vector wo){
		return false;
	}
}
