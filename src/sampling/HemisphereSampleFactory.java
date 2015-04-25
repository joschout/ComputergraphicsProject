package sampling;

import math.OrthonormalBasis;
import math.Point;
import math.Vector;

public class HemisphereSampleFactory  {
	/**
	 * Vector through the top of the hemisphere
	 * for example:
	 * 	- the surface normal at a hit point
	 *  - the reflected or transmitted ray
	 */
	//public Vector centerOfHemisphere;
	public SquareSampleFactory sampleFactory;
	// exponent element of [0, infinity)
	private double exponent;
	public OrthonormalBasis localOrthonormalbasis;
	public Point localOrigin;
	
	public HemisphereSampleFactory(Vector centerOfHemisphere, Point localOrigin,
			SquareSampleFactory sampleFactory, double exponent){
		if (centerOfHemisphere == null) {
			throw new NullPointerException("the given vector pointing to the center of the hemisphere cannot be null");
		}
		//this.centerOfHemisphere = centerOfHemisphere;
		this.localOrthonormalbasis = new OrthonormalBasis(centerOfHemisphere);
		
		if (localOrigin == null) {
			throw new NullPointerException("the given center point of the hemisphere cannot be null");
		}
		this.localOrigin = localOrigin;
		
		if (sampleFactory == null) {
			throw new NullPointerException("the given SquareSampleFactory cannot be null");
		}
		this.sampleFactory = sampleFactory;
		
		if (exponent < 0) {
			throw new IllegalArgumentException("the given exponent has to be element of the interval [0, infinity) ");
		}
		this.exponent = exponent;	
	}
	
	public HemisphereSampleFactory(Vector centerOfHemisphere, Point localOrigin) {
		this(centerOfHemisphere, localOrigin, new RandomSampleFactory(0.5, 0.5, 1, 1), 1);	
	}

	public Point getNextSample(){
		
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
		
		Point hemisphereSamplePoint = localOrigin.add(localOrthonormalbasis.u.scale(uCoord))
				.add(localOrthonormalbasis.v.scale(vCoord))
				.add(localOrthonormalbasis.w.scale(wCoord));
		return hemisphereSamplePoint;	
	}
	
	public void setExponent(double exponent){
		if (exponent < 0) {
			throw new IllegalArgumentException("the given exponent has to be element of the interval [0, infinity) ");
		}
		this.exponent = exponent;
	}
}
