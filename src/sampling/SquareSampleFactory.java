package sampling;

/**
 * Base class for all factories samplinng 2D-samples on a square.
 * @author Jonas
 *
 */
public abstract class SquareSampleFactory  implements SampleFactory{
	
	// the horizontal center of the pixel in the image space
	private double sampleSquareCenterX;
	// the vertical center of the pixel in the image space
	private double sampleSquareCenterY;
	
	// the width of the pixel in the image space
	private double sampleSquareWidth;
	//the height of the pixel in the image space
	private double sampleSquareHeight;
	
	
	/**
	 * Constructor for the abstract class. Called by the constructors of its subclasses.
	 * Initializes the center of the square and the width and height of the square.
	 *  
	 * @param sampleSquareCenterX
	 * @param sampleSquareCenterY
	 * @param sampleSquareWidth
	 * @param sampleSquareHeight
	 */
	public SquareSampleFactory(double sampleSquareCenterX, double sampleSquareCenterY,
			double sampleSquareWidth, double sampleSquareHeight) {
		
		super();
		this.sampleSquareCenterX = sampleSquareCenterX;
		this.sampleSquareCenterY = sampleSquareCenterY;
		this.sampleSquareWidth = sampleSquareWidth;
		this.sampleSquareHeight = sampleSquareHeight;
	}
	
	/**
	 * Constructor for the abstract class. Called by the constructors of its subclasses.
	 * Constructs a unit square around the given center.
	 * 
	 * @param sampleSquareCenterX
	 * @param sampleSquareCenterY
	 */
	
	public SquareSampleFactory(double sampleSquareCenterX, double sampleSquareCenterY) {
		this(sampleSquareCenterX, sampleSquareCenterY, 1, 1);
	}

	public double getSampleSquareCenterX() {
		return sampleSquareCenterX;
	}

	public void setSampleSquareCenterX(double sampleSquareCenterX) {
		this.sampleSquareCenterX = sampleSquareCenterX;
	}

	public double getSampleSquareCenterY() {
		return sampleSquareCenterY;
	}

	public void setSampleSquareCenterY(double sampleSquareCenterY) {
		this.sampleSquareCenterY = sampleSquareCenterY;
	}

	public double getSampleSquareWidth() {
		return sampleSquareWidth;
	}

	public void setSampleSquareWidth(double sampleSquareWidth) {
		this.sampleSquareWidth = sampleSquareWidth;
	}

	public double getSampleSquareHeight() {
		return sampleSquareHeight;
	}

	public void setSampleSquareHeight(double sampleSquareHeight) {
		this.sampleSquareHeight = sampleSquareHeight;
	}
	
	
	/**
	 * Most basic reset method, which resets the center of the square this factory samples from.
	 * @param sampleSquareCenterX
	 * @param sampleSquareCenterY
	 */
	public void reset(double sampleSquareCenterX, double sampleSquareCenterY){
		this.sampleSquareCenterX = sampleSquareCenterX;
		this.sampleSquareCenterY = sampleSquareCenterY;
	}
	
}
