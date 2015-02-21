package shape;

import material.Material;
import math.Matrix;
import math.Point;
import math.Ray;
import math.Vector;
import util.RGBColor;
import util.ShadeRec;

public class MeshTriangle implements Shape{
	
	TriangleMesh mesh;
	
	public int[] vertices = new int[3];

	public int[] normals = new int[3];
	
	public int[] texCoords  = new int[3];
	
	
	
	public MeshTriangle(TriangleMesh mesh) {
		if(mesh == null){
			throw new NullPointerException("the given transformation is null!");
		}
		this.mesh = mesh;
	}



	public boolean intersect(Ray ray, ShadeRec sr) {
		Ray transformed = mesh.transformation.transformInverse(ray);
		
		Point v0 = mesh.vertices.get(vertices[0]);
		Point v1 = mesh.vertices.get(vertices[1]);
		Point v2 =mesh.vertices.get(vertices[2]);
		
		Vector v0Minv1 = v0.subtract(v1);
		Vector v0Minv2 = v0.subtract(v2);
		Vector v0MinRayOrigin = v0.subtract(transformed.origin);
		
		double a = v0Minv1.get(0);
		double b = v0Minv2.get(0);
		double c = transformed.direction.get(0);
		double d = v0MinRayOrigin.get(0);
		
		double e = v0Minv1.get(1);
		double f = v0Minv2.get(1);
		double g = transformed.direction.get(1);
		double h = v0MinRayOrigin.get(1);
		
		double i = v0Minv1.get(2);
		double j = v0Minv2.get(2);
		double k = transformed.direction.get(2);
		double l = v0MinRayOrigin.get(2);
		
		double m = f*k - g*j;
		double n = h*k - g*l;
		double p = f*l - h*j;
		double q = g*i - e*k;
		double s = e*j - f*i;
		
		double inverseDenominator = 1.0 / (a*m + b*q + c*s);
		
		double numeratorBeta =d*m - b*n -c*p;
		double beta = numeratorBeta *  inverseDenominator;
		
		if(beta < 0.0){
			return false;
		}
		
		double r = e*l - h*i;
		double numeratorGamma = a*n + d*q + c*r;
		double gamma = numeratorGamma * inverseDenominator;
		
		if(gamma < 0.0){
			return false;
		}
		
		if(beta + gamma > 1){
			return false;
		}
		
		double numeratorT = a*p - b*r +d*s;
		double t = numeratorT * inverseDenominator;
		if(t < TriangleMesh.kEpsilon){
			return false;
		}
//		//=== flat triangle mesh ===//
//		Vector normal = (v1.subtract(v0)).cross(v2.subtract(v0));
//		normal = normal.normalize();
		
		//=== Phong interpolatie ===//
		Vector normal = interPolateNormal(beta, gamma);		
		
		sr.t = t;
		Matrix transposeOfInverse = mesh.transformation.getInverseTransformationMatrix().transpose();
		Vector transformedNormal = transposeOfInverse.transform(normal);
	
		sr.normal = transformedNormal;
		sr.localHitPoint = transformed.origin.add(transformed.direction.scale(t)) ;
		return true;
	}
	
	/**
	 * Phong interpolatie
	 * @param beta
	 * @param gamma
	 * @return
	 */
	private Vector interPolateNormal(double beta, double gamma){
		Vector normal = mesh.normals.get(normals[0]).scale( 1- beta -gamma)
				.add(mesh.normals.get(normals[1]).scale(beta))
				.add(mesh.normals.get(normals[2]).scale(gamma));
		return normal.normalize();
	}



	@Override
	public boolean intersect(Ray ray) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public Material getMaterial() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public RGBColor getColor() {
		// TODO Auto-generated method stub
		return null;
	}
}
