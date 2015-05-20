package material;

import brdf.GlossySpecularBRDF;
import brdf.PerfectSpecularBRDF;
import math.OrthonormalBasis;
import math.Point;
import math.Ray;
import math.Vector;
import sampling.HemisphereSampleFactory;
import sampling.JitteredSampleFactory;
import sampling.RandomSampleFactory;
import sampling.Sample;
import util.RGBColor;
import util.ShadeRec;

public class GlossyReflectionMaterial extends PhongMaterial{

	private GlossySpecularBRDF glossySpecularBRDF;
//	private HemisphereSampleFactory hemisphereSampleFactory;
	private int nbOfHemisphereSamples;

	public GlossyReflectionMaterial() {
		super();
		this.glossySpecularBRDF = new GlossySpecularBRDF();
//		this.hemisphereSampleFactory 
//		= new HemisphereSampleFactory(new Vector(1, 0, 0),
//				1);
		setExponent(1);
		this.nbOfHemisphereSamples = 1;
	}



	@Override
	public RGBColor shade(ShadeRec sr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RGBColor shade2(ShadeRec sr) {
		//		System.out.println("#########################################");
		//		System.out.println("############  SHADE ENTERED    ##########");
		//		System.out.println("#########################################");
		RGBColor L = super.shade2(sr);

		Vector wo = sr.ray.direction.scale(-1);
		double ndotwo = sr.normal.dot(wo);
		//		System.out.println("wo: " + wo.toString());
		//		System.out.println("ndotwo: " + ndotwo );
		//direction of mirror reflection
		Vector r = wo.scale(-1).add(sr.normal.scale(2.0*ndotwo)).normalize();
		//		System.out.println("r: " + r.toString());
//		hemisphereSampleFactory.reset(r);
		//		if (r.compareTo(hemisphereSampleFactory.localOrthonormalbasis.w) != 0) {
		//			System.out.println("r and w are NOT equal");
		//			System.out.println("r: " + r.toString());
		//			System.out.println("w: " + hemisphereSampleFactory.localOrthonormalbasis.w.toString());
		//		}


		//		r = hemisphereSampleFactory.localOrthonormalbasis.w;
		RGBColor temp = RGBColor.BLACK;

		HemisphereSampleFactory hemisphereSampleFactory= new HemisphereSampleFactory(r,
				getExponent());
		
		
		for (int i = 0; i < nbOfHemisphereSamples; i++) {
			//direction of reflected ray	
			Vector wi = hemisphereSampleFactory.getNextSample(sr.normal);

			double rDotWi = r.dot(wi);

			double phongLobe = Math.pow(rDotWi, this.getExponent());
			if (phongLobe == 0.0) {
				System.out.println("phongLobe is 0.0");
				System.out.println("rDotWi is " + rDotWi);
				System.out.println("exponent is " + this.getExponent());
			}

			RGBColor reflectiveBRDF = glossySpecularBRDF.sampleF(phongLobe);
			double pdf = phongLobe * sr.normal.dot(wi);		
			Ray reflectedRay = new Ray(sr.hitPoint, wi, sr.depth + 1);


			/**
			 * de fout zit hier : 0.0 .scale ( Infinity)
			 * 					want pdf = 0.0
			 * pdf = phongLobe * sr.normal.dot(wi);
			 * 			en phonglobe = 0.0
			 * phongLobe = Math.pow(r.dot(wi), glossySpecularBRDF.getPhongExponent());
			 * 				
			 */
			//				System.out.println("sr.normal.dot(wi): " + sr.normal.dot(wi));
			//				System.out.println("pdf: "+pdf);
			//				System.out.println("phonglobe: " + phongLobe);
			//				System.out.println("r.dot(wi): " +r.dot(wi));

			RGBColor recursiveColor = sr.tracer.traceRay(reflectedRay);
			RGBColor brdfTimesRecursiveColor = reflectiveBRDF.multiply(recursiveColor);
			double scalingFactor = sr.normal.dot(wi)/pdf;
			if (Double.isInfinite(scalingFactor)) {
				System.out.println("temp: " + temp.toString());
				System.out.println("recursiveColor: " + recursiveColor.toString());
				System.out.println("brdfTimesRecursiveColor: " + brdfTimesRecursiveColor);
				System.out.println("scalingFactor: " + scalingFactor);
			}


			RGBColor completeFactorToAdd = brdfTimesRecursiveColor.scale(scalingFactor);

			temp = temp.unboundedAdd(completeFactorToAdd);
			//				temp = temp.unboundedAdd(
			//						reflectiveBRDF.multiply(sr.tracer.traceRay(reflectedRay))
			//						.scale(sr.normal.dot(wi)/pdf)
			//						);
			//		}

		}
		RGBColor temp2 = temp.scale(1.0/nbOfHemisphereSamples);

		return L = L.add(temp2);
	}

	//	@Override
	//	public RGBColor totalBRDF(ShadeRec sr, Vector wo, Vector wi) {
	//			
	//	}

	public void setKr(double kr){
		glossySpecularBRDF.setKs(kr);
	}

	public void setExponent(double exponent){
		glossySpecularBRDF.setPhongExponent(exponent);
//		hemisphereSampleFactory.setExponent(exponent);
	}

	public double getExponent(){
		return glossySpecularBRDF.getPhongExponent();
	}


	public void setCr(RGBColor color){
		this.glossySpecularBRDF.setColor(color);
	}



	public int getNbOfHemisphereSamples() {
		return nbOfHemisphereSamples;
	}



	public void setNbOfHemisphereSamples(int nbOfHemisphereSamples) {
		this.nbOfHemisphereSamples = nbOfHemisphereSamples;
	}

}
