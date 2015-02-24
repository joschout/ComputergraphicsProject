package material;

import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;
import brdf.LambertianBRDF;

public class SpecialMatteMaterial extends Material{

	private LambertianBRDF ambientBRDF;
	private LambertianBRDF diffuseBRDF;
	
	public SpecialMatteMaterial(){
		ambientBRDF = new LambertianBRDF();
		diffuseBRDF = new LambertianBRDF();
	}
	
	
	public void setKa(double ka){
		ambientBRDF.setKd(ka);
	}
	
	public void setKd(double kd){
		diffuseBRDF.setKd(kd);
	}
	
	public void setCd(RGBColor color){
		ambientBRDF.setColor(color);
		diffuseBRDF.setColor(color);
	}
	
	public RGBColor shade(ShadeRec sr){
		
		//hb p 271
		Vector wo = sr.ray.direction.scale(-1);
		//RGBColor L = ambientBRDF.getReflectance(sr, wo).multiply(sr.world.ambientLight.getRadiance(sr));
		RGBColor L = new RGBColor(0,0,1); //blue
		int nbLights = sr.world.lights.size();
		
//		
//		boolean inShadowOf1 = false;
//		boolean inShadowOf2 = false;
		
		for(int j = 0; j< nbLights; j++){
			Vector wi = sr.world.lights.get(j).getDirectionOfIncomingLight(sr);
			double ndotwi = sr.normal.dot(wi);
		
			if(ndotwi > 0.0){	
				boolean inShadow = false;
				if(sr.world.lights.get(j).castShadows()){
					Ray shadowRay = new Ray(sr.hitPoint, wi);
					inShadow = sr.world.lights.get(j).inShadow(shadowRay, sr);
				}
				if(! inShadow){
					//L = L.add(diffuseBRDF.f(sr, wo, wi).multiply(sr.world.lights.get(j).getRadiance(sr).scale(ndotwi)));
					//L = new RGBColor(0,0,1); //blue
				}
				if(inShadow){
					if(j ==0){
						L = L.add(new RGBColor(1,0,0));
					}
					if(j == 1){
						L = L.add(new RGBColor(0,1,0));
					}
				}
			}
		}
		
//		if(inShadowOf1 && inShadowOf2){
//			L = new RGBColor(1, 1, 0); // yellow
//		}
//		if(inShadowOf1 && !inShadowOf2){
//			L = new RGBColor(0, 1, 0); //green
//		}
//		if(!inShadowOf1 && inShadowOf2){
//			L =  new RGBColor(1, 0, 0); //red
//		}
//		if(!inShadowOf1 && !inShadowOf2){
//			L =  new RGBColor(0, 0,1); //red
//		}
		
		
//		if(inShadowOf1 && inShadowOf2){
//			L = L.add(new RGBColor(1, 1, 0));// add yellow
//		}
//		if(inShadowOf1 && !inShadowOf2){
//			L = L.add(new RGBColor(0, 1, 0)); // add green
//		}
//		if(!inShadowOf1 && inShadowOf2){
//			L =  L.add(new RGBColor(1, 0, 0)); //add red
//		}
		
		//pink = red + blue --> only in shadow of pointlight 2
		//cyan = green + blue --> only in shadow of pointlight 1
		
		
		return L;
	}
}
