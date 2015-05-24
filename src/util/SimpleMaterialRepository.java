package util;

import java.awt.Color;

import texture.ConstantColorTexture;
import texture.ImageTexture;
import mapping.LightProbeMapping;
import material.DielectricMaterial;
import material.GlossyReflectionMaterial;
import material.MatteMaterial;
import material.PhongMaterial;
import material.ReflectiveMaterial;
import material.SVEmissiveMaterial;
import material.SimpleTransparentMaterial;

public class SimpleMaterialRepository {
	
	public PhongMaterial getPhongmaterialGreen(){
		PhongMaterial phong = new PhongMaterial();
		phong.setKa(0.25);
		phong.setKd(0.65);
		phong.setCd(RGBColor.convertToRGBColor(Color.GREEN));
		phong.setKs(0.2);
		phong.setPhongExponent(10);
		phong.setCs(RGBColor.convertToRGBColor(Color.WHITE));
		return phong;
	}
	
	public MatteMaterial getMatteMaterialCyan(){
		MatteMaterial matte = new MatteMaterial();
		matte.setKa(0.25);
		matte.setKd(0.65);
		matte.setCd(RGBColor.convertToRGBColor(Color.CYAN));	
		return matte;
	}
	
	public ReflectiveMaterial getReflectiveMaterial1(){
		ReflectiveMaterial reflectiveMaterial1 = new ReflectiveMaterial();
		reflectiveMaterial1.setKa(0.25);
		reflectiveMaterial1.setKd(0.65);
		reflectiveMaterial1.setCd(new RGBColor(0.75f, 0.75f, 0f));
		reflectiveMaterial1.setCs(RGBColor.WHITE);
		reflectiveMaterial1.setKs(0.75);
		reflectiveMaterial1.setPhongExponent(100);
		reflectiveMaterial1.setCr(RGBColor.WHITE);
		reflectiveMaterial1.setKr(0.75);
		return reflectiveMaterial1;
	}
	
	public ReflectiveMaterial getReflectiveMaterial2(){
		ReflectiveMaterial reflectiveMaterial2 = new ReflectiveMaterial();
		reflectiveMaterial2.setKa(0.25);
		reflectiveMaterial2.setKd(0.65);
		reflectiveMaterial2.setCd(new RGBColor(0f, 0f, 1f));
		reflectiveMaterial2.setCs(RGBColor.WHITE);
		reflectiveMaterial2.setKs(0.75);
		reflectiveMaterial2.setPhongExponent(100);
		reflectiveMaterial2.setCr(RGBColor.WHITE);
		reflectiveMaterial2.setKr(0.75);
		return reflectiveMaterial2;
	}
	
	public GlossyReflectionMaterial getGlossyMaterial(){
		GlossyReflectionMaterial glossyMaterial = new GlossyReflectionMaterial();
		glossyMaterial.setKa(0.0);
		glossyMaterial.setKd(0.0);
		glossyMaterial.setCd(RGBColor.WHITE);
		glossyMaterial.setKs(0.0);
		glossyMaterial.setCs(RGBColor.WHITE);
		glossyMaterial.setKr(0.9);
		glossyMaterial.setCr(new RGBColor(0f, 0f, 1f));
		glossyMaterial.setExponent(10000);
		glossyMaterial.setNbOfHemisphereSamples(10);
		return glossyMaterial;
	}
	
	public SVEmissiveMaterial getSVEmissiveMaterial_bigSphere(){
		SVEmissiveMaterial emissiveMat = new SVEmissiveMaterial();
		emissiveMat.setLs(1e1);
		LightProbeMapping lightProbeMapping = new LightProbeMapping();
		ImageTexture sphereImTex = new ImageTexture("angmap24Small.jpg", lightProbeMapping);;
		ConstantColorTexture colorText = new ConstantColorTexture();
		colorText.setColor(new RGBColor(1f,0f, 0f));
		emissiveMat.setCe(sphereImTex);	
		return emissiveMat;
	}
	
	public SimpleTransparentMaterial getSimpleTransparentMaterial1(){
		SimpleTransparentMaterial simpleTransparentMaterial1 = new SimpleTransparentMaterial();
//			simpleTransparentMaterial1.setKa(0.25);
//			simpleTransparentMaterial1.setKd(0.65);
//			simpleTransparentMaterial1.setCd(new RGBColor(0f, 0f, 1f));
//			simpleTransparentMaterial1.setCs(RGBColor.WHITE);
		simpleTransparentMaterial1.setKs(0.5);
		simpleTransparentMaterial1.setPhongExponent(2000);
//			simpleTransparentMaterial1.setCr(RGBColor.WHITE);
		simpleTransparentMaterial1.setKr(0.1);
		simpleTransparentMaterial1.setKt(0.9);
		simpleTransparentMaterial1.setAbsoluteIndexOfRefraction(0.75);
		return simpleTransparentMaterial1;	
	}
	
	public DielectricMaterial getDielectricMaterial1(){
		DielectricMaterial dielectricMaterial = new DielectricMaterial();
		dielectricMaterial.setKs(0.2);
		dielectricMaterial.setPhongExponent(2000);
		dielectricMaterial.setEtaIncoming(1.5);
		dielectricMaterial.setEtaOutgoing(1.0);
		dielectricMaterial.setCf_in(new RGBColor(0.65f, 0.45f, 0));
		dielectricMaterial.setCf_out(new RGBColor(0f, 0.5f, 0.5f));
		return dielectricMaterial;
	}
	
	
}
