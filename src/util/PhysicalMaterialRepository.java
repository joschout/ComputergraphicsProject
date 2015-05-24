package util;

import material.PhysicalMaterial;
import brdf.BlinnPhongBRDF;
import brdf.CookTorranceBRDF;
import brdf.LafortuneBRDF;
import brdf.WardBRDF;

public class PhysicalMaterialRepository {
	
	public PhysicalMaterial getAcrylicBlue_BlinnPhongMaterial(){	
		BlinnPhongBRDF acrylicBlue_BlinnPhongBRDF = new BlinnPhongBRDF(1.37e4, new RGBColor(0.0016f ,0.00115f ,0.000709f));
		PhysicalMaterial acrylicBlue_BlinnPhongMaterial = new PhysicalMaterial(acrylicBlue_BlinnPhongBRDF);
		acrylicBlue_BlinnPhongMaterial.setCd(new RGBColor(0.0147f, 0.0332f, 0.064f));
		acrylicBlue_BlinnPhongMaterial.setKa(0.25);
		acrylicBlue_BlinnPhongMaterial.setKd(0.65);
		acrylicBlue_BlinnPhongBRDF.setKs(0.2);
		return acrylicBlue_BlinnPhongMaterial;		
	}

	public PhysicalMaterial getAcrylicBlue_CookTorranceMaterial(){
		CookTorranceBRDF acrylicBlue_CookTorranceBRDF = new CookTorranceBRDF(0.117, 0.0137, new RGBColor(0.0291f, 0.0193f, 0.0118f));
		PhysicalMaterial acrylicBlue_CookTorranceMaterial = new PhysicalMaterial(acrylicBlue_CookTorranceBRDF);
		acrylicBlue_CookTorranceMaterial.setCd(new RGBColor(0.0143f,  0.033f,  0.0639f));
		acrylicBlue_CookTorranceMaterial.setKa(0.25);
		acrylicBlue_CookTorranceMaterial.setKd(0.65);
		acrylicBlue_CookTorranceBRDF.setKs(0.2);
		return acrylicBlue_CookTorranceMaterial;
	}
	
	public PhysicalMaterial getAcrylicBlue_LafortuneMaterial(){
		LafortuneBRDF acrylicBlue_LafortuneBRDF = new LafortuneBRDF(-0.577, 0.577, 4.06e3, new RGBColor(0.0238f, 0.0164f, 0.01f));
		PhysicalMaterial acrylicBlue_LafortuneMaterial = new PhysicalMaterial(acrylicBlue_LafortuneBRDF);
		acrylicBlue_LafortuneMaterial.setCd(new RGBColor(0.0145f, 0.0332f, 0.064f));
		acrylicBlue_LafortuneMaterial.setKa(0.25);
		acrylicBlue_LafortuneMaterial.setKd(0.65);
		acrylicBlue_LafortuneBRDF.setKs(0.2);
		return acrylicBlue_LafortuneMaterial;
	}
	
	public PhysicalMaterial getAcrylicBlue_WardMaterial(){
		WardBRDF acrylicBlue_WardBRDF = new WardBRDF(0.0162, new RGBColor(0.00774f, 0.00547f, 0.00339f));
		PhysicalMaterial acrylicBlue_WardMaterial = new PhysicalMaterial(acrylicBlue_WardBRDF);
		acrylicBlue_WardMaterial.setCd(new RGBColor(0.0138f,  0.0326f,  0.0636f));
		acrylicBlue_WardMaterial.setKa(0.25);
		acrylicBlue_WardMaterial.setKd(0.650);
		acrylicBlue_WardBRDF.setKs(0.2);
		return acrylicBlue_WardMaterial;
	}
	
	public PhysicalMaterial getAluminium_CookTorranceMaterial(){
		CookTorranceBRDF aluminium_CookTorranceBRDF = new CookTorranceBRDF(0.59, 0.00776, new RGBColor(0.0799f, 0.06f, 0.0294f));
		PhysicalMaterial aluminium_CookTorranceMaterial = new PhysicalMaterial(aluminium_CookTorranceBRDF);
		aluminium_CookTorranceMaterial.setCd(new RGBColor(0.0418f, 0.0356f, 0.0273f));
		aluminium_CookTorranceMaterial.setKa(0.25);
		aluminium_CookTorranceMaterial.setKd(0.65);
		aluminium_CookTorranceBRDF.setKs(0.2);
		return aluminium_CookTorranceMaterial;
	}
	
	
	
}
