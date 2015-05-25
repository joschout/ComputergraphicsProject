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
		acrylicBlue_BlinnPhongBRDF.setKs(0.9);
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
	
	
	public PhysicalMaterial getDarkRedPaint_BlinnPhongMaterial(){	
		BlinnPhongBRDF darkRedPaint_BlinnPhongBRDF = new BlinnPhongBRDF(34.1, new RGBColor(0.00473f ,0.00271f ,0.00194f));
		PhysicalMaterial darkRedPaint_BlinnPhongMaterial = new PhysicalMaterial(darkRedPaint_BlinnPhongBRDF);
		darkRedPaint_BlinnPhongMaterial.setCd(new RGBColor(0.256f, 0.0341f, 0.0102f));
		darkRedPaint_BlinnPhongMaterial.setKa(0.25);
		darkRedPaint_BlinnPhongMaterial.setKd(0.65);
		darkRedPaint_BlinnPhongBRDF.setKs(0.2);
		return darkRedPaint_BlinnPhongMaterial;		
	}

	public PhysicalMaterial getDarkRedPaint_CookTorranceMaterial(){
		CookTorranceBRDF darkRedPaint_CookTorranceBRDF = new CookTorranceBRDF(0.0405, 0.329, new RGBColor(0.185f, 0.138f, 0.09f));
		PhysicalMaterial darkRedPaint_CookTorranceMaterial = new PhysicalMaterial(darkRedPaint_CookTorranceBRDF);
		darkRedPaint_CookTorranceMaterial.setCd(new RGBColor(0.253f,  0.0308f,  0.00838f));
		darkRedPaint_CookTorranceMaterial.setKa(0.25);
		darkRedPaint_CookTorranceMaterial.setKd(0.65);
		darkRedPaint_CookTorranceBRDF.setKs(0.2);
		return darkRedPaint_CookTorranceMaterial;
	}
	
	public PhysicalMaterial getDarkRedPaint_LafortuneMaterial(){
		LafortuneBRDF darkRedPaint_LafortuneBRDF = new LafortuneBRDF(-0.656, 0.372, 24.8, new RGBColor(0.223f, 0.169f, 0.109f));
		PhysicalMaterial darkRedPaint_LafortuneMaterial = new PhysicalMaterial(darkRedPaint_LafortuneBRDF);
		darkRedPaint_LafortuneMaterial.setCd(new RGBColor(0.26f, 0.0361f, 0.0118f));
		darkRedPaint_LafortuneMaterial.setKa(0.25);
		darkRedPaint_LafortuneMaterial.setKd(0.65);
		darkRedPaint_LafortuneBRDF.setKs(0.2);
		return darkRedPaint_LafortuneMaterial;
	}
	
	public PhysicalMaterial getDarkRedPaint_WardMaterial(){
		WardBRDF darkRedPaint_WardBRDF = new WardBRDF(0.419, new RGBColor(0.0477f, 0.0312f, 0.0221f));
		PhysicalMaterial darkRedPaint_WardMaterial = new PhysicalMaterial(darkRedPaint_WardBRDF);
		darkRedPaint_WardMaterial.setCd(new RGBColor(0.242f,  0.0244f,  0.00338f));
		darkRedPaint_WardMaterial.setKa(0.25);
		darkRedPaint_WardMaterial.setKd(0.650);
		darkRedPaint_WardBRDF.setKs(0.2);
		return darkRedPaint_WardMaterial;
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
