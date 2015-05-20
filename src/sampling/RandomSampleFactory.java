package sampling;

import java.util.Random;
/**
 * Factory generating samples in a square. 
 * Samples are calculated as random offsets to the center of the sampled square.
 * It uses on object of Random to generate both the x and y offset.
 * 
 * @author Jonas
 *
 */
public class RandomSampleFactory extends SquareSampleFactory {

	private Random randomNumberGenerator;
	
	/**
	 * Constructs a {@link RandomSampleFactory} object.
	 * The objects samples from a square centered around the given (x,y)-coordinates.
	 * The square has the given dimensions. 
	 * 
	 * Also given as an argument is an object of java.util.Random. 
	 * This object is used to calculate the samples.
	 * Samples are calculated by adding an offset to the center of the sampled square.
	 * Note that the same Random object is used to calculate both the offsets in the x and y direction,
	 *  in that order.
	 *  
	 * @param sampleSquareCenterX	the x-coordinate of the sampled square
	 * @param sampleSquareCenterY	the y-coordinate of the sampled square
	 * @param sampleSquareWidth		the width of the sampled square
	 * @param sampleSquareHeight 	the height of the sampled square
	 * @param randomNumberGenerator
	 */
	public RandomSampleFactory(double sampleSquareCenterX, double sampleSquareCenterY,
			double sampleSquareWidth, double sampleSquareHeight, Random randomNumberGenerator) {
		super(sampleSquareCenterX, sampleSquareCenterY, sampleSquareWidth, sampleSquareHeight);
		this.randomNumberGenerator = randomNumberGenerator;
	}
	
	/**
	 * Constructs a {@link RandomSampleFactory} object.
	 * The objects samples from a square centered around the given (x,y)-coordinates.
	 * The square has the given dimensions
	 * 
	 * Also given as an argument is a long value to be which is used as a seed for an object of java.util.Random.
	 * This Random object is used to calculate the samples.
	 * Samples are calculated by adding an offset to the center of the sampled square.
	 * Note that the same Random object is used to calculate both the offsets in the x and y direction,
	 *  in that order.
	 * 
	 * @param sampleSquareCenterX	the x-coordinate of the sampled square
	 * @param sampleSquareCenterY	the y-coordinate of the sampled square
	 * @param sampleSquareWidth		the width of the sampled square
	 * @param sampleSquareHeight 	the height of the sampled square
	 * @param seed		the seed for an object of Random used to calculate the random values inside the sampled square
	 */
	public RandomSampleFactory(double sampleSquareCenterX, double sampleSquareCenterY,
			double sampleSquareWidth, double sampleSquareHeight, long seed) {
		super(sampleSquareCenterX, sampleSquareCenterY, sampleSquareWidth, sampleSquareHeight);
		randomNumberGenerator = new Random(seed);
	}

	/**
	 * Constructs a {@link RandomSampleFactory} object.
	 * The objects samples from a square centered around the given (x,y)-coordinates.
	 * The square has the given dimensions.
	 * 
	 * This construction uses the Random() constructor of java.util.Random to create a Random object.
	 * This Random object is used to calculate the samples.
	 * Samples are calculated by adding an offset to the center of the sampled square.
	 * Note that the same Random object is used to calculate both the offsets in the x and y direction,
	 *  in that order.
	 * 
	 * @param sampleSquareCenterX	the x-coordinate of the sampled square
	 * @param sampleSquareCenterY	the y-coordinate of the sampled square
	 * @param sampleSquareWidth		the width of the sampled square
	 * @param sampleSquareHeight 	the height of the sampled square
	 */
	public RandomSampleFactory(double sampleSquareCenterX, double sampleSquareCenterY,
			double sampleSquareWidth, double sampleSquareHeight){
		this(sampleSquareCenterX, sampleSquareCenterY,
				sampleSquareWidth, sampleSquareHeight,
				new Random());
	}

	/**
	 * 	 * Constructs a {@link RandomSampleFactory} object.
	 * The objects samples from a square centered around the given (x,y)-coordinates.
	 * 
	 * The square has a width and height of 1.
	 * 
	 * This construction uses the Random() constructor of java.util.Random to create a Random object.
	 * This Random object is used to calculate the samples.
	 * Samples are calculated by adding an offset to the center of the sampled square.
	 * Note that the same Random object is used to calculate both the offsets in the x and y direction,
	 *  in that order.
	 * 
	 * @param sampleSquareCenterX	the x-coordinate of the sampled square
	 * @param sampleSquareCenterY	the y-coordinate of the sampled square
	 */
	public RandomSampleFactory(double sampleSquareCenterX, double sampleSquareCenterY){
		this(sampleSquareCenterX, sampleSquareCenterY,
				1, 1, new Random());
	}
	
	/**
	 * Calculates the next sample by adding an offset to the center of the sampled square.
	 * Note that the same Random object is used to calculate both the offsets in the x and y direction,
	 *  in that order.
	 */
	@Override
	public Sample getNextSample(){
		double x = getSampleSquareCenterX() +  (randomNumberGenerator.nextDouble() - 0.5) * getSampleSquareWidth();
		double y = getSampleSquareCenterY() + ( randomNumberGenerator.nextDouble() - 0.5) * getSampleSquareHeight();
		return new Sample(x, y);
	}

}
