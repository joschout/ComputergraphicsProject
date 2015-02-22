package shape;

import util.RGBColor;
import util.ShadeRec;
import material.Material;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;

public class BoundingBox implements Shape{
	public Point p0;
	public Point p1;
	public static final double kEpsilon = 0;
	private Transformation transformation;
	
	
	public BoundingBox(Transformation transformation){
		if (transformation == null)
			throw new NullPointerException("the given transformation is null!");
		p0 = new Point(-1, -1, -1);
		p1 = new Point(1, 1, 1);
		setTransformation(transformation);
	}
	
	
	public BoundingBox(Point p0, Point p1,Transformation transformation){
		if(p0 == null){
			throw new NullPointerException("the given point p0 is null");
		}
		this.p0 = p0;
		if(p1 == null){
			throw new NullPointerException("the given point p1 is null");
		}
		this.p1 = p1;
		if (transformation == null)
			throw new NullPointerException("the given transformation is null!");
		setTransformation(transformation);
	}
	
	public BoundingBox(double p0X, double p0Y, double p0Z, double p1X, double p1Y, double p1Z,Transformation transformation){
		this.p0 = new Point(p0X, p0Y, p0Z);
		this.p1 = new Point(p1X, p1Y, p1Z);
		if (transformation == null)
			throw new NullPointerException("the given transformation is null!");
		setTransformation(transformation);
	}
	

	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		Ray transformed = transformation.transformInverse(ray);
		Point rayOrigin = transformed.origin;
		Vector rayDirection = transformed.direction;
		
//		Point rayOrigin = ray.origin;
//		Vector rayDirection = ray.direction;
		
		
		double tx_min, ty_min, tz_min;
		double tx_max, ty_max, tz_max; 

		double a = 1.0 / rayDirection.x;
		if (a >= 0) {
			tx_min = (p0.x - rayOrigin.x) * a;
			tx_max = (p1.x - rayOrigin.x) * a;
		}
		else {
			tx_min = (p1.x - rayOrigin.x) * a;
			tx_max = (p0.x - rayOrigin.x) * a;
		}
		
		double b = 1.0 / rayDirection.y;
		if (b >= 0) {
			ty_min = (p0.y - rayOrigin.y) * b;
			ty_max = (p1.y - rayOrigin.y) * b;
		}
		else {
			ty_min = (p1.y - rayOrigin.y) * b;
			ty_max = (p0.y - rayOrigin.y) * b;
		}
		
		double c = 1.0 / rayDirection.z;
		if (c >= 0) {
			tz_min = (p0.z - rayOrigin.z) * c;
			tz_max = (p1.z - rayOrigin.z) * c;
		}
		else {
			tz_min = (p1.z - rayOrigin.z) * c;
			tz_max = (p0.z - rayOrigin.z) * c;
		}
		
		double t0, t1;
	
		// find largest entering t value
		
		if (tx_min > ty_min){
			t0 = tx_min;
		}else{
			t0 = ty_min;	
		}
		if (tz_min > t0){
			t0 = tz_min;	
		}
		// find smallest exiting t value
			
		if (tx_max < ty_max){
			t1 = tx_max;
		}else{
			t1 = ty_max;
		}	
		if (tz_max < t1){
			t1 = tz_max;
		}	
		return (t0 < t1 && t1 > kEpsilon);
	}
	@Override
	public Material getMaterial() {
		return null;
	}
	@Override
	public RGBColor getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public BoundingBox getBoundingBox() {
		return this;
	}
	
	public boolean isInside(Point p){
			return ((p.x > p0.x && p.x < p1.x) && (p.y > p0.y && p.y < p1.y) && (p.z > p0.z && p.z < p1.z));
	}
	
	
	public BoundingBox addShape(Shape shape){
		
		BoundingBox bbOfShape = shape.getBoundingBox();
		
		Point p0New = computeMinCoords(bbOfShape.p0);
		Point p1New = computeMaxCoords(bbOfShape.p1);
		
		return new BoundingBox(p0New, p1New, this.transformation);
	}
	
	private Point computeMinCoords(Point p0OfNewShape){
		double p0XOld = this.p0.x;
		double p0YOld = this.p0.y;
		double p0ZOld = this.p0.z;

			if(p0OfNewShape.x < p0XOld){
				p0XOld = p0OfNewShape.x;
			}
			if(p0OfNewShape.y < p0YOld){
				p0YOld = p0OfNewShape.y;
			}
			if(p0OfNewShape.z < p0ZOld){
				p0ZOld = p0OfNewShape.z;
			}	
		return new Point(p0XOld - kEpsilon, p0YOld -kEpsilon, p0ZOld -kEpsilon);
	}

	private Point computeMaxCoords(Point p1OfNewShape){
		double p1XOld = this.p1.x;
		double p1YOld = this.p1.y;
		double p1ZOld = this.p1.z;

			if(p1OfNewShape.x > p1XOld){
				p1XOld = p1OfNewShape.x;
			}
			if(p1OfNewShape.y > p1YOld){
				p1YOld = p1OfNewShape.y;
			}
			if(p1OfNewShape.z > p1ZOld){
				p1ZOld = p1OfNewShape.z;
			}	
		return new Point(p1XOld + kEpsilon, p1YOld + kEpsilon, p1ZOld + kEpsilon);
	}


	@Override
	public void setTransformation(Transformation transformation) {
		this.transformation = transformation;
		
	}
	
	public Transformation getTransformation(){
		return transformation;
	}
	
	
}
