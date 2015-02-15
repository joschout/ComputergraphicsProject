package shape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import camera.Camera;
import camera.PerspectiveCamera;
import rayTracers.MultipleObjectsTracer;
import rayTracers.Tracer;
import light.AmbientLight;
import light.Light;
import light.PointLight;
import material.MatteMaterial;
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
		backgroundColor = new RGBColor(0);
		tracer = new MultipleObjectsTracer(this);
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
				Transformation t1 = Transformation.createTranslation(0, 0, 14);
				Transformation t2 = Transformation.createTranslation(4, -4, 12);
				Transformation t3 = Transformation.createTranslation(-4, -4, 12);
				Transformation t4 = Transformation.createTranslation(4, 4, 12);
				Transformation t5 = Transformation.createTranslation(-4, 4, 12);
				
				Transformation t6 = Transformation.createRotationX(60);
				Transformation t7 = t1.append(t6);
				Transformation t8 = t2.append(t6);
				
				PointLight pl1 = new PointLight();
				pl1.setLocation(new Point(0,0,-1));
				this.addLight(pl1);
				
				MatteMaterial matte = new MatteMaterial();
				matte.setKa(0.25);
				matte.setKd(0.65);
				matte.setCd(RGBColor.convertToRGBColor(Color.GREEN));
				
				Sphere sp1 = new Sphere(t1, 5);
				sp1.material =  matte;
				shapes.add(sp1);
				
				Sphere sp2 = new Sphere(t2, 5);
				sp2.material = matte;
				shapes.add(sp2);
				
				Sphere sp3 = new Sphere(t3, 5);
				sp3.material = matte;
				shapes.add(sp3);
				
				Sphere sp4 = new Sphere(t4, 5);
				sp4.material = matte;
				shapes.add(sp4);
				
				Sphere sp5 = new Sphere(t5, 5);
				sp5.material = matte;
				shapes.add(sp5);
			
				Cylinder cy = new Cylinder(t7, 1, 1, -1);
				cy.material = matte;
				shapes.add(cy);
				
//				Disk disk = new Disk(t8, new Point(), 60, new Vector(0, 0, -1));
//				disk.material = matte;
//				shapes.add(disk);
//				
				
//				Triangle tri = new Triangle(t2, new Point(5,0,0), new Point(-5,0,0), new Point(0,5,0));
//				tri.material = matte;
//				shapes.add(tri);
				
//				Plane pl = new Plane(t1, new Point(), new Vector(0,0,-1));
//				pl.material = matte;
//				shapes.add(pl);
				
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

	
//	public void renderScene(){
//		
//		
//		// render the scene
//				for (int x = 0; x < width; ++x) {
//					for (int y = 0; y < height; ++y) {
//						// create a ray through the center of the pixel.
//						Ray ray = camera.generateRay(new Sample(x + 0.5, y + 0.5));
//						Color pixelColor = tracer.traceRay(ray);
//						
//						
//						panel.set(x, y, pixelColor);
//					}
//					reporter.update(height);
//				}
//				reporter.done();
//	}



	private  void initializeCamera(int width, int height) {
		camera = new PerspectiveCamera(width, height,
				new Point(), new Vector(0, 0, 1), new Vector(0, 1, 0),90);
		
	}


	

	
}
