package sampling;

import java.util.Random;

public class RandomSampleFactory extends SquareSampleFactory {

	private Random randomNumberGenerator;
	
	public RandomSampleFactory(double pixelCenterX, double pixelCenterY,
			double pixelwidth, double pixelHeight) {
		super(pixelCenterX, pixelCenterY, pixelwidth, pixelHeight);
		randomNumberGenerator = new Random();
	}

	public RandomSampleFactory(double pixelCenterX, double pixelCenterY,
			double pixelwidth, double pixelHeight, long seed) {
		super(pixelCenterX, pixelCenterY, pixelwidth, pixelHeight);
		randomNumberGenerator = new Random(seed);
	}
	
	@Override
	public Sample getNextSample(){
		double x = getPixelCenterX() +  (randomNumberGenerator.nextDouble() - 0.5) * getPixelWidth();
		double y = getPixelCenterY() + ( randomNumberGenerator.nextDouble() - 0.5) * getPixelHeight();
		return new Sample(x, y);
	}

}
