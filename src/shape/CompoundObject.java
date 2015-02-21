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
	
	public Transformation transformation;
	public static final double kEpsilon = 0;
	public RGBColor color;
	public List<Shape> shapes;
	public Material material;

	public CompoundObject(Transformation transformation){
		if (transformation == null)
			throw new NullPointerException("the given transformation is null!");
		shapes = new ArrayList<Shape>();
	}
	

	@Override
	public boolean intersect(Ray ray, ShadeRec sr) {
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
		shapes.add(shape);
	}
}
