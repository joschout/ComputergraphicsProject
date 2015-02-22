package shape;

import java.util.ArrayList;
import java.util.List;

import material.Material;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class CompoundObject implements Shape {
	
	private Transformation transformation;
	public static final double kEpsilon = 0;
	public RGBColor color;
	private List<Shape> shapes;
	public Material material;
	public BoundingBox boundingBox;

	public CompoundObject(Transformation transformation){
		if (transformation == null)
			throw new NullPointerException("the given transformation is null!");
		shapes = new ArrayList<Shape>();
		boundingBox = new BoundingBox(
				new Point(Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE),
				new Point(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE),
				transformation);
		setTransformation(transformation);
	}
	

	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		if(boundingBox.intersect(ray, sr) == false){
			//System.out.println("het is false geworden");
			return false;
		}
		
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
					sr.material = this.material;	
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
			return true;
		}
		return false;
	}


	@Override
	public Material getMaterial() {
		return this.material;
	}

	@Override
	public RGBColor getColor() {
		return this.color;
	}

	public void addObject(Shape shape){
//		if(shapes.isEmpty()){
//			shapes.add(shape);
//			boundingBox = shape.getBoundingBox();
//		}else{
//			shapes.add(shape);
//			this.boundingBox = boundingBox.addShape(shape);	
//		}
		shapes.add(shape);
		
		//this.boundingBox = getBoundingBoxFromScratch();
		// getBoundingBoxFromScratch() wordt 1 keer gedaan,
		// op het einde van ObjectFileReader.readFile();
		// (in plaats van het hier bij het toevoegen van elk object te doen
		
	}
	
	public BoundingBox getBoundingBoxFromScratch(){
		Point p0 = computeMinCoords();
		Point p1 = computeMaxCoords();
		return new BoundingBox(p0, p1, transformation);
	}
	
	private Point computeMinCoords(){
		double p0X = Double.MAX_VALUE;
		double p0Y = Double.MAX_VALUE;
		double p0Z = Double.MAX_VALUE;

		BoundingBox temp;
		for(int j = 0; j < shapes.size(); j++){
			temp = shapes.get(j).getBoundingBox();

			if(temp.p0.x < p0X){
				p0X = temp.p0.x;
			}
			if(temp.p0.y < p0Y){
				p0Y = temp.p0.y;
			}
			if(temp.p0.z < p0Z){
				p0Z = temp.p0.z;
			}	
		}

		return new Point(p0X - kEpsilon, p0Y -kEpsilon, p0Z -kEpsilon);
	}

	private Point computeMaxCoords(){
		double p1X = Double.MIN_VALUE;
		double p1Y = Double.MIN_VALUE;
		double p1Z = Double.MIN_VALUE;

		BoundingBox temp;
		for(Shape shape: shapes){
			temp = shape.getBoundingBox();

			if(temp.p1.x > p1X){
				p1X = temp.p1.x;
			}
			if(temp.p1.y > p1Y){
				p1Y = temp.p1.y;
			}
			if(temp.p1.z > p1Z){
				p1Z = temp.p1.z;
			}	
		}

		return new Point(p1X + kEpsilon, p1Y + kEpsilon, p1Z + kEpsilon);
	}


	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}


	@Override
	public void setTransformation(Transformation transformation) {
		this.transformation = transformation;
		for(Shape shape: shapes){
			shape.setTransformation(transformation);
		}
		this.boundingBox = getBoundingBoxFromScratch();
	}
	
	public Transformation getTransformation(){
		return this.transformation;
	}
	
	public void recalculateBoundingBoxFromScratch(){
		this.boundingBox = getBoundingBoxFromScratch();
	}
}
