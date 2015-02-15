package shape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import camera.Camera;
import camera.PerspectiveCamera;
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
	public Color backgroundColor;
	public  List<Shape> shapes = new ArrayList<Shape>();
	public List<Light> lights = new ArrayList<Light>();
	public Tracer tracer;
	public Light ambientLight;
	
	
	public World(){
		camera = null;
		backgroundColor = Color.BLACK;
		tracer = null;
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
//				Transformation t2 = Transformation.createTranslation(4, -4, 12);
//				Transformation t3 = Transformation.createTranslation(-4, -4, 12);
//				Transformation t4 = Transformation.createTranslation(4, 4, 12);
//				Transformation t5 = Transformation.createTranslation(-4, 4, 12);
//				
//				Transformation t6 = Transformation.createRotationX(60);
//				Transformation t7 = t1.append(t6);
				
				PointLight pl1 = new PointLight();
				pl1.setLocation(new Point(0,0,-1));
				this.addLight(pl1);
				
				MatteMaterial matte = new MatteMaterial();
				matte.setKa(0.25);
				matte.setKd(0.9);
				matte.setCd(RGBColor.convertToRGBColor(Color.GREEN));
				
				Sphere sp1 = new Sphere(t1, 5);
				sp1.material =  matte;
				shapes.add(sp1);
				
				//shapes.add(new Sphere(t2, 4));
				//shapes.add(new Sphere(t3, 4));
				//shapes.add(new Sphere(t4, 4));
				//shapes.add(new Sphere(t5, 4));
				
				//shapes.add(new Cylinder(t1, 1, 1, -1));
				
				//shapes.add(new Disk(t7, new Point(), 1, new Vector(0, 0, -1)));
				
				//shapes.add(new Triangle(t7, new Point(2,0,0), new Point(-2,0,0), new Point(0,3,0)));
				
				//shapes.add(new Plane(t1, new Point(), new Vector(0,0,-1)));
				
	}

	public ShadeRec hitObjects(Ray ray){
		ShadeRec sr = new ShadeRec(this);
		Vector normal = null;
		Point localHitPoint = null;
		double tmin = Double.MAX_VALUE;
		int nbObjects = shapes.size();
		
		for(int i =0; i < nbObjects; i++){
			if(shapes.get(i).intersect(ray, sr)){
				if(sr.t < tmin){
					sr.hasHitAnObject = true;
					tmin = sr.t;
					normal = sr.normal;
					localHitPoint = sr.localHitPoint;
					sr.material = shapes.get(i).getMaterial();			
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
