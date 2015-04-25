package texture;

import util.RGBColor;
import util.ShadeRec;

public class Checkers3DTexture implements Texture {
	
	
	private static double eps = -0.000187453738;
	
	private RGBColor color1;
	private RGBColor color2;
	private double squareSize;
	
	public Checkers3DTexture() {
		this.color1 = RGBColor.BLACK;
		this.color2 = RGBColor.WHITE;
		this.squareSize = 1;
	}

	public Checkers3DTexture(RGBColor color1, RGBColor color2, double squareSize) {
		super();
		this.color1 = color1;
		this.color2 = color2;
		this.squareSize = squareSize;
	}

	@Override
	public RGBColor getColor(ShadeRec sr) {


		double x = sr.localHitPoint.x + eps;
		double y = sr.localHitPoint.y + eps;
		double z = sr.localHitPoint.z + eps;
		
		int sum = (int)Math.floor(x/squareSize)
				+ (int)Math.floor(y/squareSize)
				+ (int)Math.floor(z/squareSize);
		
		
		if (sum %2 == 0 ) {
			return this.color1;
		}else {
			return this.color2;
		}
		
	}


	public RGBColor getColor1() {
		return color1;
	}


	public void setColor1(RGBColor color1) {
		this.color1 = color1;
	}


	public RGBColor getColor2() {
		return color2;
	}


	public void setColor2(RGBColor color2) {
		this.color2 = color2;
	}

	public double getSquareSize() {
		return squareSize;
	}

	public void setSquareSize(double squareSize) {
		this.squareSize = squareSize;
	}

}
