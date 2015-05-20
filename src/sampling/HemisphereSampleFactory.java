package sampling;

import math.OrthonormalBasis;
import math.Point;
import math.Vector;
/**
 * Class implementing a factory which samples from a hemisphere.
 * It is based on chapter 7 from the book Ray tracing from the Ground Up.
 * 
 * The hemisphere is defined by an orthonormal basis which has a u, v and w axis.
 * The w-axis points to the top of the hemisphere, the u and v axes are in the plane under the hemisphere.
 * 
 * The factory maps sample points from a square 
 * to a hemisphere with a cosine power density distribution.
 * 
 * {@link HemisphereSampleFactory} objects make use of an object of {@link SquareSampleFactory} to get samples fron a square.
 * Note that only samples within [0, 1] x [0, 1] are allowed.
 * This means that the factory should only have a {@link SquareSampleFactory} object as an attribute where:
 * 		SquareSampleFactory.getSampleSquareCenterX() == 0.5
 * 		SquareSampleFactory.getSampleSquareCenterY() == 0.5
 * 		SquareSampleFactory.getSampleSquareHeight() == 1
 * 		SquareSampleFactory.getSampleSquareHwidth() == 1
 * 
 * @author Jonas
 *
 */
public class HemisphereSampleFactory  {

	private SquareSampleFactory sampleFactory;
	// exponent element of [0, infinity)
	private double exponent;
	/*
	 * w-axis points to the top of the hemisphere
	 * 
	 * for example:
	 * 	- the surface normal at a hit point
	 *  - the reflected or transmitted ray
	 */
	public OrthonormalBasis localOrthonormalbasis;

	
	
	/**
	 * Con
	 * 
	 * 
	 * @param centerOfHemisphere
	 * @param localOrigin
	 * @param sampleFactory
	 * @param exponent
	 */
	public HemisphereSampleFactory(Vector centerOfHemisphere,
			SquareSampleFactory sampleFactory, double exponent){
		
		if (sampleFactory == null) {
			throw new NullPointerException("the given SquareSampleFactory cannot be null");
		}
		if (sampleFactory.getSampleSquareCenterX() != 0.5){
			throw new IllegalArgumentException("the x coord of the sampleSquare must be equal to 0.5");
		}
		if (sampleFactory.getSampleSquareCenterY() != 0.5){
			throw new IllegalArgumentException("the y coord of the sampleSquare must be equal to 0.5");
		}
		if (sampleFactory.getSampleSquareHeight()!= 1){
			throw new IllegalArgumentException("the height of the sampleSquare must be equal to 1");
		}
		if (sampleFactory.getSampleSquareWidth() != 1){
			throw new IllegalArgumentException("the length of the sampleSquare must be equal to 1");
		}
		this.sampleFactory = sampleFactory;
		
		if (centerOfHemisphere == null) {
			throw new NullPointerException("the given vector pointing to the center of the hemisphere cannot be null");
		}
		this.localOrthonormalbasis = new OrthonormalBasis(centerOfHemisphere);
		
		if (exponent < 0) {
			throw new IllegalArgumentException("the given exponent has to be element of the interval [0, infinity) ");
		}
		this.exponent = exponent;	
	}
	
	public HemisphereSampleFactory(Vector centerOfHemisphere, double exponent) {
		this(centerOfHemisphere, new RandomSampleFactory(0.5, 0.5, 1, 1), exponent);	
	}
	
	/**
	 * 
	 * @param centerOfHemisphere
	 * @param localOrigin
	 */
	public HemisphereSampleFactory(Vector centerOfHemisphere) {
		this(centerOfHemisphere, new RandomSampleFactory(0.5, 0.5, 1, 1), 1);	
	}

	/**
	 * 
	 * @return
	 */
	public Vector getNextSample(){
		
		/*
		 * Point = [sin(theta)*cos(phi), sin(theta)*sin(phi), cos(theta)]
		 */
		Sample squareSample = sampleFactory.getNextSample();
		
		double cosOfPhi = Math.cos(2.0 * Math.PI * squareSample.x);
		double sinOfPhi = Math.sin(2.0 * Math.PI * squareSample.x);
		
		double cosOfTheta = Math.pow((1.0 - squareSample.y), 1.0/(exponent + 1.0));
		double sinOfTheta = Math.sqrt(1.0 - cosOfTheta * cosOfTheta);
		
		double uCoord = sinOfTheta * cosOfPhi;
		double vCoord = sinOfTheta * sinOfPhi;
		double wCoord = cosOfTheta;
		
		Vector hemisphereSampleVector = localOrthonormalbasis.u.scale(uCoord)
				.add(localOrthonormalbasis.v.scale(vCoord))
				.add(localOrthonormalbasis.w.scale(wCoord)).normalize();
		return hemisphereSampleVector;	
	}
	
	/**
	 * 
	 * @param normal
	 * @return
	 */
	public Vector getNextSample(Vector normal){
		Sample squareSample = sampleFactory.getNextSample();
		
		double cosOfPhi = Math.cos(2.0 * Math.PI * squareSample.x);
		double sinOfPhi = Math.sin(2.0 * Math.PI * squareSample.x);
		
		double cosOfTheta = Math.pow((1.0 - squareSample.y), 1.0/(exponent + 1.0));
		double sinOfTheta = Math.sqrt(1.0 - cosOfTheta * cosOfTheta);
		
		double uCoord = sinOfTheta * cosOfPhi;
		double vCoord = sinOfTheta * sinOfPhi;
		double wCoord = cosOfTheta;
		
		Vector hemisphereSampleVector = localOrthonormalbasis.u.scale(uCoord)
				.add(localOrthonormalbasis.v.scale(vCoord))
				.add(localOrthonormalbasis.w.scale(wCoord)).normalize();
//		System.out.println("wi old: " + hemisphereSampleVector.toString());
//		System.out.println("localOrthonormalbasis.w: "+ localOrthonormalbasis.w.toString());
//		System.out.println("r.dot(wi old): " + localOrthonormalbasis.w.dot(hemisphereSampleVector));
		if (normal.dot(hemisphereSampleVector) < 0.0) {// if direction is below surface
			
			hemisphereSampleVector = localOrthonormalbasis.u.scale(-1* uCoord)
					.add(localOrthonormalbasis.v.scale(-1* vCoord))
					.add(localOrthonormalbasis.w.scale(wCoord)).normalize();
//			System.out.println("wi new: " + hemisphereSampleVector.toString());;
//			System.out.println("r.dot(wi new): " + localOrthonormalbasis.w.dot(hemisphereSampleVector));
		}
		
		return hemisphereSampleVector;
	}
	
	/**
	 * Sets the exponent used in the of the power density distribution of the hemisphere samples.
	 * The only allowed values of the exponent are in the interval [0, infinity).
	 * 
	 * @throws IllegalArgumentException if exponent < 0
	 * @param exponent
	 */
	public void setExponent(double exponent){
		if (exponent < 0) {
			throw new IllegalArgumentException("the given exponent has to be element of the interval [0, infinity) ");
		}
		this.exponent = exponent;
	}
	
	public void reset(Vector centerOfHemisphere){
		this.reset(centerOfHemisphere, this.exponent);
	}
	
	public void reset(Vector centerOfHemisphere, double exponent){
		this.localOrthonormalbasis = new OrthonormalBasis(centerOfHemisphere);
		this.sampleFactory.reset(sampleFactory.getSampleSquareCenterX(), sampleFactory.getSampleSquareCenterY());
		setExponent(exponent);
	}
	
}
