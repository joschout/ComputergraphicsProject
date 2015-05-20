package util;

import java.awt.Color;

public class RGBColor {
	
	public static double GAMMA = 2.2;
	
	public static final RGBColor BLACK = new RGBColor(0);
	public static final RGBColor WHITE = new RGBColor(1);
	
	public static RGBColor convertToRGBColor(Color color){
		return new RGBColor(color.getRGBColorComponents(null)[0], 
				color.getRGBColorComponents(null)[1],
				color.getRGBColorComponents(null)[2]);
	}
	
	private float R;
	private float G;
	private float B;
	
	public RGBColor(float R, float G, float B){
		
		if (Float.isNaN(R)) {
			throw new IllegalArgumentException("the given value for R is NaN");
		}
		if(R < 0 || R > 1){
			throw new IllegalArgumentException("the value of R should be between 0 and 1.0");
		}
		this.R=R;
		
		if (Float.isNaN(G)) {
			throw new IllegalArgumentException("the given value for G is NaN");
		}
		if(G < 0 || G > 1){
			throw new IllegalArgumentException("the value of R should be between 0 and 1.0");
		}
		this.G=G;
		
		if (Float.isNaN(B)) {
			throw new IllegalArgumentException("the given value for B is NaN");
		}
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
		double sTimesR = s*R;
		double sTimesG = s*G;
		double sTimesB = s*B;
		
		float tempR = (float) sTimesR;
		float tempG = (float) sTimesG;
		float tempB = (float) sTimesB;
		
//		float tempR = (float) s*R;
//		float tempG = (float) s*G;
//		float tempB = (float) s*B;
		if (Float.isNaN(tempR)) {
			System.out.println("R_Out is NaN");
			System.out.println("R_Out is R_In * s");
			System.out.println("R_In is " + R);
//			System.out.println("double R_In is" + (double) R);
			System.out.println("s is " + s);
//			System.out.println("R*s is " + s*R);
			throw new IllegalArgumentException("nan encountered");
			
		}
		if (Float.isNaN(tempG)) {
			System.out.println("tempG is NaN");
			System.out.println("G is " + G);
//			System.out.println("double G is " + (double) G);
			System.out.println("s is " + s);
//			System.out.println("uncasted G*s is " + s*G);
		}
		if (Float.isNaN(tempB)) {
			System.out.println("tempB is NaN");
			System.out.println("B is " + B);
//			System.out.println("double B is " + (double) B);
			System.out.println("s is " + s);
//			System.out.println("uncasted B*s is " + s*B);
		}
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
//			System.out.println("r/maxValue " + r/maxValue);
//			System.out.println("g/maxValue " + g/maxValue);
//			System.out.println("b/maxValue " + b/maxValue);
			return new RGBColor(r/maxValue,g/maxValue,b/maxValue);
		}
		
		if (!(r<=1 && r>=0 )) {
			System.out.println("R " + r);
		}
		if (!(g<=1 && g>=0 )) {
			System.out.println("G " + g);
		}
		if (!(b<=1 && b>=0 )) {
			System.out.println("B " + b);
		}
//		System.out.println("R " + r);
//		System.out.println("G " + g);
//		System.out.println("B " + b);
		
		
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
