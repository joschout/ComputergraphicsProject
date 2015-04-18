package sampling;

public abstract class SquareSampleFactory  implements SampleFactory{
	
	// the horizontal center of the pixel in the image space
	private double pixelCenterX;
	// the verticall center of the pixel in the image space
	private double pixelCenterY;
	
	// the width of the pixel in the image space
	private double pixelWidth;
	//the height of the pixel in the image space
	private double pixelHeight;
	
	public SquareSampleFactory(double pixelCenterX, double pixelCenterY,
			double pixelWidth, double pixelHeight) {
		
		super();
		this.pixelCenterX = pixelCenterX;
		this.pixelCenterY = pixelCenterY;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
	}
	
	public SquareSampleFactory(double pixelCenterX, double pixelCenterY) {
		this(pixelCenterX, pixelCenterY, 1, 1);
	}

	public double getPixelCenterX() {
		return pixelCenterX;
	}

	public void setPixelCenterX(double pixelCenterX) {
		this.pixelCenterX = pixelCenterX;
	}

	public double getPixelCenterY() {
		return pixelCenterY;
	}

	public void setPixelCenterY(double pixelCenterY) {
		this.pixelCenterY = pixelCenterY;
	}

	public double getPixelWidth() {
		return pixelWidth;
	}

	public void setPixelWidth(double pixelWidth) {
		this.pixelWidth = pixelWidth;
	}

	public double getPixelHeight() {
		return pixelHeight;
	}

	public void setPixelHeight(double pixelHeight) {
		this.pixelHeight = pixelHeight;
	}
	
	public void reset(double pixelCenterX, double pixelCenterY){
		this.pixelCenterX = pixelCenterX;
		this.pixelCenterY = pixelCenterY;
	}
	
}
