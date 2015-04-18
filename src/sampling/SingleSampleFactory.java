package sampling;


public class SingleSampleFactory extends SquareSampleFactory {

	public SingleSampleFactory(double pixelCenterX, double pixelCenterY,
			double pixelwidth, double pixelHeight) {
		super(pixelCenterX, pixelCenterY, pixelwidth, pixelHeight);
	}

	@Override
	public Sample getNextSample() {
		return new Sample(getPixelCenterX(), getPixelCenterY());
	}
	
	public SingleSampleFactory(double pixelCenterX, double pixelCenterY) {
		super(pixelCenterX, pixelCenterY, 1, 1);
	}

}
