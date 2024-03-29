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
				
				PointLight pl1 = new PointLight(3.0, new RGBColor(1), new  Point(-5,3,0));
				pl1.setCastShadows(true);
				this.addLight(pl1);

				PointLight pl2 = new PointLight(1.0, new RGBColor(1), new  Point(5,3,0));
				pl2.setCastShadows(true);
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

				Plane plane = new Plane(Transformation.createIdentity(), new Point(0,0,0), new Vector(0,1,0));
				plane.material = matte;
				shapes.add(plane);		

				//==== BUNNY ====//
				Transformation meshTransform = Transformation.createRotationY(200);
				meshTransform = meshTransform.append(Transformation.createRotationX(0));
				meshTransform = meshTransform.appendToTheLeft(Transformation.createTranslation(-1, 1, 7));
				ObjectFileReader reader = new ObjectFileReader();
				TriangleMesh mesh;
				mesh = reader.readFile("objects//suzanne.obj");
				mesh.setTransformation(meshTransform);
				mesh.material = phong;
				shapes.add(mesh);
//				Box box1 = Box.boundingBoxtoBox(mesh.getBoundingBox());
//				box1.material = phong;
//				shapes.add(box1);
				
				//====CYLINDER PRIMITIVE ====//
				Transformation cylinderTrans = Transformation.createRotationY(200);
				cylinderTrans = cylinderTrans.append(Transformation.createRotationX(0));
				cylinderTrans = cylinderTrans.appendToTheLeft(Transformation.createTranslation(-6 ,0,7));			
				Cylinder cy = new Cylinder(cylinderTrans, 1, 0, 2 );
				cy.material = phong;
				shapes.add(cy);
				
				// ==== BOX PRIMITIVE ==== ///
				Transformation boxTrans = Transformation.createRotationY(200);
				boxTrans = boxTrans.append(Transformation.createRotationX(0));
				boxTrans = boxTrans.appendToTheLeft(Transformation.createTranslation(6 ,0, 7));
				Box box = new  Box(boxTrans, new Point(), new Point(1,1,1));
				box.material = phong;
				shapes.add(box);



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

				new Point(0,5,0), new Vector(0, -1,3), new Vector(0, 1, 0),90);
				//new Point(), new Vector(0, 0, 1), new Vector(0, 1, 0), 60);
	}
	
}
