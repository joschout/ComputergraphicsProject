package sampling;

import java.util.Random;

public class JitteredSampleFactory extends SquareSampleFactory{
	
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
		
		subcellWidth = pixelWidth/nbOfSubcellsXDimension;
		subcellHeight = pixelHeight/nbOfSubcellsYDimension;
		
		reset(pixelCenterX,pixelCenterY);
		
		randomNumberGenerator = new Random((long)(pixelCenterX*pixelWidth+pixelCenterY*pixelHeight));
	}

	public JitteredSampleFactory(double pixelCenterX, double pixelCenterY,
			double pixelWidth, double pixelHeight, int nbOfSubCellsPerDimension) {
		this(pixelCenterX, pixelCenterY, pixelWidth, pixelHeight, nbOfSubCellsPerDimension, nbOfSubCellsPerDimension);
	}
	
	public JitteredSampleFactory(double pixelCenterX, double pixelCenterY,
			double pixelSideLength, int nbOfSubCellsPerDimension) {
		this(pixelCenterX, pixelCenterY, pixelSideLength, pixelSideLength, nbOfSubCellsPerDimension);
	}
	
	public JitteredSampleFactory(double pixelSideLength, int nbOfSubCellsPerDimension) {
		this(0, 0, pixelSideLength, pixelSideLength, nbOfSubCellsPerDimension);
	}
	
	
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
		if(currentSubcellCenterX > rightPixelBorder){
			currentSubcellCenterX = initialSubcellCenterX;
			currentSubcellCenterY = currentSubcellCenterY + subcellHeight;
			if (currentSubcellCenterY > upperPixelBorder) {
				currentSubcellCenterY = initialSubcellCenterY;
			}
		}
	}
	
	public void reset(double pixelCenterX, double pixelCenterY){
		super.reset(pixelCenterX, pixelCenterY);
		rightPixelBorder = pixelCenterX + getPixelWidth()/2;
		upperPixelBorder = pixelCenterY + getPixelHeight()/2;
		
		initialSubcellCenterX = pixelCenterX - getPixelWidth()/2 + subcellWidth/2;
		initialSubcellCenterY = pixelCenterY - getPixelHeight()/2 + subcellHeight/2;
		
		resetCurrentSubcell();
	}
	
	
	public void resetCurrentSubcell(){
		this.currentSubcellCenterX = this.initialSubcellCenterX;
		this.currentSubcellCenterY = this.initialSubcellCenterY;
	}
	
	
}
