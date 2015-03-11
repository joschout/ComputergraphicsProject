package shape;

import boundingVolumeHierarchy.AABBox;
import boundingVolumeHierarchy.BoundingBox;
import boundingVolumeHierarchy.CompositeAABBox;
import material.Material;
import math.Matrix;
import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;
import util.ShadeRec;
import util.UVCoordinates;

public class MeshTriangle2 implements Shape{
	
	CompoundObject mesh;
	
	public Point[] vertices = new Point[3];

	public Vector[] normals = new Vector[3];
	
	public UVCoordinates[] uvCoordinates  = new UVCoordinates[3];
	
	public static double boundingBoxDelta = 0.0001;
	
	
	public MeshTriangle2(CompoundObject mesh) {
		if(mesh == null){
			throw new NullPointerException("the given transformation is null!");
		}
		this.mesh = mesh;
	}


	public boolean intersect(Ray ray, ShadeRec sr) {
		Ray transformed = mesh.getTransformation().transformInverse(ray);
		
		Point v0 = vertices[0];
		Point v1 = vertices[1];
		Point v2 = vertices[2];
		
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
		
		
		if(t > CompoundObject.kEpsilon  && sr.t > t){
//		if(t < CompoundObject.kEpsilon){
//			return false;
//		}
//		//=== flat triangle mesh ===//
//		Vector normal = (v1.subtract(v0)).cross(v2.subtract(v0));
//		normal = normal.normalize();
		
			//=== Phong interpolatie ===//
			Vector normal = interPolateNormal(beta, gamma);		
			
			sr.u = interpolateU(beta, gamma);
			sr.v = interpolateV(beta, gamma);
			
		
			sr.t = t;
			Matrix transposeOfInverse = mesh.getTransformation().getInverseTransformationMatrix().transpose();
			Vector transformedNormal = transposeOfInverse.transform(normal);
		
			sr.material = this.getMaterial();
			sr.normal = transformedNormal;
			sr.localHitPoint = transformed.origin.add(transformed.direction.scale(t)) ;
			return true;
		}
		return false;
	}
	

//	public boolean intersect(Ray ray, ShadeRec sr) {
//		Ray transformed = mesh.getTransformation().transformInverse(ray);
//		
//		Point v0 = vertices[0];
//		Point v1 = vertices[1];
//		Point v2 = vertices[2];
//		
//		Vector v0Minv1 = v0.subtract(v1);
//		Vector v0Minv2 = v0.subtract(v2);
//		Vector v0MinRayOrigin = v0.subtract(transformed.origin);
//		
//		double a = v0Minv1.get(0);
//		double b = v0Minv2.get(0);
//		double c = transformed.direction.get(0);
//		double d = v0MinRayOrigin.get(0);
//		
//		double e = v0Minv1.get(1);
//		double f = v0Minv2.get(1);
//		double g = transformed.direction.get(1);
//		double h = v0MinRayOrigin.get(1);
//		
//		double i = v0Minv1.get(2);
//		double j = v0Minv2.get(2);
//		double k = transformed.direction.get(2);
//		double l = v0MinRayOrigin.get(2);
//		
//		double m = f*k - g*j;
//		double n = h*k - g*l;
//		double p = f*l - h*j;
//		double q = g*i - e*k;
//		double s = e*j - f*i;
//		
//		double inverseDenominator = 1.0 / (a*m + b*q + c*s);
//		
//		double numeratorBeta =d*m - b*n -c*p;
//		double beta = numeratorBeta *  inverseDenominator;
//		
//		if(beta < 0.0){
//			return false;
//		}
//		
//		double r = e*l - h*i;
//		double numeratorGamma = a*n + d*q + c*r;
//		double gamma = numeratorGamma * inverseDenominator;
//		
//		if(gamma < 0.0){
//			return false;
//		}
//		
//		if(beta + gamma > 1){
//			return false;
//		}
//		
//		double numeratorT = a*p - b*r +d*s;
//		double t = numeratorT * inverseDenominator;
//		if(t > CompoundObject.kEpsilon  && sr.t > t){
//		
//	//		//=== flat triangle mesh ===//
//	//		Vector normal = (v1.subtract(v0)).cross(v2.subtract(v0));
//	//		normal = normal.normalize();
//			
//			//=== Phong interpolatie ===//
//			Vector normal = interPolateNormal(beta, gamma);		
//			
//			sr.u = interpolateU(beta, gamma);
//			sr.v = interpolateV(beta, gamma);
//			
//			sr.hasHitAnObject = true;
//			sr.ray = ray;
//			sr.hitPoint = ray.origin.add(ray.direction.scale(sr.t));
//			
//			sr.t = t;
//			Matrix transposeOfInverse = mesh.getTransformation().getInverseTransformationMatrix().transpose();
//			Vector transformedNormal = transposeOfInverse.transform(normal);
//		
//			sr.material = this.getMaterial();
//			sr.normal = transformedNormal;
//			sr.localHitPoint = transformed.origin.add(transformed.direction.scale(t)) ;
//			return true;
//		}
//		
//		return false;
//	}
	
	private double interpolateU(double beta, double gamma) {
		double u = uvCoordinates[0].u * (1-beta-gamma) 
				+ uvCoordinates[1].u * beta
				+ uvCoordinates[2].u * gamma;
		return u;
	}



	private double interpolateV(double beta, double gamma) {
		double v = uvCoordinates[0].v * (1-beta-gamma) 
				+ uvCoordinates[1].v * beta
				+ uvCoordinates[2].v * gamma;
		return v;
	}



	/**
	 * Phong interpolatie
	 * @param beta
	 * @param gamma
	 * @return
	 */
	private Vector interPolateNormal(double beta, double gamma){
		Vector normal = normals[0].scale( 1- beta -gamma)
				.add(normals[1].scale(beta))
				.add(normals[2].scale(gamma));
		return normal.normalize();
	}



//	@Override
//	public boolean intersect(Ray ray) {
//		// TODO Auto-generated method stub
//		return false;
//	}



	@Override
	public Material getMaterial() {
		return mesh.getMaterial();
	}


	@Override
	public BoundingBox getBoundingBox() {
		
		Point v0 = vertices[0];
		Point v1 = vertices[1];
		Point v2 = vertices[2];
		
		double p0X = Math.min(Math.min(v0.x, v1.x), v2.x) - boundingBoxDelta;
		double p0Y = Math.min(Math.min(v0.y, v1.y), v2.y) - boundingBoxDelta;
		double p0Z = Math.min(Math.min(v0.z, v1.z), v2.z) - boundingBoxDelta;
		
		double p1X = Math.max(Math.max(v0.x, v1.x), v2.x) + boundingBoxDelta;
		double p1Y = Math.max(Math.max(v0.y, v1.y), v2.y) + boundingBoxDelta;
		double p1Z = Math.max(Math.max(v0.z, v1.z), v2.z) + boundingBoxDelta;
		
		return new BoundingBox(p0X, p0Y, p0Z, p1X, p1Y, p1Z, mesh.getTransformation());
		
	}



	@Override
	public void setTransformation(Transformation transformation) {
	
		
	}





	@Override
	public boolean shadowHit(Ray ray, ShadeRec sr) {
		Ray transformed = mesh.getTransformation().transformInverse(ray);
		
		Point v0 = vertices[0];
		Point v1 = vertices[1];
		Point v2 = vertices[2];
		
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
		if(t < CompoundObject.kEpsilon){
			return false;
		}
		if(Double.isNaN(t)){
			System.out.println("Nanananananananananananananananan");
		}
		
		
		sr.t = t;
//		sr.localHitPoint = transformed.origin.add(transformed.direction.scale(t)) ;
		return true;
	}



	@Override
	public AABBox getAABoundingBox() {
		return AABBox.boundingBoxToAABoundingBox(getBoundingBox(), this);
	}



	@Override
	public CompositeAABBox getBoundingVolumeHierarchy() {
		return getAABoundingBox();
	}

}
