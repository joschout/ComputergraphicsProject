package main;

import ioPackage.ObjectFileReader;
import ioPackage.ObjectFileReader2;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import boundingVolumeHierarchy.BVHManager2;
import boundingVolumeHierarchy.CompositeAABBox;
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
import texture.Checkers3Dtexture;
import texture.ConstantColor;
import texture.ImageTexture;
import texture.NormalMapTexture;
import texture.Texture;
import light.AmbientLight;
import light.AreaLight;
import light.Light;
import light.PointLight;
import mapping.CylindricalMapping;
import mapping.Mapping;
import mapping.SphericalMapping;
import material.EmissiveMaterial;
import material.MatteMaterial;
import material.PhongMaterial;
import material.SVMatteMaterial;
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
		
	public World(){
		camera = null;
		backgroundColor = new RGBColor((float)0.3);
		ambientLight = new AmbientLight();
		maxBVHCounter = 0;
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
				this.initializePerspectiveCamera(imageWidth, imageHeight);
//				this.initializeThinLensCamera(imageWidth, imageHeight);
				
				
				PointLight pl1 = new PointLight(3.0, new RGBColor(1), new  Point(-5,3,0));
				pl1.setCastShadows(false);
				this.addLight(pl1);

				PointLight pl2 = new PointLight(1.0, new RGBColor(1), new  Point(5,3,0));
				pl2.setCastShadows(false);
				this.addLight(pl2);

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
				
				//==== CHECKERS MATERIAL ====//
				Texture imTex = new Checkers3Dtexture();
				SVMatteMaterial svMatte = new SVMatteMaterial();
				svMatte.setKa(0.45);
				svMatte.setKd(0.65);
				svMatte.setCd(imTex);
				
				
	
				
				
				

//				Mapping mapping = new SphericalMapping();
//				ImageTexture imTex = new ImageTexture("MercatorProjection.jpg", mapping);
//
//				SVMatteMaterial svMatte = new SVMatteMaterial();
//				svMatte.setKa(0.45);
//				svMatte.setKd(0.65);
//				svMatte.setCd(imTex);
//				
//				Transformation sphereTrans = Transformation.createRotationY(120);
//				sphereTrans = sphereTrans.append(Transformation.createRotationX(180));
//				sphereTrans = sphereTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 4));
//				Sphere sphere = new Sphere(sphereTrans, 1);
//				sphere.material = svMatte;
//				intersectables.add(sphere);
				
				
//				Mapping mapping = new CylindricalMapping();
//				ImageTexture imTex = new ImageTexture("Hobbel.JPG", mapping);
//				SVMatteMaterial svMatte = new SVMatteMaterial();
//				svMatte.setKa(0.45);
//				svMatte.setKd(0.65);
//				svMatte.setCd(imTex);
//				Transformation cylinderTrans = Transformation.createRotationY(180);
//				cylinderTrans = cylinderTrans.append(Transformation.createRotationX(180));
//				cylinderTrans = cylinderTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 4));
//				Cylinder cylinder = new Cylinder(cylinderTrans, 1, -1, 1);
//				cylinder.material = svMatte;
//				intersectables.add(cylinder);		
			
				
//				Plane plane = new Plane(Transformation.createIdentity(), new Point(0,0,0), new Vector(0,1,0));
//				plane.material = matte;
//				intersectables.add(plane);
				

//				Plane plane = new Plane(Transformation.createIdentity(), new Point(0,0, 11), new Vector(0,0, -1));
//				PhongMaterial planeMaterial = new PhongMaterial();
//				planeMaterial.setKa(0.25);
//				planeMaterial.setKd(0.65);
//				planeMaterial.setCd(RGBColor.convertToRGBColor(Color.CYAN));
//				planeMaterial.setKs(0.2);
//				planeMaterial.setPhongExponent(10);
//				planeMaterial.setCs(RGBColor.convertToRGBColor(Color.WHITE));
//				plane.material = planeMaterial;
//				intersectables.add(plane);

				
				
//				//==== PLANE PRIMITIVE ====//
//				Transformation planeTransform = Transformation.createTranslation(0, 0, 5);
//				//Transformation planeTransform2 = Transformation.createRotationX(50);
//				Plane pl = new Plane(planeTransform, new Point(0, -5, 0), new Vector(0,1,0));
//				pl.material = svMatte;
//				intersectables.add(pl);
				
				
//				//==== PLANE PRIMITIVE ====//
//				Transformation planeTransform = Transformation.createIdentity();
//				Plane pl = new Plane(planeTransform, new Point(0, -1, 0), new Vector(0,1,0));
//				pl.material = svMatte;
//				intersectables.add(pl);
				
				
				
//				testLowResDragon(phong);
//				testApple2(phong);
				testLowBuddha(phong);
				
//				testThinLensCamera();
//				testAreaLightSuzanne1(phong);
//				testSquarePrimitive(phong);
//				testTrianglePrimitive(phong);				
//				testParallelogramPrimitive(phong);				
//				testDiskPrimitive(phong);			
//				testTeapotCheckers3D(svMatte);
//				testSuzanne(phong);
//				testBunny(phong);
//				testTriceratops(phong);
//				testVenus(phong);
//				testCylinderPrimitive(phong);
//				testBoxPrimitive(phong);
//				testSpherePrimitive(phong);
//				testApple(phong);
//				testHouse();
//				testTeapot(phong);	
//				testBuddha(phong);
				
				
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
	
				//new Point(0,5,0), new Vector(0, -1,3), new Vector(0, 1, 0),90);
				new Point(0,0,-0.65), new Vector(0, 0, 1), new Vector(0, 1, 0), 60);
	}

	private void testThinLensCamera() {
		Transformation box1Transf = Transformation.createIdentity();
		Box box1 = new Box(box1Transf, new Point(-1, -1, 7), new Point(1, 2, 9));
		Texture boxText1 = new Checkers3Dtexture(new RGBColor(0), new RGBColor(1.0f, 1.0f, 0.0f), 0.25);
		SVMatteMaterial svMatteBox1 = new SVMatteMaterial();
		svMatteBox1.setKa(0.45);
		svMatteBox1.setKd(0.65);
		svMatteBox1.setCd(boxText1);
		box1.material = svMatteBox1;
		intersectables.add(box1);
		
		Transformation box2Transf = Transformation.createIdentity();
		Box box2 = new Box(box2Transf, new Point(-3, -1, 5), new Point(-1, 2, 7));
		Texture boxText2 = new Checkers3Dtexture(new RGBColor(0), new RGBColor(0.0f, 1.0f, 0.0f), 0.25);
		SVMatteMaterial svMatteBox2 = new SVMatteMaterial();
		svMatteBox2.setKa(0.45);
		svMatteBox2.setKd(0.65);
		svMatteBox2.setCd(boxText2);
		box2.material = svMatteBox2;
		intersectables.add(box2);
		
		Transformation box3Transf = Transformation.createIdentity();
		Box box3 = new Box(box3Transf, new Point(1, -1, 9), new Point(3, 2, 11));
		Texture boxText3 = new Checkers3Dtexture(new RGBColor(0), new RGBColor(1.0f, 0.0f, 0.0f), 0.25);
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
			
			
			ConstantColor constantColorTexture = new ConstantColor(RGBColor.convertToRGBColor(Color.GREEN));

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
			
			
			ConstantColor constantColorTexture = new ConstantColor(RGBColor.convertToRGBColor(Color.GREEN));
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
		
		
		ConstantColor constantColorTexture = new ConstantColor(RGBColor.convertToRGBColor(Color.GREEN));
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

	private void testSpherePrimitive(PhongMaterial phong) {
			// ==== SPHERE PRIMITIVE ==== ///
			Transformation sphereTrans = Transformation.createRotationY(200);
			sphereTrans = sphereTrans.append(Transformation.createRotationX(0));
			sphereTrans = sphereTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 4));
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
