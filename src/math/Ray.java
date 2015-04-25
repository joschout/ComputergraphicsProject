package math;

/**
 * Represents an {@link Ray} in three dimensional space starting at a given
 * {@link Point} and extending infinitely in a given direction.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public class Ray implements Cloneable {
	/**
	 * The origin of the ray.
	 */
	public final Point origin;

	/**
	 * The direction the ray is pointing to.
	 */
	public final Vector direction;
	
	/**
	 * The recursion depth of the ray
	 */
	public final int recursionDepth;

	/**
	 * Creates a new {@link Ray} starting at the given origin and propagating
	 * in the given direction with the given recursion depth.
	 * 
	 * @param origin
	 *            the origin of the ray.
	 * @param direction
	 *            the direction of the ray.
	 * @param recusursionDepth
	 * 			  the recursion depth of the ray, element of [0, infinity)         
	 * @throws NullPointerException
	 *             when the given origin and/or direction is null.
	 */
	public Ray(Point origin, Vector direction, int recursionDepth) throws NullPointerException {
		if (origin == null)
			throw new NullPointerException("the given origin is null!");
		if (direction == null)
			throw new NullPointerException("the given direction is null!");
		if (recursionDepth < 0){
			throw new IllegalArgumentException("the given recursion depth cannot be smaller than zero");
		}
		this.origin = origin;
		this.direction = direction.normalize();
		this.recursionDepth = recursionDepth;
	}
	
	/**
	 * Creates a new {@link Ray} starting at the given origin and propagating
	 * in the given direction, with a recursion depth of zero.
	 * @param origin
	 * @param direction
	 */
	public Ray(Point origin, Vector direction){
		this(origin, direction, 0);
	}
	

	/**
	 * Creates a copy of the given {@link Ray}.
	 * 
	 * @param ray
	 *            the ray to copy.
	 * @throws NullPointerException
	 *             when the given ray is null.
	 */
	public Ray(Ray ray) throws NullPointerException {
		this(ray.origin, ray.direction, ray.recursionDepth);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Ray(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("[Ray3D] from %s %s %s in direction %s %s %s",
				origin.x, origin.y, origin.z, direction.x, direction.y,
				direction.z);
	}
}
