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
import rayTracers.BVHFalseColorGrayTracer;
import rayTracers.BVHFalseColorImageTracer;
import rayTracers.BVHTracer;
import rayTracers.DepthTracer;
import rayTracers.MultipleObjectsTracer;
import rayTracers.NormalBVHTracer;
import rayTracers.NormalFalseColorImagetracer;
import rayTracers.Tracer;
import shape.Box;
import shape.CompoundObject;
import shape.Cylinder;
import shape.Disk;
import shape.Intersectable;
import shape.Parallelogram;
import shape.Plane;
import shape.Shape;
import shape.Sphere;
import shape.Triangle;
import shape.TriangleMesh;
import texture.Checkers3Dtexture;
import texture.ImageTexture;
import texture.Texture;
import light.AmbientLight;
import light.Light;
import light.PointLight;
import mapping.CylindricalMapping;
import mapping.Mapping;
import mapping.SphericalMapping;
import material.MatteMaterial;
import material.PhongMaterial;
import material.SVMatteMaterial;
import material.SpecialMatteMaterial;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class World {
	
	public  Camera camera;
	public RGBColor backgroundColor;
	public  List<Shape> shapes = new ArrayList<Shape>();
	public List<Light> lights = new ArrayList<Light>();
	//public Tracer tracer;
	public Light ambientLight;
	public CompositeAABBox bvh;
	public int maxBVHCounter;
	
	
	public World(){
		camera = null;
		backgroundColor = new RGBColor((float)0.3);
		ambientLight = new AmbientLight();
		bvh = null;
		maxBVHCounter = 0;
	}
	
	public void addShape(Shape shape){
		shapes.add(shape);
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
				this.initializeCamera(imageWidth, imageHeight);
				
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
//				shapes.add(sphere);
				
				
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
//				shapes.add(cylinder);		
			
				
//				Plane plane = new Plane(Transformation.createIdentity(), new Point(0,0,0), new Vector(0,1,0));
//				plane.material = matte;
//				shapes.add(plane);		
				
				
//				testTrianglePrimitive(phong);				
//				testParallelogramPrimitive(phong);				
//				testDiskPrimitive(phong);			
				testTeapotCheckers3D(svMatte);
//				testSuzanne(phong);
//				testBunny(phong);
//				testTriceratops(phong);
//				testVenus(phong);
//				testCylinderPrimitive(phong);
//				testBoxPrimitive(phong);
//				testSpherePrimitive(phong);
//				testApple();
//				testHouse();
//				testTeapot(phong);	
//				testBuddha(phong);
				
				
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
		shapes.add(mesh);
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
		shapes.add(mesh);
//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
//				box1.material = phong;
//				shapes.add(box1);
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
		shapes.add(mesh);
//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
//				box1.material = phong;
//				shapes.add(box1);	
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
		shapes.add(mesh);
//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
//				box1.material = phong;
//				shapes.add(box1);		
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
		shapes.add(mesh);
//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
//				box1.material = phong;
//				shapes.add(box1);
	}

	private void testCylinderPrimitive(PhongMaterial phong) {
		//====CYLINDER PRIMITIVE ====//
		Transformation cylinderTrans = Transformation.createRotationY(200);
		cylinderTrans = cylinderTrans.append(Transformation.createRotationX(0));
		cylinderTrans = cylinderTrans.appendToTheLeft(Transformation.createTranslation(0 ,0,7));			
		Cylinder cy = new Cylinder(cylinderTrans, 1, 0, 2 );
		cy.material = phong;
		shapes.add(cy);
//				Box box1 = Box.boundingBoxtoBox(cy.getBoundingBox());
//				box1.material = matte;
//				//shapes.add(box1);
//			Box box2 = Box.aaBoundingBoxtoBox(cy.getAABoundingBox());
//			box2.material = phong;
//			shapes.add(box2);
	}

	private void testBoxPrimitive(PhongMaterial phong) {
		// ==== BOX PRIMITIVE ==== ///
		Transformation boxTrans = Transformation.createRotationY(200);
		boxTrans = boxTrans.append(Transformation.createRotationX(0));
		boxTrans = boxTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 2));
		Box box = new  Box(boxTrans, new Point(), new Point(1,1,1));
		box.material = phong;
		shapes.add(box);
	}

	private void testDiskPrimitive(PhongMaterial phong) {
		//====DISK PRIMITIVE ====//
		Transformation diskTrans = Transformation.createRotationY(200);
		diskTrans = diskTrans.append(Transformation.createRotationX(40));
		diskTrans = diskTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 12));	
		Disk disk = new Disk(diskTrans, new Point(), 5, new Vector(0, 0, -1));
		disk.material = phong;
//		shapes.add(disk);
		Box box1 = Box.boundingBoxtoBox(disk.getBoundingBox());
		box1.material = phong;
		shapes.add(box1);
//		Box box2 = Box.aaBoundingBoxtoBox(disk.getAABoundingBox());
//		box2.material = phong;
//		shapes.add(box2);
		
		
	}

	private void testParallelogramPrimitive(PhongMaterial phong) {
		//==== PARALLELOGRAM PRIMITIVE ====//
		Transformation pllTrans = Transformation.createRotationX(0);
		pllTrans = pllTrans.appendToTheLeft(Transformation.createTranslation(0, 0, 14)); 
		Parallelogram pll = new Parallelogram(pllTrans, new Point(5,0,0), new Point(-5,0,0), new Point(0,5,0));
		pll.material = phong;
		shapes.add(pll);
//		Box box1 = Box.boundingBoxtoBox(pll.getBoundingBox());
//		box1.material = phong;
//		shapes.add(box1);
//		Box box2 = Box.aaBoundingBoxtoBox(pll.getAABoundingBox());
//		box2.material = phong;
//		shapes.add(box2);
	}

	private void testTrianglePrimitive(PhongMaterial phong) {
		//==== TRIANGLE PRIMITIVE ====///
		Transformation triangleTrans = Transformation.createTranslation(0, 0, 14);
		Triangle tri = new Triangle(triangleTrans, new Point(5,0,0), new Point(-5,0,0), new Point(0,5,0));
		tri.material = phong;
		shapes.add(tri);
//		Box box = Box.boundingBoxtoBox(tri.getBoundingBox());
//		box.material = phong;
//		shapes.add(box);
	}

	private void testSpherePrimitive(PhongMaterial phong) {
		// ==== SPHERE PRIMITIVE ==== ///
		Transformation sphereTrans = Transformation.createRotationY(200);
		sphereTrans = sphereTrans.append(Transformation.createRotationX(0));
		sphereTrans = sphereTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 4));
		Sphere sphere = new  Sphere(sphereTrans, 1);
		sphere.material = phong;
		shapes.add(sphere);
//				Box box1 = Box.boundingBoxtoBox(sphere.getBoundingBox());
//				box1.material = matte;
//				shapes.add(box1);
//				Box box2 = Box.aaBoundingBoxtoBox(sphere.getAABoundingBox());
//				box2.material = matte;
//				shapes.add(box2);
	}

	private void testApple() {
		ImageTexture imTex = new ImageTexture("objects//apple//apple_texture.jpg", null);
		SVMatteMaterial svMatte = new SVMatteMaterial();
		svMatte.setKa(0.45);
		svMatte.setKd(0.65);
		svMatte.setCd(imTex);
		Transformation meshTransform = Transformation.createRotationY(1);
		meshTransform = meshTransform.append(Transformation.createRotationX(0));
		meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0,0, 2));
		ObjectFileReader reader = new ObjectFileReader();
		TriangleMesh mesh;
		mesh = reader.readFile("objects//apple//apple.obj");
		mesh.setTransformation(meshTransform);
		mesh.material = svMatte;
		shapes.add(mesh);
//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
//				box1.material = phong;
//				shapes.add(box1);
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
		shapes.add(mesh);
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
		shapes.add(mesh);
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
		shapes.add(mesh);
//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
//				box1.material = phong;
//				shapes.add(box1);
	}

	public ShadeRec hitObjects(Ray ray){
		
		//hb p 262
		ShadeRec sr = new ShadeRec(this);
		
		double tmin = Double.MAX_VALUE;
		Vector normal = null;
		Point localHitPoint = null;
		//Shape hittedShape = null;
		
		for(Shape shape: shapes){
			if(shape.intersect(ray, sr)){
				if(sr.t < tmin){
					//hittedShape = shape;
					sr.hasHitAnObject = true;
					sr.ray = ray;
					sr.material = shape.getMaterial();	
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
		return sr;
	}
	
	
public ShadeRec hitBVH(Ray ray){
		ShadeRec sr = new ShadeRec(this);
		this.bvh.intersect(ray, sr);
		
		if(sr.bvhCounter > this.maxBVHCounter){
			this.maxBVHCounter = sr.bvhCounter;
		}
		
		return sr;
	}
	
	
	
	/**
	 * 
	 * @param imageWidth The number of pixels of the image in the horizontal direction
	 * @param imageHeight The number of pixels of the image in the vertical direction
	 */
	private void initializeCamera(int imageWidth, int imageHeight) {
		camera = new PerspectiveCamera(imageWidth, imageHeight,

				//new Point(0,5,0), new Vector(0, -1,3), new Vector(0, 1, 0),90);
				new Point(0,0,-0.65), new Vector(0, 0, 1), new Vector(0, 1, 0), 60);
	}
	
	public CompositeAABBox createBVH(){
		BVHManager2 manager = new BVHManager2();
		return manager.getBoundingVolumeHierarchy(this.shapes);
		//return BVHManagerCfrJerre.getBoundingVolumeHierarchy(this.shapes);
	}
	
	public void setBVH(CompositeAABBox bvh){
		this.bvh = bvh;
	}
}
