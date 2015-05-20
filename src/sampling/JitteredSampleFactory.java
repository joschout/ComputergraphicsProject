package sampling;

import java.util.Random;

/**
 * Factory creating multi-jittered samples in a square.
 * 
 * 
 * @author Jonas
 *
 */
public class JitteredSampleFactory extends SquareSampleFactory{
	
	private Random randomNumberGenerator;


	private double rightSampleSquareBorder;
	private double upperSampleSquareBorder;
	
	private double subcellWidth;
	private double subcellHeight;
	
	private double currentSubcellCenterX;
	private double currentSubcellCenterY;

	private double initialSubcellCenterX;
	private double initialSubcellCenterY;
	
	/**
	 * 
	 * 
	 * @param sampleSquareCenterX
	 * @param sampleSquareCenterY
	 * @param sampleSquareWidth
	 * @param sampleSquareHeight
	 * @param nbOfSubcellsXDimension
	 * @param nbOfSubcellsYDimension
	 */
	public JitteredSampleFactory(double sampleSquareCenterX, double sampleSquareCenterY,
			double sampleSquareWidth, double sampleSquareHeight, int nbOfSubcellsXDimension, int nbOfSubcellsYDimension) {
		super(sampleSquareCenterX, sampleSquareCenterY, sampleSquareWidth, sampleSquareHeight);
		
		this.subcellWidth = sampleSquareWidth/nbOfSubcellsXDimension;
		this.subcellHeight = sampleSquareHeight/nbOfSubcellsYDimension;
		
		reset(sampleSquareCenterX,sampleSquareCenterY);
		
		randomNumberGenerator = new Random((long)(sampleSquareCenterX*sampleSquareWidth+sampleSquareCenterY*sampleSquareHeight));
	}

	/**
	 * 
	 * @param sampleSquareCenterX
	 * @param sampleSquareCenterY
	 * @param sampleSquareWidth
	 * @param sampleSquareHeight
	 * @param nbOfSubCellsPerDimension
	 */
	public JitteredSampleFactory(double sampleSquareCenterX, double sampleSquareCenterY,
			double sampleSquareWidth, double sampleSquareHeight, int nbOfSubCellsPerDimension) {
		this(sampleSquareCenterX, sampleSquareCenterY, sampleSquareWidth, sampleSquareHeight, nbOfSubCellsPerDimension, nbOfSubCellsPerDimension);
	}
	
	/**
	 * 
	 * @param sampleSquareCenterX
	 * @param pixelCenterY
	 * @param sampleSquareSideLength
	 * @param nbOfSubCellsPerDimension
	 */
	public JitteredSampleFactory(double sampleSquareCenterX, double sampleSquareCenterY,
			double sampleSquareSideLength, int nbOfSubCellsPerDimension) {
		this(sampleSquareCenterX, sampleSquareCenterY, sampleSquareSideLength, sampleSquareSideLength, nbOfSubCellsPerDimension);
	}
	
	/**
	 * 
	 * @param sampleSquareSideLength
	 * @param nbOfSubCellsPerDimension
	 */
	public JitteredSampleFactory(double sampleSquareSideLength, int nbOfSubCellsPerDimension) {
		this(0, 0, sampleSquareSideLength, sampleSquareSideLength, nbOfSubCellsPerDimension);
	}
	
	/**
	 * 
	 * @param nbOfSubCellsPerDimension
	 */
	public JitteredSampleFactory(int nbOfSubCellsPerDimension) {
		this(1, nbOfSubCellsPerDimension);
	}
	
	

	@Override
	public Sample getNextSample() {
		
		double x = currentSubcellCenterX +  (randomNumberGenerator.nextDouble() - 0.5) * subcellWidth;
		double y = currentSubcellCenterY + ( randomNumberGenerator.nextDouble() - 0.5) * subcellHeight;
		
		incrementCurrentSubcell();
		
		return new Sample(x, y);
	}

	private void incrementCurrentSubcell() {
		//increment
		currentSubcellCenterX = currentSubcellCenterX + subcellWidth;
		if(currentSubcellCenterX > rightSampleSquareBorder){
			currentSubcellCenterX = initialSubcellCenterX;
			currentSubcellCenterY = currentSubcellCenterY + subcellHeight;
			if (currentSubcellCenterY > upperSampleSquareBorder) {
				currentSubcellCenterY = initialSubcellCenterY;
			}
		}
	}
	
	public void reset(double pixelCenterX, double pixelCenterY){
		super.reset(pixelCenterX, pixelCenterY);
		rightSampleSquareBorder = pixelCenterX + getSampleSquareWidth()/2;
		upperSampleSquareBorder = pixelCenterY + getSampleSquareHeight()/2;
		
		initialSubcellCenterX = pixelCenterX - getSampleSquareWidth()/2 + subcellWidth/2;
		initialSubcellCenterY = pixelCenterY - getSampleSquareHeight()/2 + subcellHeight/2;
		
		resetCurrentSubcell();
	}
	
	
	public void resetCurrentSubcell(){
		this.currentSubcellCenterX = this.initialSubcellCenterX;
		this.currentSubcellCenterY = this.initialSubcellCenterY;
	}
	
	
}
