package boundingVolumeHierarchy;

import java.util.ArrayList;
import java.util.List;

import shape.Shape;
import util.ShadeRec;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;

public class AABBox implements CompositeAABBox{
	public Point p0;
	public Point p1;
	public static final double kEpsilon = 1e-5;
	public Shape shape;
	
	
	public AABBox(){
		p0 = new Point(-1, -1, -1);
		p1 = new Point(1, 1, 1);
		shape = null;
	}
	
	
	public AABBox(Point p0, Point p1, Shape shape){
//		if(p0 == null){
//			throw new NullPointerException("the given point p0 is null");
//		}
		this.p0 = p0;
//		if(p1 == null){
//			throw new NullPointerException("the given point p1 is null");
//		}
//		if(p0.x >= p1.x){
//			throw new IllegalArgumentException("p1X should be bigger than p0X");
//		}
//		if(p0.y >= p1.y){
//			throw new IllegalArgumentException("p1X should be bigger than p0X");
//		}
//		if(p0.z >= p1.z){
//			throw new IllegalArgumentException("p1X should be bigger than p0X");
//		}
		this.p1 = p1;
//		if(shape == null){
//			throw new NullPointerException("the given shape is null");
//		}
		this.shape = shape;
	}
	
	public AABBox(double p0X, double p0Y, double p0Z, double p1X, double p1Y, double p1Z, Shape shape){
		if(p0X >= p1X){
			throw new IllegalArgumentException("p1X should be bigger than p0X");
		}
		if(p0Y >= p1Y){
			throw new IllegalArgumentException("p1X should be bigger than p0X");
		}
		if(p0Z >= p1Z){
			throw new IllegalArgumentException("p1X should be bigger than p0X");
		}
		if(shape == null){
			throw new NullPointerException("the given shape is null");
		}
		this.p0 = new Point(p0X, p0Y, p0Z);
		this.p1 = new Point(p1X, p1Y, p1Z);
		this.shape = shape;
	}
	


	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
		Point rayOrigin = ray.origin;
		Vector rayDirection = ray.direction;
		
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
		
		if(t0 < t1 && t1 > kEpsilon){ //condition for a hit
			return this.shape.intersect(ray, sr);	
		}
		return false;
		
		
		
	}
	
	public AABBox getAABoundingBox() {
		return this;
	}
	
	public boolean isInside(Point p){
			return ((p.x > p0.x && p.x < p1.x) && (p.y > p0.y && p.y < p1.y) && (p.z > p0.z && p.z < p1.z));
	}
	
	
//	public AABoundingBox addShape(Shape shape){
//		
//		BoundingBox bbOfShape = shape.getBoundingBox();
//		
//		Point p0New = computeMinCoords(bbOfShape.p0);
//		Point p1New = computeMaxCoords(bbOfShape.p1);
//		
//		return new AABoundingBox(p0New, p1New, shape);
//	}
//	
//	private Point computeMinCoords(Point p0OfNewShape){
//		double p0XOld = this.p0.x;
//		double p0YOld = this.p0.y;
//		double p0ZOld = this.p0.z;
//
//			if(p0OfNewShape.x < p0XOld){
//				p0XOld = p0OfNewShape.x;
//			}
//			if(p0OfNewShape.y < p0YOld){
//				p0YOld = p0OfNewShape.y;
//			}
//			if(p0OfNewShape.z < p0ZOld){
//				p0ZOld = p0OfNewShape.z;
//			}	
//		return new Point(p0XOld - kEpsilon, p0YOld -kEpsilon, p0ZOld -kEpsilon);
//	}
//
//
//	private Point computeMaxCoords(Point p1OfNewShape){
//		double p1XOld = this.p1.x;
//		double p1YOld = this.p1.y;
//		double p1ZOld = this.p1.z;
//
//			if(p1OfNewShape.x > p1XOld){
//				p1XOld = p1OfNewShape.x;
//			}
//			if(p1OfNewShape.y > p1YOld){
//				p1YOld = p1OfNewShape.y;
//			}
//			if(p1OfNewShape.z > p1ZOld){
//				p1ZOld = p1OfNewShape.z;
//			}	
//		return new Point(p1XOld + kEpsilon, p1YOld + kEpsilon, p1ZOld + kEpsilon);
//	}
	
	/**
	 * Computes the AABoundingBox around a BoundingBox
	 * 
	 * @param boundingBox
	 * @return
	 */
	public static AABBox boundingBoxToAABoundingBox(BoundingBox boundingBox, Shape shape){
		Transformation boundingBoxTransformation = boundingBox.getTransformation();
		
		//p0 should be smaller than p1;
		List<Point> untransformedPoints = new ArrayList<Point>();
		Point p0 = boundingBox.p0;
		Point p1 = boundingBox.p1;
		untransformedPoints.add(p0);
		untransformedPoints.add(p1);
		
		Point pa = new Point(p0.x, p1.y, p0.z);
		Point pb = new Point(p0.x, p1.y, p1.z);
		Point pc = new Point(p0.x, p0.y, p1.z);
		untransformedPoints.add(pa);
		untransformedPoints.add(pb);
		untransformedPoints.add(pc);
		
		Point pd = new Point(p1.x, p0.y, p0.z);
		Point pe = new Point(p1.x, p1.y, p0.z);
		Point pf = new Point(p1.x, p0.y, p1.z);
		untransformedPoints.add(pd);
		untransformedPoints.add(pe);
		untransformedPoints.add(pf);
		
		// calculate the vertices of the transformed bounding box
		List<Point> transformedPoints = new ArrayList<Point>();
		for(Point point: untransformedPoints){
			Point transf = boundingBoxTransformation.transform(point);
			transformedPoints.add(transf);
		}
		
		
		double p0X = Double.MAX_VALUE;
		double p1X = Double.MIN_VALUE;
		for(Point point: transformedPoints){
			double pX = point.x;
			if(pX < p0X){
				p0X = pX;
			}
			if(pX > p1X){
				p1X = pX;
			}
		}
		
		double p0Y = Double.MAX_VALUE;
		double p1Y = Double.MIN_VALUE;
		for(Point point: transformedPoints){
			double pY = point.y;
			if(pY < p0Y){
				p0Y = pY;
			}
			if(pY > p1Y){
				p1Y = pY;
			}
		}
		
		double p0Z = Double.MAX_VALUE;
		double p1Z = Double.MIN_VALUE;
		for(Point point: transformedPoints){
			double pZ = point.z;
			if(pZ < p0Z){
				p0Z = pZ;
			}
			if(pZ > p1Z){
				p1Z = pZ;
			}
		}
		
		return new AABBox(p0X, p0Y, p0Z, p1X, p1Y, p1Z, shape);
	}


	@Override
	public Point getP0() {
		return this.p0;
	}


	@Override
	public Point getP1() {
		return this.p1;
	}

	
}
