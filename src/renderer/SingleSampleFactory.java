package renderer;

import sampling.Sample;
import sampling.SampleFactory;

public class SingleSampleFactory extends SampleFactory {

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
