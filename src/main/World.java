package main;

import ioPackage.ObjectFileReader2;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import boundingVolumeHierarchy.BVHManager2;
import brdf.BRDF;
import brdf.BlinnPhongBRDF;
import brdf.CookTorranceBRDF;
import brdf.LafortuneBRDF;
import brdf.WardBRDF;
import camera.Camera;
import camera.PerspectiveCamera;
import camera.ThinLensCamera;
import sampling.JitteredSampleFactory;
import shape.Box;
import shape.CompoundObject;
import shape.Cylinder;
import shape.Disk;
import shape.Intersectable;
import shape.Parallelogram;
import shape.Plane;
import shape.Shape;
import shape.Sphere;
import shape.Square;
import shape.Triangle;
import texture.Checkers3DTexture;
import texture.ConstantColorTexture;
import texture.ImageTexture;
import texture.NormalMapTexture;
import texture.Texture;
import light.AmbientLight;
import light.AreaLight;
import light.EnvironmentLight;
import light.Light;
import light.PointLight;
import mapping.CylindricalMapping;
import mapping.LightProbeMapping;
import mapping.Mapping;
import mapping.SphericalMapping;
import material.DielectricMaterial;
import material.EmissiveMaterial;
import material.GlossyReflectionMaterial;
import material.Material;
import material.MatteMaterial;
import material.PhongMaterial;
import material.PhysicalMaterial;
import material.ReflectiveMaterial;
import material.SVEmissiveMaterial;
import material.SVMatteMaterial;
import material.SimpleTransparentMaterial;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class World{
	
	public Camera camera;
	public RGBColor backgroundColor;
	
	/**
	 * Deze lijst wordt geintersect
	 */
	public List<Intersectable> intersectablesToIntersect = new ArrayList<Intersectable>();
	
	/**
	 * hierin worden alle objecten gedumpt in het begin
	 */
	public List<Intersectable> intersectables = new ArrayList<Intersectable>();	
	public List<Light> lights = new ArrayList<Light>();
	public Light ambientLight;
	public int maxBVHCounter;
	private int maxRecursionDepth;
		
	public World(){
		camera = null;
		backgroundColor = new RGBColor((float)0.3);
		ambientLight = new AmbientLight();
		maxBVHCounter = 0;
		maxRecursionDepth = 0;
	}
	
	public int getMaxRecursionDepth(){
		return this.maxRecursionDepth;
	}
	
	public void setMaxRecursionDepth(int maxRecursionDepth) throws IllegalArgumentException{
		if (maxRecursionDepth < 0) {
			throw new IllegalArgumentException("the given max recursion depth cannot be smaller than zero");
		}
		this.maxRecursionDepth = maxRecursionDepth;
	}
	
	public void addIntersectable(Intersectable intersectable){
		intersectables.add(intersectable);
	} 
	
	public void addLight(Light light){
		lights.add(light);
	}
	
	/**
	 * 
	 * @param imageWidth The number of pixels of the image in the horizontal direction
	 * @param imageHeight The number of pixels of the image in the vertical direction
	 */
	public  void build(int imageWidth, int imageHeight){
				//=== CAMERA ===///
				this.initializePerspectiveCamera(imageWidth, imageHeight);
//				this.initializeThinLensCamera(imageWidth, imageHeight);
				
				
				//=== LIGHT SOURCES ===//
				PointLight pl1 = new PointLight(3.0, RGBColor.WHITE, new  Point(-5,3,0));
				pl1.setCastShadows(true);
				this.addLight(pl1);

				PointLight pl2 = new PointLight(1.0, RGBColor.WHITE, new  Point(5,3,0));
				pl2.setCastShadows(true);
				this.addLight(pl2);

				//=== MATERIALS ===///
				PhongMaterial phong = new PhongMaterial();
				phong.setKa(0.25);
				phong.setKd(0.65);
				phong.setCd(RGBColor.convertToRGBColor(Color.GREEN));
				phong.setKs(0.2);
				phong.setPhongExponent(10);
				phong.setCs(RGBColor.convertToRGBColor(Color.WHITE));
				
				MatteMaterial matte = new MatteMaterial();
				matte.setKa(0.25);
				matte.setKd(0.65);
				matte.setCd(RGBColor.convertToRGBColor(Color.CYAN));				
				
				

				ReflectiveMaterial reflectiveMaterial1 = new ReflectiveMaterial();
				reflectiveMaterial1.setKa(0.25);
				reflectiveMaterial1.setKd(0.65);
				reflectiveMaterial1.setCd(new RGBColor(0.75f, 0.75f, 0f));
				reflectiveMaterial1.setCs(RGBColor.WHITE);
				reflectiveMaterial1.setKs(0.75);
				reflectiveMaterial1.setPhongExponent(100);
				reflectiveMaterial1.setCr(RGBColor.WHITE);
				reflectiveMaterial1.setKr(0.75);
				
				ReflectiveMaterial reflectiveMaterial2 = new ReflectiveMaterial();
				reflectiveMaterial2.setKa(0.25);
				reflectiveMaterial2.setKd(0.65);
				reflectiveMaterial2.setCd(new RGBColor(0f, 0f, 1f));
				reflectiveMaterial2.setCs(RGBColor.WHITE);
				reflectiveMaterial2.setKs(0.75);
				reflectiveMaterial2.setPhongExponent(100);
				reflectiveMaterial2.setCr(RGBColor.WHITE);
				reflectiveMaterial2.setKr(0.75);
				
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
					
				
				SVEmissiveMaterial emissiveMat = new SVEmissiveMaterial();
				emissiveMat.setLs(1e1);
				LightProbeMapping lightProbeMapping = new LightProbeMapping();
				ImageTexture sphereImTex = new ImageTexture("angmap24Small.jpg", lightProbeMapping);;
				ConstantColorTexture colorText = new ConstantColorTexture();
				colorText.setColor(new RGBColor(1f,0f, 0f));
				
				emissiveMat.setCe(sphereImTex);	
		
//				EnvironmentLight environmentLight = new EnvironmentLight();
//				environmentLight.nbOfShadowRaysPerEnvironmentLight = 100;
//				environmentLight.emissiveMaterial = emissiveMat;
//				environmentLight.setCastShadows(true);
//				lights.add(environmentLight);
//				
				Transformation bigSphereTrans = Transformation.createRotationY(0);
				bigSphereTrans = bigSphereTrans.append(Transformation.createRotationX(0));
				bigSphereTrans = bigSphereTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 0));
				Sphere bigSphere = new  Sphere(bigSphereTrans, 10);
				bigSphere.material = emissiveMat;
				bigSphere.infinite = true;
	//			intersectables.add(bigSphere);
				
				
				
				BlinnPhongBRDF acrylicBlue_BlinnPhongBRDF = new BlinnPhongBRDF(1.37e4, new RGBColor(0.0016f ,0.00115f ,0.000709f));
				PhysicalMaterial acrylicBlue_BlinnPhongMaterial = new PhysicalMaterial(acrylicBlue_BlinnPhongBRDF);
				acrylicBlue_BlinnPhongMaterial.setCd(new RGBColor(0.0147f, 0.0332f, 0.064f));
				acrylicBlue_BlinnPhongMaterial.setKa(0.25);
				acrylicBlue_BlinnPhongMaterial.setKd(0.65);
				acrylicBlue_BlinnPhongBRDF.setKs(0.2);
				
				
				CookTorranceBRDF acrylicBlue_CookTorranceBRDF = new CookTorranceBRDF(0.117, 0.0137, new RGBColor(0.0291f, 0.0193f, 0.0118f));
				PhysicalMaterial acrylicBlue_CookTorranceMaterial = new PhysicalMaterial(acrylicBlue_CookTorranceBRDF);
				acrylicBlue_CookTorranceMaterial.setCd(new RGBColor(0.0143f,  0.033f,  0.0639f));
				acrylicBlue_CookTorranceMaterial.setKa(0.25);
				acrylicBlue_CookTorranceMaterial.setKd(0.65);
				acrylicBlue_CookTorranceBRDF.setKs(0.2);
				
				
				CookTorranceBRDF aluminium_CookTorranceBRDF = new CookTorranceBRDF(0.59, 0.00776, new RGBColor(0.0799f, 0.06f, 0.0294f));
				PhysicalMaterial aluminium_CookTorranceMaterial = new PhysicalMaterial(aluminium_CookTorranceBRDF);
				aluminium_CookTorranceMaterial.setCd(new RGBColor(0.0418f, 0.0356f, 0.0273f));
				aluminium_CookTorranceMaterial.setKa(0.25);
				aluminium_CookTorranceMaterial.setKd(0.65);
				aluminium_CookTorranceBRDF.setKs(0.2);
				
				
				
				LafortuneBRDF acrylicBlue_LafortuneBRDF = new LafortuneBRDF(-0.577, 0.577, 4.06e3, new RGBColor(0.0238f, 0.0164f, 0.01f));
				PhysicalMaterial acrylicBlue_LafortuneMaterial = new PhysicalMaterial(acrylicBlue_LafortuneBRDF);
				acrylicBlue_LafortuneMaterial.setCd(new RGBColor(0.0145f, 0.0332f, 0.064f));
				acrylicBlue_LafortuneMaterial.setKa(0.25);
				acrylicBlue_LafortuneMaterial.setKd(0.65);
				acrylicBlue_LafortuneBRDF.setKs(0.2);
				
				WardBRDF acrylicBlue_WardBRDF = new WardBRDF(0.0162, new RGBColor(0.00774f, 0.00547f, 0.00339f));
				PhysicalMaterial acrylicBlue_WardMaterial = new PhysicalMaterial(acrylicBlue_WardBRDF);
				acrylicBlue_WardMaterial.setCd(new RGBColor(0.0138f,  0.0326f,  0.0636f));
				acrylicBlue_WardMaterial.setKa(0.25);
				acrylicBlue_WardMaterial.setKd(0.650);
				acrylicBlue_WardBRDF.setKs(0.2);
				
//				Transformation sphereTrans = Transformation.createRotationY(0);
//				sphereTrans = sphereTrans.append(Transformation.createRotationX(0));
//				sphereTrans = sphereTrans.appendToTheLeft(Transformation.createTranslation(0.25 ,0, 4));
//				Sphere sphere = new  Sphere(sphereTrans, 1);
//				sphere.material = reflectiveMaterial1;
//				intersectables.add(sphere);
//				
				
				
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
				
				
				DielectricMaterial dielectricMaterial = new DielectricMaterial();
				dielectricMaterial.setKs(0.2);
				dielectricMaterial.setPhongExponent(2000);
				dielectricMaterial.setEtaIncoming(1.5);
				dielectricMaterial.setEtaOutgoing(1.0);
				dielectricMaterial.setCf_in(new RGBColor(0.65f, 0.45f, 0));
				dielectricMaterial.setCf_out(new RGBColor(0f, 0.5f, 0.5f));
				
				Transformation sphereTrans2 = Transformation.createRotationY(0);
				sphereTrans2 = sphereTrans2.append(Transformation.createRotationX(0));
				sphereTrans2 = sphereTrans2.appendToTheLeft(Transformation.createTranslation(1 ,0, 3));
				Sphere sphere2 = new  Sphere(sphereTrans2, 1);
				sphere2.material = acrylicBlue_BlinnPhongMaterial;
				//intersectables.add(sphere2);
				
				
				

				Transformation meshTransform = Transformation.createRotationY(90);
				meshTransform = meshTransform.append(Transformation.createRotationX(0));
			
				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0,-1, 2));
				
				
				ObjectFileReader2 reader = new ObjectFileReader2();
				CompoundObject mesh;
				mesh = reader.readFile("objects//luxo_test.obj");
				mesh.setTransformation(meshTransform);
				mesh.material = aluminium_CookTorranceMaterial;
			//	intersectables.add(mesh);
				
				CompoundObject testCompound = new CompoundObject(Transformation.createIdentity());
				testCompound.addObject(sphere2);
				testCompound.addObject(mesh);
				intersectables.add(testCompound);
				
////				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box1.material = phong;
////				intersectables.add(box1);
				
				
								
//				testApple(phong);				
//				testApple2(phong);
//				testAreaLightSuzanne1(phong);				
//				testBoxPrimitive(phong);
//				testBuddha(phong);
//				testBunny(phong);				
//				testBunnyReflective(phong);
//				testCylindricalMapping();				
//				testCylinderPrimitive(phong);
//				testDiskPrimitive(phong);
//				testGlossyMaterialSphereAncCylinder(reflectiveMaterial1, glossyMaterial);
//				testHouse();				
//				testLowBuddha(phong);				
//				testLowResDragon(phong);
//				testParallelogramPrimitive(phong);	
//				testParallelogramWithVertices(phong, matte);
//				testPlanePrimitive(matte);
//				testPlanePrimitive2();
				
				testPlanePrimitiveCheckersMaterial();				
//				testSpherePrimitive(phong);				
//				testSpherePrimitiveLightProbeMapping(phong);	
//				testSphericalMapping();
//				testSuzanne(phong);				
//				testTeapot(phong);					
//				testThinLensCamera();				
//				testSquarePrimitive(phong);	
//				testTrianglePrimitive(phong);				
//				testTriceratops(phong);
//				testVenus(phong);
			
	}

	private void testGlossyMaterialSphereAncCylinder(
			ReflectiveMaterial reflectiveMaterial1,
			GlossyReflectionMaterial glossyMaterial) {
		//=== OBJECTS ===//
		Transformation sphereTrans = Transformation.createRotationY(0);
		sphereTrans = sphereTrans.append(Transformation.createRotationX(0));
		sphereTrans = sphereTrans.appendToTheLeft(Transformation.createTranslation(1 ,0, 3));
		Sphere sphere = new  Sphere(sphereTrans, 1);
		sphere.material = reflectiveMaterial1;
		intersectables.add(sphere);
		
		Transformation cylinderTrans = Transformation.createRotationY(0);
		cylinderTrans = cylinderTrans.append(Transformation.createRotationX(0));
		cylinderTrans = cylinderTrans.appendToTheLeft(Transformation.createTranslation(-1 ,0, 3));
		Cylinder cylinder = new Cylinder(cylinderTrans, 1, -1, 1);
		cylinder.material = glossyMaterial;
		intersectables.add(cylinder);
	}

	public ShadeRec hitObjects(Ray ray){
		
		//hb p 262
		ShadeRec sr = new ShadeRec(this);
		
		double tmin = Double.MAX_VALUE;
		Vector normal = null;
		Point localHitPoint = null;
		//Shape hittedShape = null;
		
		for(Intersectable intersectable: intersectablesToIntersect){
			if(intersectable.intersect(ray, sr)){
				if(sr.t < tmin){
					//hittedShape = shape;
					sr.hasHitAnObject = true;
					sr.ray = ray;
					
					if (intersectable.isInfinite()) {
						sr.material = ((Shape) intersectable).getMaterial();	
					}
					
					sr.hitPoint = ray.origin.add(ray.direction.scale(sr.t));
					
					//deze drie worden door de intersect functie van een shape aangepast
					// en moeten we tijdelijk lokaal opslaan
					tmin = sr.t;
					normal = sr.normal;
					localHitPoint = sr.localHitPoint;	
				}
			}	
		}
		if(sr.hasHitAnObject){
			sr.t = tmin;
			sr.normal = normal;
			sr.localHitPoint = localHitPoint;
		}
		if(sr.bvhCounter > this.maxBVHCounter){
			this.maxBVHCounter = sr.bvhCounter;
		}
		return sr;
	}

	/**
	 * Method called for a specific light source 
	 *  when shading a hitpoint on an object
	 *  to see if that hitpoint is in the shadow
	 *  
	 * check for all objects in the scene:
	 * if the shadow ray in the direction to the light source starting from the hitpoint
	 * 		hits another object
	 * and if the distance to that object is a positive number t smaller than the distance d
	 * then the hit point lies in the shadow of that object under this light source.
	 *  
	 * @param shadowRay
	 * @param distance  distance between the hit point on an object
	 * 					 and the point on the light source
	 * 
	 * @return boolean returns true if there is an object between the hitpoint and the point on the light source
	 */
	public boolean shadowHitObjects(Ray shadowRay, double distance){
		ShadeRec sr = new ShadeRec(this);
		for(Intersectable intersectable: intersectablesToIntersect){
			if(intersectable.shadowHit(shadowRay, sr) && sr.t < distance){
				return true;
			}
		}
		return false;
	}
	
	public void createBVH2(){
		BVHManager2 manager = new BVHManager2();
		List<Shape> finiteShapes = new ArrayList<Shape>();
		
		for(Intersectable intersectable: intersectables){
			if (intersectable.isInfinite()) {
				intersectablesToIntersect.add(intersectable);
			}else {
				finiteShapes.add((Shape)intersectable);
			}
		}
		intersectablesToIntersect.add(manager.getBoundingVolumeHierarchy(finiteShapes));
	}


	/**
	 * 
	 * @param imageWidth The number of pixels of the image in the horizontal direction
	 * @param imageHeight The number of pixels of the image in the vertical direction
	 */
	private void initializeThinLensCamera(int imageWidth, int imageHeight) {
		camera = new ThinLensCamera(imageWidth, imageHeight,
				new Point(0,1,0), new Vector(0, 0, 1), new Vector(0, 1, 0), 60,
				0.2, 9, new JitteredSampleFactory(0, 0, 1, 10));
	}
	
	
	/**
	 * 
	 * @param imageWidth The number of pixels of the image in the horizontal direction
	 * @param imageHeight The number of pixels of the image in the vertical direction
	 */
	private void initializePerspectiveCamera(int imageWidth, int imageHeight) {
		camera = new PerspectiveCamera(imageWidth, imageHeight,
				//new Point(5,5,5), new Vector(-1, -1, -1), new Vector(0, 1, 0), 120);
				//new Point(0,0, 6), new Vector(0, 0, -1), new Vector(0, 1, 0),90);
				new Point(0,0,-0.65), new Vector(0, 0, 1), new Vector(0, 1, 0), 60);
	}

	private void testThinLensCamera() {
		Transformation box1Transf = Transformation.createIdentity();
		Box box1 = new Box(box1Transf, new Point(-1, -1, 7), new Point(1, 2, 9));
		Texture boxText1 = new Checkers3DTexture(RGBColor.BLACK, new RGBColor(1.0f, 1.0f, 0.0f), 0.25);
		SVMatteMaterial svMatteBox1 = new SVMatteMaterial();
		svMatteBox1.setKa(0.45);
		svMatteBox1.setKd(0.65);
		svMatteBox1.setCd(boxText1);
		box1.material = svMatteBox1;
		intersectables.add(box1);
		
		Transformation box2Transf = Transformation.createIdentity();
		Box box2 = new Box(box2Transf, new Point(-3, -1, 5), new Point(-1, 2, 7));
		Texture boxText2 = new Checkers3DTexture(RGBColor.BLACK, new RGBColor(0.0f, 1.0f, 0.0f), 0.25);
		SVMatteMaterial svMatteBox2 = new SVMatteMaterial();
		svMatteBox2.setKa(0.45);
		svMatteBox2.setKd(0.65);
		svMatteBox2.setCd(boxText2);
		box2.material = svMatteBox2;
		intersectables.add(box2);
		
		Transformation box3Transf = Transformation.createIdentity();
		Box box3 = new Box(box3Transf, new Point(1, -1, 9), new Point(3, 2, 11));
		Texture boxText3 = new Checkers3DTexture(RGBColor.BLACK, new RGBColor(1.0f, 0.0f, 0.0f), 0.25);
		SVMatteMaterial svMatteBox3 = new SVMatteMaterial();
		svMatteBox3.setKa(0.45);
		svMatteBox3.setKd(0.65);
		svMatteBox3.setCd(boxText3);
		box3.material = svMatteBox3;
		intersectables.add(box3);
	}

	private void testApple(PhongMaterial phong) {
			ImageTexture appleImTex = new ImageTexture("objects//apple//apple_texture.jpg", null);
			SVMatteMaterial appleSVMatte = new SVMatteMaterial();
			appleSVMatte.setKa(0.45);
			appleSVMatte.setKd(0.65);
			appleSVMatte.setCd(appleImTex);
			
			Transformation meshTransform = Transformation.createRotationY(0);
			meshTransform = meshTransform.append(Transformation.createRotationX(0));
			meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0,0, 0.5));
	//		
			NormalMapTexture appleNormalMap = new NormalMapTexture("objects//apple//apple_objectSpaceNormalMap.png",
					meshTransform);
			appleSVMatte.setNormalMaptexture(appleNormalMap);
			
			ObjectFileReader2 reader = new ObjectFileReader2();
			CompoundObject mesh;
			mesh = reader.readFile("objects//apple//apple.obj");
			mesh.setTransformation(meshTransform);
			mesh.material = appleSVMatte;
			intersectables.add(mesh);
	//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
	//				box1.material = phong;
	//				intersectables.add(box1);
		}

	private void testApple2(PhongMaterial phong) {

			MatteMaterial matte = new MatteMaterial();
			matte.setKa(0.25);
			matte.setKd(0.65);
			matte.setCd(RGBColor.convertToRGBColor(Color.GREEN));
			
			
			ConstantColorTexture constantColorTexture = new ConstantColorTexture(RGBColor.convertToRGBColor(Color.GREEN));

			ImageTexture appleImTex = new ImageTexture("objects//apple2//apple2_texture.jpg", null);
			SVMatteMaterial appleSVMatte = new SVMatteMaterial();
			appleSVMatte.setKa(0.25);
			appleSVMatte.setKd(0.65);
			appleSVMatte.setCd(appleImTex);
			
			Transformation meshTransform = Transformation.createRotationY(0);
			meshTransform = meshTransform.append(Transformation.createRotationX(90));
			meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0,-0.5, 0.5));
	//		
//			NormalMapTexture appleNormalMap = new NormalMapTexture("objects//apple2//apple2_normalMapFromBumpMap.png",
//					meshTransform);
//			appleSVMatte.setNormalMaptexture(appleNormalMap);
			
			ObjectFileReader2 reader = new ObjectFileReader2();
			CompoundObject mesh;
			mesh = reader.readFile("objects//apple2//apple2.obj");
			mesh.setTransformation(meshTransform);
			mesh.material = appleSVMatte;
			intersectables.add(mesh);
	//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
	//				box1.material = phong;
	//				intersectables.add(box1);
		}

	private void testAreaLightSuzanne1(PhongMaterial phong) {
			
	//		PointLight pl1 = new PointLight(3.0, new RGBColor(1), new Point(3,0,5));
	//		pl1.setCastShadows(true);
	//		this.addLight(pl1);
			
			EmissiveMaterial emissiveMat = new EmissiveMaterial();
			emissiveMat.setLs(1e1);
			
			Transformation sqrTrans = Transformation.createIdentity();
			Square sqr = new Square(sqrTrans, new Point(3,0,5), new Vector(0,1,0), new Vector(0,0,-1));
			sqr.material = emissiveMat;
			intersectables.add(sqr);
	
			AreaLight areaLight = new AreaLight();
			AreaLight.nbOfShadowRaysPerAreaLight = 100;
			areaLight.emissiveMaterial = emissiveMat;
			areaLight.setCastShadows(true);
			areaLight.lightEmittingShape = sqr;
			lights.add(areaLight);
			
			Transformation planeTransform = Transformation.createIdentity();
			Plane pl = new Plane(planeTransform, new Point(-2, 0, 0), new Vector(1,0,0));
			pl.material = phong;
			intersectables.add(pl);
			
			Transformation meshTransform = Transformation.createRotationY(200);
			meshTransform = meshTransform.append(Transformation.createScale(0.5, 0.5, 0.5));
			meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-0.5, 0, 5));
			ObjectFileReader2 reader = new ObjectFileReader2();
			CompoundObject mesh;
			mesh = reader.readFile("objects//suzanne.obj");
			mesh.setTransformation(meshTransform);
			mesh.material = phong;
			intersectables.add(mesh);
		}

	private void testBoxPrimitive(PhongMaterial phong) {
		// ==== BOX PRIMITIVE ==== ///
		Transformation boxTrans = Transformation.createRotationY(200);
		boxTrans = boxTrans.append(Transformation.createRotationX(0));
		boxTrans = boxTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 2));
		Box box = new  Box(boxTrans, new Point(), new Point(1,1,1));
		box.material = phong;
		intersectables.add(box);
	}

	private void testBuddha(PhongMaterial phong) {
			//==== BUDDHA ====//
			Transformation meshTransform = Transformation.createRotationY(0);
			meshTransform = meshTransform.append(Transformation.createRotationX(0));
			meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0, 0, 1.5));
			ObjectFileReader2 reader = new ObjectFileReader2();
			CompoundObject mesh;
			mesh = reader.readFile("objects//buddha.obj");
			mesh.setTransformation(meshTransform);
			mesh.material = phong;
			intersectables.add(mesh);
	//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
	//				box1.material = phong;
	//				intersectables.add(box1);
		}

	private void testBunny(PhongMaterial phong) {
			//==== BUNNY ====//
			Transformation meshTransform = Transformation.createRotationY(200);
			meshTransform = meshTransform.append(Transformation.createRotationX(10));
			meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -1, 10));
			ObjectFileReader2 reader = new ObjectFileReader2();
			CompoundObject mesh;
			mesh = reader.readFile("objects//bunny.obj");
			mesh.setTransformation(meshTransform);
			mesh.material = phong;
			intersectables.add(mesh);
	//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
	//				box1.material = phong;
	//				intersectables.add(box1);		
		}
	private void testBunnyReflective(PhongMaterial phong) {
		//==== BUNNY ====//
		ReflectiveMaterial reflectiveMaterial2 = new ReflectiveMaterial();
		reflectiveMaterial2.setKa(0.25);
		reflectiveMaterial2.setKd(0.65);
		reflectiveMaterial2.setCd(new RGBColor(0f, 0f, 1f));
		reflectiveMaterial2.setCr(RGBColor.WHITE);
		reflectiveMaterial2.setKr(0.75);
	
	
		Transformation meshTransform = Transformation.createRotationY(200);
		meshTransform = meshTransform.append(Transformation.createRotationX(10));
		meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -1, 10));
		ObjectFileReader2 reader = new ObjectFileReader2();
		CompoundObject mesh;
		mesh = reader.readFile("objects//bunny.obj");
		mesh.setTransformation(meshTransform);
		mesh.material = reflectiveMaterial2;
		intersectables.add(mesh);
//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
//				box1.material = phong;
//				intersectables.add(box1);		
	}

	private void testCylinderPrimitive(PhongMaterial phong) {
			//====CYLINDER PRIMITIVE ====//
			Transformation cylinderTrans = Transformation.createRotationY(200);
			cylinderTrans = cylinderTrans.append(Transformation.createRotationX(0));
			cylinderTrans = cylinderTrans.appendToTheLeft(Transformation.createTranslation(0 ,0,7));			
			Cylinder cy = new Cylinder(cylinderTrans, 1, 0, 2 );
			cy.material = phong;
			intersectables.add(cy);
	//				Box box1 = Box.boundingBoxtoBox(cy.getBoundingBox());
	//				box1.material = matte;
	//				//intersectables.add(box1);
	//			Box box2 = Box.aaBoundingBoxtoBox(cy.getAABoundingBox());
	//			box2.material = phong;
	//			intersectables.add(box2);
		}

	private void testCylindricalMapping() {
		Mapping mapping = new CylindricalMapping();
		ImageTexture imTex = new ImageTexture("Lambert-cylindrical-equal-area-projection.jpg", mapping);
		SVMatteMaterial svMatte = new SVMatteMaterial();
		svMatte.setKa(0.45);
		svMatte.setKd(0.65);
		svMatte.setCd(imTex);
		Transformation cylinderTrans = Transformation.createRotationY(0);
		cylinderTrans = cylinderTrans.append(Transformation.createRotationX(0));
		cylinderTrans = cylinderTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 2.5));
		Cylinder cylinder = new Cylinder(cylinderTrans, 1, -1, 1);
		cylinder.material = svMatte;
		intersectables.add(cylinder);
	}

	private void testDiskPrimitive(PhongMaterial phong) {
			//====DISK PRIMITIVE ====//
			Transformation diskTrans = Transformation.createRotationY(200);
			diskTrans = diskTrans.append(Transformation.createRotationX(40));
			diskTrans = diskTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 12));	
			Disk disk = new Disk(diskTrans, new Point(), 5, new Vector(0, 0, -1));
			disk.material = phong;
	//		intersectables.add(disk);
			Box box1 = Box.boundingBoxtoBox(disk.getBoundingBox());
			box1.material = phong;
			intersectables.add(box1);
	//		Box box2 = Box.aaBoundingBoxtoBox(disk.getAABoundingBox());
	//		box2.material = phong;
	//		intersectables.add(box2);
			
			
		}

	private void testHouse() {
		//======== HOUSE =====//
		ImageTexture imTex = new ImageTexture("objects//house//house_texture.jpg", null);
		SVMatteMaterial svMatte = new SVMatteMaterial();
		svMatte.setKa(0.45);
		svMatte.setKd(0.65);
		svMatte.setCd(imTex);
		Transformation meshTransform = Transformation.createRotationY(120);
		meshTransform = meshTransform.append(Transformation.createRotationX(40));
		meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0,0, 2));
		ObjectFileReader2 reader = new ObjectFileReader2();
		CompoundObject mesh;
		mesh = reader.readFile("objects//house//house.obj");
		mesh.setTransformation(meshTransform);
		mesh.material = svMatte;
		intersectables.add(mesh);
	}

	private void testLowResDragon(PhongMaterial phong) {
			//ImageTexture appleImTex = new ImageTexture("objects//dragonLowPoly//apple_texture.jpg", null);
			
			MatteMaterial matte = new MatteMaterial();
			matte.setKa(0.25);
			matte.setKd(0.65);
			matte.setCd(RGBColor.convertToRGBColor(Color.GREEN));
			
			
			ConstantColorTexture constantColorTexture = new ConstantColorTexture(RGBColor.convertToRGBColor(Color.GREEN));
			SVMatteMaterial dragonSVMatte = new SVMatteMaterial();
			dragonSVMatte.setKa(0.25);
			dragonSVMatte.setKd(0.65);
			dragonSVMatte.setCd(constantColorTexture);
			
			Transformation meshTransform = Transformation.createRotationY(90);
			meshTransform = meshTransform.append(Transformation.createRotationX(90));
			meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0, 0, 0.5));
			
			NormalMapTexture dragonNormalMap = new NormalMapTexture("objects//dragonLowPoly//dragonNormalMap4k.png",
					meshTransform);
			dragonSVMatte.setNormalMaptexture(dragonNormalMap);
			
			ObjectFileReader2 reader = new ObjectFileReader2();
			CompoundObject mesh;
			mesh = reader.readFile("objects//dragonLowPoly//dragonHighPoly.obj");
			mesh.setTransformation(meshTransform);
			mesh.material = matte;
			intersectables.add(mesh);
	//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
	//				box1.material = phong;
	//				intersectables.add(box1);
		}
	
	
	private void testLowBuddha(PhongMaterial phong) {
		MatteMaterial matte = new MatteMaterial();
		matte.setKa(0.25);
		matte.setKd(0.65);
		matte.setCd(RGBColor.convertToRGBColor(Color.GREEN));
		
		
		ConstantColorTexture constantColorTexture = new ConstantColorTexture(RGBColor.convertToRGBColor(Color.GREEN));
		SVMatteMaterial buddhaSVMatte = new SVMatteMaterial();
		buddhaSVMatte.setKa(0.25);
		buddhaSVMatte.setKd(0.65);
		buddhaSVMatte.setCd(constantColorTexture);
		
		Transformation meshTransform = Transformation.createRotationY(0);
		meshTransform = meshTransform.append(Transformation.createRotationX(90));
		meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0, 0, 0.5));
//		
//		NormalMapTexture buddhaNormalMap = new NormalMapTexture("objects//buddhaLowPoly//buddhaNormalMap4k.png",
//				meshTransform);
//		buddhaSVMatte.setNormalMaptexture(buddhaNormalMap);
		
		ObjectFileReader2 reader = new ObjectFileReader2();
		CompoundObject mesh;
		mesh = reader.readFile("objects//buddhaLowPoly//buddhaHighPoly.obj");
		mesh.setTransformation(meshTransform);
		mesh.material = buddhaSVMatte;
		intersectables.add(mesh);
//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
//				box1.material = phong;
//				intersectables.add(box1);
	}

	private void testParallelogramPrimitive(PhongMaterial phong) {
			//==== PARALLELOGRAM PRIMITIVE ====//
			Transformation pllTrans = Transformation.createRotationX(0);
			pllTrans = pllTrans.appendToTheLeft(Transformation.createTranslation(0, 0, 14)); 
			Parallelogram pll = new Parallelogram(pllTrans, new Point(5,0,0), new Point(-5,0,0), new Point(0,5,0));
			pll.material = phong;
			intersectables.add(pll);
	//		Box box1 = Box.boundingBoxtoBox(pll.getBoundingBox());
	//		box1.material = phong;
	//		intersectables.add(box1);
	//		Box box2 = Box.aaBoundingBoxtoBox(pll.getAABoundingBox());
	//		box2.material = phong;
	//		intersectables.add(box2);
		}

	private void testParallelogramWithVertices(PhongMaterial phong,
			MatteMaterial matte) {
		//=== CAMERA INSTELLEN ===//
		//camera = new PerspectiveCamera(imageWidth, imageHeight,
		//		new Point(5,5,5), new Vector(-1, -1, -1), new Vector(0, 1, 0), 120);
		//=======================//
		
		Transformation pllTrans = Transformation.createRotationX(0);
		pllTrans = pllTrans.appendToTheLeft(Transformation.createTranslation(0, 0, 0)); 
		Parallelogram pll = new Parallelogram(Transformation.createIdentity(), new Point(0,0,2), new Point(-2,2,4), new Point(0,3,2));
		pll.material = matte;
		intersectables.add(pll);
		
		Sphere sph1 = new Sphere(Transformation.createTranslation(0, 0, 2), 0.5);
		sph1.material = phong;
		intersectables.add(sph1);
		Sphere sph2 = new Sphere(Transformation.createTranslation(-2, 2, 4), 0.5);
		sph2.material = phong;
		intersectables.add(sph2);
		Sphere sph3 = new Sphere(Transformation.createTranslation(0, 3, 2),0.5);
		sph3.material = phong;
		intersectables.add(sph3);
	
		Sphere sph4 = new Sphere(Transformation.createTranslation(-2,5,4),0.5);
		sph4.material = phong;
		intersectables.add(sph4);
	}

	private void testPlanePrimitive(MatteMaterial matte) {
		Plane plane = new Plane(Transformation.createIdentity(), new Point(0,-1,0), new Vector(0,1,0));
		plane.material = matte;
		intersectables.add(plane);
	}

	private void testPlanePrimitive2() {
		Plane plane = new Plane(Transformation.createIdentity(), new Point(0,0, 11), new Vector(0,0, -1));
		PhongMaterial planeMaterial = new PhongMaterial();
		planeMaterial.setKa(0.25);
		planeMaterial.setKd(0.65);
		planeMaterial.setCd(RGBColor.convertToRGBColor(Color.CYAN));
		planeMaterial.setKs(0.2);
		planeMaterial.setPhongExponent(10);
		planeMaterial.setCs(RGBColor.convertToRGBColor(Color.WHITE));
		plane.material = planeMaterial;
		intersectables.add(plane);
	}

	private void testPlanePrimitiveCheckersMaterial() {
		//==== CHECKERS MATERIAL ====//
		Texture imTex = new Checkers3DTexture();
		SVMatteMaterial svMatte = new SVMatteMaterial();
		svMatte.setKa(0.25);
		svMatte.setKd(0.65);
		svMatte.setCd(imTex);
		//==== PLANE PRIMITIVE ====//
		Transformation planeTransform = Transformation.createIdentity();
		Plane pl = new Plane(planeTransform, new Point(0, -1, 0), new Vector(0,1,0));
		pl.material = svMatte;
		intersectables.add(pl);
	}

	private void testSphericalMapping() {
		Mapping mapping = new SphericalMapping();
		ImageTexture imTex = new ImageTexture("Lambert-cylindrical-equal-area-projection.jpg", mapping);
	
		SVMatteMaterial svMatte = new SVMatteMaterial();
		svMatte.setKa(0.45);
		svMatte.setKd(0.65);
		svMatte.setCd(imTex);
		
		Transformation sphereTrans = Transformation.createRotationY(0);
		sphereTrans = sphereTrans.append(Transformation.createRotationX(0));
		sphereTrans = sphereTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 2));
		Sphere sphere = new Sphere(sphereTrans, 1);
		sphere.material = svMatte;
		intersectables.add(sphere);
	}

	private void testSpherePrimitive(PhongMaterial phong) {
			// ==== SPHERE PRIMITIVE ==== ///
			Transformation sphereTrans = Transformation.createRotationY(0);
			sphereTrans = sphereTrans.append(Transformation.createRotationX(0));
			sphereTrans = sphereTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 2));
			Sphere sphere = new  Sphere(sphereTrans, 1);
			sphere.material = phong;
			intersectables.add(sphere);
	//				Box box1 = Box.boundingBoxtoBox(sphere.getBoundingBox());
	//				box1.material = matte;
	//				intersectables.add(box1);
	//				Box box2 = Box.aaBoundingBoxtoBox(sphere.getAABoundingBox());
	//				box2.material = matte;
	//				intersectables.add(box2);
		}
	
	
	private void testSpherePrimitiveLightProbeMapping(PhongMaterial phong) {
		// ==== SPHERE PRIMITIVE ==== ///
		
		LightProbeMapping lightProbeMapping = new LightProbeMapping();
		ImageTexture sphereImTex = new ImageTexture("angmap24Small.jpg", lightProbeMapping);
		SVMatteMaterial svMatte = new SVMatteMaterial();
		svMatte.setKa(0.45);
		svMatte.setKd(0.65);
		svMatte.setCd(sphereImTex);
		
		
		Transformation sphereTrans = Transformation.createRotationY(50);
		sphereTrans = sphereTrans.append(Transformation.createRotationX(0));
		sphereTrans = sphereTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 2));
		Sphere sphere = new  Sphere(sphereTrans, 1);
		sphere.material = svMatte;
		intersectables.add(sphere);
//				Box box1 = Box.boundingBoxtoBox(sphere.getBoundingBox());
//				box1.material = matte;
//				intersectables.add(box1);
//				Box box2 = Box.aaBoundingBoxtoBox(sphere.getAABoundingBox());
//				box2.material = matte;
//				intersectables.add(box2);
	}

	private void testSquarePrimitive(PhongMaterial phong) {
			//==== SQUARE PRIMITIVE ====//
			Transformation sqrTrans = Transformation.createRotationX(00);
			sqrTrans = sqrTrans.appendToTheLeft(Transformation.createTranslation(0, 0, 10)); 
			Square sqr = new Square(sqrTrans, new Point(0,0,0), new Vector(1,0,0), new Vector(0,1,0));
			sqr.material = phong;
			intersectables.add(sqr);
	//		Box box1 = Box.boundingBoxtoBox(sqr.getBoundingBox());
	//		box1.material = phong;
	//		intersectables.add(box1);
	//		Box box2 = Box.aaBoundingBoxtoBox(sqr.getAABoundingBox());
	//		box2.material = phong;
	//		intersectables.add(box2);
		}

	private void testSuzanne(PhongMaterial phong) {
			//==== SUZANNE ====//
			Transformation meshTransform = Transformation.createRotationY(200);
			meshTransform = meshTransform.append(Transformation.createRotationX(0));
			meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, 1, 7));
			ObjectFileReader2 reader = new ObjectFileReader2();
			CompoundObject mesh;
			mesh = reader.readFile("objects//suzanne.obj");
			mesh.setTransformation(meshTransform);
			mesh.material = phong;
			intersectables.add(mesh);
	//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
	//				box1.material = phong;
	//				intersectables.add(box1);
		}

	private void testTeapot(PhongMaterial phong) {
		Transformation meshTransform = Transformation.createRotationY(200);
		meshTransform = meshTransform.append(Transformation.createRotationX(10));
		meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -1, 10));
		ObjectFileReader2 reader = new ObjectFileReader2();
		CompoundObject mesh;
		mesh = reader.readFile("objects//teapot.obj");
		mesh.setTransformation(meshTransform);
		mesh.material = phong;
		intersectables.add(mesh);
	}

	private void testTeapotCheckers3D(SVMatteMaterial svMatte) {
		Transformation meshTransform = Transformation.createRotationY(200);
		meshTransform = meshTransform.append(Transformation.createRotationX(10));
		meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -1, 10));
		ObjectFileReader2 reader = new ObjectFileReader2();
		CompoundObject mesh;
		mesh = reader.readFile("objects//teapot.obj");
		mesh.setTransformation(meshTransform);
		mesh.material = svMatte;
		intersectables.add(mesh);
	}

	private void testTrianglePrimitive(PhongMaterial phong) {
			//==== TRIANGLE PRIMITIVE ====///
			Transformation triangleTrans = Transformation.createTranslation(0, 0, 14);
			Triangle tri = new Triangle(triangleTrans, new Point(5,0,0), new Point(-5,0,0), new Point(0,5,0));
			tri.material = phong;
			intersectables.add(tri);
	//		Box box = Box.boundingBoxtoBox(tri.getBoundingBox());
	//		box.material = phong;
	//		intersectables.add(box);
		}

	private void testTriceratops(PhongMaterial phong) {
		//==== TRICERATOPS ====//
		Transformation meshTransform = Transformation.createRotationY(200);
		meshTransform = meshTransform.append(Transformation.createRotationX(10));
		meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -1, 20));
		ObjectFileReader2 reader = new ObjectFileReader2();
		CompoundObject mesh;
		mesh = reader.readFile("objects//triceratops.obj");
		mesh.setTransformation(meshTransform);
		mesh.material = phong;
		intersectables.add(mesh);
//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
//				box1.material = phong;
//				intersectables.add(box1);	
	}

	private void testVenus(PhongMaterial phong) {
			//==== VENUS ====//
			Transformation meshTransform = Transformation.createRotationY(200);
			meshTransform = meshTransform.append(Transformation.createRotationX(10));
			meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -1, 15));
			ObjectFileReader2 reader = new ObjectFileReader2();
			CompoundObject mesh;
			mesh = reader.readFile("objects//venus.obj");
			mesh.setTransformation(meshTransform);
			mesh.material = phong;
			intersectables.add(mesh);
	//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
	//				box1.material = phong;
	//				intersectables.add(box1);
		}
}
