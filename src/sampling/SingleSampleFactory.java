package sampling;

/**
 * Most basic form factory sampling a 2D sample from a square.
 *  The factory always gives back the center of the square form which it samples.
 * @author Jonas
 *
 */
public class SingleSampleFactory extends SquareSampleFactory {

	/**
	 * Constructs a sample factory which only gives its center back as its samples.
	 * @param sampleSquareCenterX
	 * @param sampleSquareCenterY
	 * @param sampleSquareWidth
	 * @param sampleSquareHeight
	 */
	public SingleSampleFactory(double sampleSquareCenterX, double sampleSquareCenterY,
			double sampleSquareWidth, double sampleSquareHeight) {
		super(sampleSquareCenterX, sampleSquareCenterY, sampleSquareWidth, sampleSquareHeight);
	}

	/**
	 * @return the center of the square from which it samples
	 */
	@Override
	public Sample getNextSample() {
		return new Sample(getSampleSquareCenterX(), getSampleSquareCenterY());
	}
	
	/**
	 * Constructs a unit square around the given center.
	 * @param pixelCenterX
	 * @param pixelCenterY
	 */
	public SingleSampleFactory(double pixelCenterX, double pixelCenterY) {
		super(pixelCenterX, pixelCenterY, 1, 1);
	}

}
