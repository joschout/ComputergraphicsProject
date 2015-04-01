package util;

import java.awt.Color;

public class RGBColor {
	
	public static double GAMMA = 2.2;
	
	public static RGBColor convertToRGBColor(Color color){
		return new RGBColor(color.getRGBColorComponents(null)[0], 
				color.getRGBColorComponents(null)[1],
				color.getRGBColorComponents(null)[2]);
	}
	
	private float R;
	private float G;
	private float B;
	
	public RGBColor(float R, float G, float B){
		if(R < 0 || R > 1){
			throw new IllegalArgumentException("the value of R should be between 0 and 1.0");
		}
		this.R=R;
		
		if(G < 0 || G > 1){
			throw new IllegalArgumentException("the value of R should be between 0 and 1.0");
		}
		this.G=G;
		
		if(B < 0 || B > 1){
			throw new IllegalArgumentException("the value of R should be between 0 and 1.0");
		}
		this.B=B;
	}
	
	public RGBColor(float i){
		this(i,i,i);
	}
	
	public RGBColor(RGBColor color){
		this(color.R, color.G, color.B);
	}
	
	public float R(){
		return R;
	}
	
	public float G(){
		return G;
	}
	
	public float B(){
		return B;
	}
	
	public RGBColor add(RGBColor rgbColor){
	
		float tempR = (float)(this.R + rgbColor.R);
		float tempG= (float)(this.G + rgbColor.G);
		float tempB = (float)(this.B + rgbColor.B);
		return format( tempR, tempG, tempB);
	}

	public RGBColor unboundedAdd(RGBColor rgbColor){
		
		float tempR = (float)(this.R + rgbColor.R);
		float tempG= (float)(this.G + rgbColor.G);
		float tempB = (float)(this.B + rgbColor.B);
		RGBColor color = new RGBColor(0);
		color.R = tempR;
		color.G = tempG;
		color.B = tempB;
		return color;
	}
	
	public RGBColor multiply(RGBColor rgbColor){
		float tempR = (float)(this.R * rgbColor.R);
		float tempG= (float)(this.G * rgbColor.G);
		float tempB = (float)(this.B * rgbColor.B);
		return format( tempR, tempG, tempB);
	}

//	public RGBColor format(RGBColor color){
//		return color;
//	}
	
	public RGBColor format(float R, float G, float B){
		return clamp( R, G,  B);
	}
	
//	public RGBColor format(){
//		return this;
//	}
	
	
	public RGBColor power(double power){
		float tempR = (float) Math.pow(this.R, power);
		float tempG = (float) Math.pow(this.G, power);
		float tempB = (float) Math.pow(this.B, power);
		return format( tempR, tempG, tempB);
	}
	
	
	public RGBColor gammaCorrect(){
		return power(1.0/GAMMA);
	}
	
	public RGBColor scale(double s){
		float tempR = (float) s*R;
		float tempG = (float) s*G;
		float tempB = (float) s*B;
		return format( tempR, tempG, tempB);
	}
	
	public Color convertToColor(){
		return new Color(R,G,B);
	}

	public RGBColor clamp(RGBColor color){
		float maxValue = Math.max(color.R(), Math.max(color.G(), color.B()));
		
		if(maxValue > 1.0){
			return color.scale(1.0/maxValue);
		}
		return color;
	}
	
	public static RGBColor clamp(float r, float g, float b){
		float maxValue = Math.max(r, Math.max(g, b));
		
		if(maxValue > 1.0){
			return new RGBColor(r/maxValue,g/maxValue,b/maxValue);
		}
		return new RGBColor(r,g,b);
	}
	
	public RGBColor clampToRed(){
		if(this.R > 1.0 || this.G > 1.0 || this.B > 1.0){
			return new RGBColor((float)1, (float)0, (float)0);
		}
		return this;
	}
	
//	public double toneMap(double i){
//		return (i/(i+1));
//	}
//	
	
	
	public String toString(){
		return "R:" + Float.toString(R) + " , G:" + Float.toString(G) + " , B:" + Float.toString(B); 
	}
}
