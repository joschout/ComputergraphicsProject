package shape;

import java.util.ArrayList;
import java.util.List;

//import util.RGBColor;
//import util.ShadeRec;
//import material.Material;
import math.Point;
//import math.Ray;
import math.Transformation;
import math.Vector;

public class TriangleMesh extends CompoundObject{

	// hb p 477

//	public Transformation transformation;
//	public static final double kEpsilon = 0;
//	public RGBColor color;
//	public Material material;

	public List<Point> vertices;
	public List<Vector> normals;
	public List<Point> textureCoordinates;
//	public List<MeshTriangle> triangles;

	public TriangleMesh(Transformation transformation){
//		if (transformation == null)
//			throw new NullPointerException("the given transformation is null!");
		super(transformation);
		vertices = new ArrayList<Point>();
		normals = new ArrayList<Vector>();
		textureCoordinates = new ArrayList<Point>();
//		triangles = new ArrayList<MeshTriangle>();
	}

	public void addVertex(Point vertex){
		vertices.add(vertex);
	}

	public void addNormal(Vector normal){
		normals.add(normal);
	}
	
	public void addTextureCoordinates(Point texCoords){
		textureCoordinates.add(texCoords);
	}
	public void addTriangle(MeshTriangle triangle){
		//triangles.add(triangle);
		super.shapes.add(triangle);
	}

//	public boolean intersect(Ray ray, ShadeRec sr) {
//		double tmin = Double.MAX_VALUE;
//		Vector normal = null;
//		Point localHitPoint = null;
//		//Shape hittedShape = null;
//
//		for(MeshTriangle triangle: triangles){
//			if(triangle.intersect(ray, sr)){
//				if(sr.t < tmin){
//					//hittedShape = shape;
//					sr.hasHitAnObject = true;
//					sr.ray = ray;
//					sr.material = this.material;	
//					sr.hitPoint = ray.origin.add(ray.direction.scale(sr.t));
//
//					//deze drie worden door de intersect functie van een shape aangepast
//					// en moeten we tijdelijk lokaal opslaan
//					tmin = sr.t;
//					normal = sr.normal;
//					localHitPoint = sr.localHitPoint;	
//				}
//			}
//		}
//		if(sr.hasHitAnObject){
//			sr.t = tmin;
//			sr.normal = normal;
//			sr.localHitPoint = localHitPoint;
//			return true;
//		}
//		return false;
//	}
//
//	@Override
//	public boolean intersect(Ray ray) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Material getMaterial() {
//		return this.material;
//	}
//
//	@Override
//	public RGBColor getColor() {
//		return this.color;
//	}

}
