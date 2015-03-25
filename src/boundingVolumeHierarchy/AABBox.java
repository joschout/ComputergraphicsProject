package boundingVolumeHierarchy;

import java.util.ArrayList;
import java.util.List;

import shape.Shape;
import util.ShadeRec;
import math.Point;
import math.Ray;
import math.Transformation;

public class AABBox extends CompositeAABBox{
	public Shape shape;
	
	
	public AABBox(){
		p0 = new Point(-1, -1, -1);
		p1 = new Point(1, 1, 1);
		shape = null;
		midpoint = new Point(0,0,0);
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
		calculateMidpoint();
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
		calculateMidpoint();
	}

	@Override
	public boolean intersect(Ray ray, ShadeRec sr){
		sr.bvhCounter ++;
		double thisBoxT = this.getSurroundingBoxIntersectionT(ray);
		if(thisBoxT < kEpsilon ){
			return false;
		}
		if(sr.hasHitAnObject && sr.t <  thisBoxT){
			return false;
		}
		return intersectInside(ray, sr);
	}
	
	public boolean intersectInside(Ray ray, ShadeRec sr){
		return this.shape.intersect(ray, sr);
	}
	
	@Override
	public boolean shadowHit(Ray shadowRay, ShadeRec sr) {
		sr.bvhCounter ++;
		double thisBoxT = this.getSurroundingBoxIntersectionT(shadowRay);
		if(thisBoxT < kEpsilon ){
			return false;
		}
		if(sr.hasHitAnObject && sr.t <  thisBoxT){
			return false;
		}
		return shadowHitInside(shadowRay, sr);
	}
	
	public boolean shadowHitInside(Ray shadowRay, ShadeRec sr){
		return this.shape.shadowHit(shadowRay, sr);
	}


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
		double p1X = -Double.MAX_VALUE;
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
		double p1Y = -Double.MAX_VALUE;
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
		double p1Z = -Double.MAX_VALUE;
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
}
