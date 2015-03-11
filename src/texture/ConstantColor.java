package texture;

import util.RGBColor;
import util.ShadeRec;

public class ConstantColor implements Texture {

	private RGBColor color;

	public ConstantColor(RGBColor color){
		if(color == null){
			throw new IllegalArgumentException("the given color is null");
		}
		setColor(color);
	}
	
	public ConstantColor() {
		setColor(new RGBColor(0));
	}
	
	@Override
	public RGBColor getColor(ShadeRec sr) {
		return color;
	}

	public void setColor(RGBColor color){
		this.color = color;
	}
}
