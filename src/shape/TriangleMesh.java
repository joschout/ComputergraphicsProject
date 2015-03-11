//package shape;
//
//import java.util.ArrayList;
//import java.util.List;
//
////import util.RGBColor;
////import util.ShadeRec;
////import material.Material;
//import math.Point;
////import math.Ray;
//import math.Transformation;
//import math.Vector;
//
//public class TriangleMesh extends CompoundObject{
//	
//	// hb p 477
//	public List<Point> vertices;
//	public List<Vector> normals;
//	public List<Point> textureCoordinates;
//
//	public TriangleMesh(Transformation transformation){
//		super(transformation);
//		vertices = new ArrayList<Point>();
//		normals = new ArrayList<Vector>();
//		textureCoordinates = new ArrayList<Point>();
//	}
//
//	public void addVertex(Point vertex){
//		vertices.add(vertex);
//	}
//
//	public void addNormal(Vector normal){
//		normals.add(normal);
//	}
//	
//	public void addTextureCoordinates(Point texCoords){
//		textureCoordinates.add(texCoords);
//	}
//	public void addTriangle(MeshTriangle triangle){
//		//triangles.add(triangle);
//		super.addObject(triangle);
//	}
//}
