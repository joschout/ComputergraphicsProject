package sampling;

import java.util.Random;

public class JitteredSampleFactory extends SampleFactory{
	
	private Random randomNumberGenerator;


	private double rightPixelBorder;
	private double upperPixelBorder;
	
	private double subcellWidth;
	private double subcellHeight;
	
	private double currentSubcellCenterX;
	private double currentSubcellCenterY;

	private double initialSubcellCenterX;
	private double initialSubcellCenterY;
	
	public JitteredSampleFactory(double pixelCenterX, double pixelCenterY,
			double pixelWidth, double pixelHeight, int nbOfSubcellsXDimension, int nbOfSubcellsYDimension) {
		super(pixelCenterX, pixelCenterY, pixelWidth, pixelHeight);
		
		rightPixelBorder = pixelCenterX + pixelWidth/2;
		upperPixelBorder = pixelCenterY + pixelHeight/2;
		
		subcellWidth = pixelWidth/nbOfSubcellsXDimension;
		subcellHeight = pixelHeight/nbOfSubcellsYDimension;
		
		initialSubcellCenterX = pixelCenterX - pixelWidth/2 + subcellWidth;
		initialSubcellCenterY = pixelCenterY - pixelHeight/2 + subcellHeight;
		
		currentSubcellCenterX = initialSubcellCenterX;
		currentSubcellCenterY = initialSubcellCenterY;
		
		
		randomNumberGenerator = new Random();
	}

	public JitteredSampleFactory(double pixelCenterX, double pixelCenterY,
			double pixelWidth, double pixelHeight, int nbOfSubCellsPerDimension) {
		super(pixelCenterX, pixelCenterY, pixelWidth, pixelHeight);
		
		rightPixelBorder = pixelCenterX + pixelWidth/2;
		upperPixelBorder = pixelCenterY + pixelHeight/2;
		
		subcellWidth = pixelWidth/nbOfSubCellsPerDimension;
		subcellHeight = pixelHeight/nbOfSubCellsPerDimension;
		
		initialSubcellCenterX = pixelCenterX - pixelWidth/2 + subcellWidth;
		initialSubcellCenterY = pixelCenterY - pixelHeight/2 + subcellHeight;
		
		currentSubcellCenterX = initialSubcellCenterX;
		currentSubcellCenterY = initialSubcellCenterY;
		
		
		randomNumberGenerator = new Random();
	}
	
	public JitteredSampleFactory(double pixelCenterX, double pixelCenterY,
			double pixelWidth, double pixelHeight, int nbOfSubcellsXDimension, int nbOfSubcellsYDimension, long seed) {
		super(pixelCenterX, pixelCenterY, pixelWidth, pixelHeight);
		
		
		rightPixelBorder = pixelCenterX + pixelWidth/2;
		upperPixelBorder = pixelCenterY + pixelHeight/2;
		
		subcellWidth = pixelWidth/nbOfSubcellsXDimension;
		subcellHeight = pixelHeight/nbOfSubcellsYDimension;
		
		initialSubcellCenterX = pixelCenterX - pixelWidth/2 + subcellWidth;
		initialSubcellCenterY = pixelCenterY - pixelHeight/2 + subcellHeight;
		
		currentSubcellCenterX = initialSubcellCenterX;
		currentSubcellCenterY = initialSubcellCenterY;
		
		randomNumberGenerator = new Random(seed);
	}
	
	
	
	
//	public Sample makeSample(double xCenter, double yCenter){
//		double xLeftBound = xCenter - 0.5;
//		double xRightBound = xCenter + 0.5;
//		
//		double yLeftBound = yCenter - 0.5;
//		double yRightBound = yCenter + 0.5;
//		
//		double x = xLeftBound + randomNumberGenerator.nextDouble()*(xRightBound - xLeftBound);
//		double y = yLeftBound + randomNumberGenerator.nextDouble()*(yRightBound - yLeftBound);
//		
//		return new Sample(x,y);
//		
//	}

	@Override
	public Sample getNextSample() {
		
		double x = currentSubcellCenterX +  (randomNumberGenerator.nextDouble() - 0.5) * subcellWidth;
		double y = currentSubcellCenterY + ( randomNumberGenerator.nextDouble() - 0.5) * subcellHeight;
		
		//increment
		currentSubcellCenterX = currentSubcellCenterX + subcellWidth;
		if(currentSubcellCenterX > rightPixelBorder){
			currentSubcellCenterX = initialSubcellCenterX;
			currentSubcellCenterY = currentSubcellCenterY + subcellHeight;
			if (currentSubcellCenterY > upperPixelBorder) {
				currentSubcellCenterY = initialSubcellCenterY;
			}
		}
		return new Sample(x, y);
	}
	
	
	
	
}
