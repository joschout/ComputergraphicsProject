package material;

import brdf.GlossySpecularBRDF;
import math.Vector;
import sampling.HemisphereSampleFactory;
import util.RGBColor;
import util.ShadeRec;

public class GlossyReflectionMaterial extends PhongMaterial{
	
	public GlossySpecularBRDF glossySpecularBRDF;
	public HemisphereSampleFactory hemisphereSampleFactory;
	

	@Override
	public RGBColor shade(ShadeRec sr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RGBColor shade2(ShadeRec sr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RGBColor totalBRDF(ShadeRec sr, Vector wo, Vector wi) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setKr(double kr){
		glossySpecularBRDF.setKs(kr);
	}

	public void setExponent(double exponent){
		glossySpecularBRDF.setPhongExponent(exponent);
	}
	

}
