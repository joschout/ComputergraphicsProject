package shape;

import ioPackage.ObjectFileReader;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import camera.Camera;
import camera.PerspectiveCamera;
import rayTracers.DepthTracer;
import rayTracers.MultipleObjectsTracer;
import rayTracers.NormalFalseColorImagetracer;
import rayTracers.Tracer;
import light.AmbientLight;
import light.Light;
import light.PointLight;
import material.MatteMaterial;
import material.PhongMaterial;
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
	public Tracer tracer;
	public Light ambientLight;
	
	
	public World(){
		camera = null;
		backgroundColor = new RGBColor((float)0.3);
		tracer = new MultipleObjectsTracer(this);
		//tracer = new DepthTracer(this);
		//tracer = new NormalFalseColorImagetracer(this);
		ambientLight = new AmbientLight();
	}
	
	public void addShape(Shape shape){
		shapes.add(shape);
	}
	
	public void addLight(Light light){
		lights.add(light);
	}
	
	
	public  void build(int width, int height){
				this.initializeCamera(width, height);
				// initialize the scene
				Transformation t1 = Transformation.createTranslation(0, 0, 5);
				Transformation t2 = Transformation.createTranslation(4, -4, 14);
				Transformation t3 = Transformation.createTranslation(-4, -4, 14);
				Transformation t4 = Transformation.createTranslation(4, 4, 14);
				Transformation t5 = Transformation.createTranslation(-4, 4, 14);
				
				Transformation t6 = Transformation.createRotationX(10);
				Transformation t7 = t1.append(t6);
				Transformation t8 = t2.append(t6);
				
				PointLight pl1 = new PointLight();
				pl1.setLocation(new Point(0,0,-1));
				this.addLight(pl1);
				
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
				matte.setCd(RGBColor.convertToRGBColor(Color.BLUE));
				
//				// ==== BOX PRIMITIVE ==== ///
//				Transformation boxTrans = Transformation.createRotationY(200);
//				boxTrans = boxTrans.append(Transformation.createRotationX(40));
//				boxTrans = boxTrans.appendToTheLeft(Transformation.createTranslation(0 ,0, 4));
//				Box box = new  Box(boxTrans, new Point(), new Point(1,1,1));
//				box.material = phong;
//				shapes.add(box);
				
				
//				Plane plane = new Plane(t2, new Point(), new Vector(0,1,0));
//				plane.material = matte;
//				shapes.add(plane);
				
//				Sphere sp1 = new Sphere(t1, 5);
//				sp1.material =  matte;
//				shapes.add(sp1);
//				
//				Sphere sp2 = new Sphere(t2, 3);
//				sp2.material = phong;
//				shapes.add(sp2);
//				
//				Sphere sp3 = new Sphere(t3, 3);
//				sp3.material = phong;
//				shapes.add(sp3);
//				
//				Sphere sp4 = new Sphere(t4, 3);
//				sp4.material = phong;
//				shapes.add(sp4);
//				
//				Sphere sp5 = new Sphere(t5, 3);
//				sp5.material = phong;
//				shapes.add(sp5);
			
//				Cylinder cy = new Cylinder(t7, 1, 1, -1);
//				cy.material = phong;
//				shapes.add(cy);
				
//				Disk disk = new Disk(t8, new Point(), 60, new Vector(0, 0, -1));
//				disk.material = matte;
//				shapes.add(disk);
				
//				//==== TRIANGLE PRIMITIVE ====///
//				Transformation triangleTrans = Transformation.createTranslation(0, 0, 14);
//				Triangle tri = new Triangle(triangleTrans, new Point(5,0,0), new Point(-5,0,0), new Point(0,5,0));
//				tri.material = phong;
//				shapes.add(tri);
//				Box box = Box.boundingBoxtoBox(tri.getBoundingBox());
//				box.material = matte;
//				shapes.add(box);
				
//				//==== PARALLELOGRAM PRIMITIVE ====//
//				Transformation pllTrans = Transformation.createRotationX(0);
//				pllTrans = pllTrans.appendToTheLeft(Transformation.createTranslation(0, 0, 14)); 
//				Parallelogram pll = new Parallelogram(pllTrans, new Point(5,0,0), new Point(-5,0,0), new Point(0,5,0));
//				pll.material = phong;
//				shapes.add(pll);
		
//				//==== PLANE PRIMITIVE ====//
//				Transformation planeTransform = Transformation.createTranslation(0, 0, 5);
//				Transformation planeTransform2 = Transformation.createRotationX(50);
//				Plane pl = new Plane(planeTransform, new Point(0, -5, 0), new Vector(0,1,0));
//				pl.material = phong;
//				shapes.add(pl);
				
//				//==== BUNNY ====//
//				Transformation meshTransform = Transformation.createRotationY(200);
//				meshTransform = meshTransform.append(Transformation.createRotationX(10));
//				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -1, 10));
//				ObjectFileReader reader = new ObjectFileReader();
//				TriangleMesh mesh;
//				mesh = reader.readFile("objects//bunny.obj");
//				mesh.setTransformation(meshTransform);
//				mesh.material = phong;
//				shapes.add(mesh);
////				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box1.material = phong;
////				shapes.add(box1);
				
//				//===== SUZANNE ===========//
//				Transformation meshTransform = Transformation.createRotationY(200);
//				meshTransform = meshTransform.append(Transformation.createRotationX(10));
//				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0, 0, 2));
//				ObjectFileReader reader = new ObjectFileReader();
//				TriangleMesh mesh;
//				mesh = reader.readFile("objects//suzanne.obj");
//				mesh.setTransformation(meshTransform);
//				mesh.material = phong;
//				shapes.add(mesh);
////				Box box = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box.material = phong;
////				shapes.add(box);
				
				
	
//				//======== CYLINDER ==========//
//				Transformation meshTransform = Transformation.createRotationY(0);
//				meshTransform = meshTransform.append(Transformation.createRotationX(-15));
//				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0, 0, 5));
//				ObjectFileReader reader = new ObjectFileReader();
//				TriangleMesh mesh;
//				mesh = reader.readFile("objects//cylinder.obj");
//				mesh.setTransformation(meshTransform);
//				mesh.material = phong;
//				shapes.add(mesh);	
////				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box1.material = phong;
////				shapes.add(box1);
			
//				
//				//======== CONE ==========//
//				Transformation meshTransform = Transformation.createRotationY(0);
//				meshTransform = meshTransform.append(Transformation.createRotationX(-15));
//				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0, 0, 5));
//				ObjectFileReader reader = new ObjectFileReader();
//				TriangleMesh mesh;
//				mesh = reader.readFile("objects//cone.obj");
//				mesh.setTransformation(meshTransform);
//				mesh.material = phong;
//				//shapes.add(mesh);
////				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box1.material = matte;
////				shapes.add(box1);
////				Box box2 = Box.boundingBoxtoBox(mesh.getBoundingBoxFromScratch());
////				box2.material = matte;
////				shapes.add(box2);
	
//				//======== apple ==========//
//				Transformation meshTransform = Transformation.createRotationY(1);
//				meshTransform = meshTransform.append(Transformation.createRotationX(0));
//				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-0.2,-0.5, 1));
//				ObjectFileReader reader = new ObjectFileReader();
//				TriangleMesh mesh;
//				mesh = reader.readFile("objects//apple//apple.obj");
//				mesh.setTransformation(meshTransform);
//				mesh.material = phong;
//				shapes.add(mesh);
////				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box1.material = phong;
////				shapes.add(box1);
				
//				//======== TORUS ==========//
//				Transformation meshTransform = Transformation.createRotationY(0);
//				meshTransform = meshTransform.append(Transformation.createRotationX(40));
//				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0, 0, 5));
//				ObjectFileReader reader = new ObjectFileReader();
//				TriangleMesh mesh;
//				mesh = reader.readFile("objects//torus.obj");
//				mesh.setTransformation(meshTransform);
//				mesh.material = phong;
//				shapes.add(mesh);
////				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box1.material = phong;
////				shapes.add(box1);				
			
				//======== Table ==========//
//				Transformation meshTransform = Transformation.createRotationY(0);
//				meshTransform = meshTransform.append(Transformation.createRotationX(0));
//				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(0, 0, 5));
//				ObjectFileReader reader = new ObjectFileReader();
//				TriangleMesh mesh;
//				mesh = reader.readFile("objects//table.obj");
//				mesh.setTransformation(meshTransform);
//				mesh.material = phong;
//				shapes.add(mesh);
////				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box1.material = phong;
////				shapes.add(box1);
						
//				//==== VENUS ====//
//				Transformation meshTransform = Transformation.createRotationY(200);
//				meshTransform = meshTransform.append(Transformation.createRotationX(10));
//				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -1, 15));
//				ObjectFileReader reader = new ObjectFileReader();
//				TriangleMesh mesh;
//				mesh = reader.readFile("objects//venus.obj");
//				mesh.setTransformation(meshTransform);
//				mesh.material = phong;
//				shapes.add(mesh);
////				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box1.material = phong;
////				shapes.add(box1);
				
//				//==== TRICERATOPS ====//
//				Transformation meshTransform = Transformation.createRotationY(200);
//				meshTransform = meshTransform.append(Transformation.createRotationX(10));
//				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -1, 20));
//				ObjectFileReader reader = new ObjectFileReader();
//				TriangleMesh mesh;
//				mesh = reader.readFile("objects//triceratops.obj");
//				mesh.setTransformation(meshTransform);
//				mesh.material = phong;
//				shapes.add(mesh);
////				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box1.material = phong;
////				shapes.add(box1);

//				//==== TEAPOT ====//
//				Transformation meshTransform = Transformation.createRotationY(200);
//				meshTransform = meshTransform.append(Transformation.createRotationX(10));
//				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -1, 10));
//				ObjectFileReader reader = new ObjectFileReader();
//				TriangleMesh mesh;
//				mesh = reader.readFile("objects//teapot.obj");
//				mesh.setTransformation(meshTransform);
//				mesh.material = phong;
//				shapes.add(mesh);
////				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
////				box1.material = phong;
////				shapes.add(box1);
				
				//==== TREA BRANCHES ====//
				Transformation meshTransform = Transformation.createRotationY(200);
				meshTransform = meshTransform.append(Transformation.createRotationX(10));
				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, -3, 10));
				ObjectFileReader reader = new ObjectFileReader();
				TriangleMesh mesh;
				mesh = reader.readFile("objects//treebranches.obj");
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

	private  void initializeCamera(int width, int height) {
		camera = new PerspectiveCamera(width, height,
				new Point(), new Vector(0, 0, 1), new Vector(0, 1, 0),90);
	}
	
}
