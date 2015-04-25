package texture;

import util.RGBColor;
import util.ShadeRec;

public class ConstantColorTexture implements Texture {

	private RGBColor color;

	public ConstantColorTexture(RGBColor color){
		if(color == null){
			throw new IllegalArgumentException("the given color is null");
		}
		setColor(color);
	}
	
	public ConstantColorTexture() {
		setColor(RGBColor.BLACK);
	}
	
	@Override
	public RGBColor getColor(ShadeRec sr) {
		return color;
	}

	public void setColor(RGBColor color){
		this.color = color;
	}
}
