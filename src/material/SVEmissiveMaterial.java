package material;

import math.Vector;
import texture.ConstantColorTexture;
import texture.Texture;
import util.RGBColor;
import util.ShadeRec;

public class SVEmissiveMaterial extends Material {


	//radiance scaling factor
	public double ls;
	
	//emissive texture
	public Texture emissiveTexture;
	
	public SVEmissiveMaterial(double ls, Texture emissiveTexture) {
		super();
		this.ls = ls;
		this.emissiveTexture = emissiveTexture;
	}

	public SVEmissiveMaterial() {
		this(1, new ConstantColorTexture(RGBColor.WHITE));

	}
	
	@Override
	public RGBColor shade(ShadeRec sr) {
//		if (sr.normal.scale(-1).dot(sr.ray.direction) > 0.0) {
			return emissiveTexture.getColor(sr).scale(ls);
//		}
//		else {
//			return RGBColor.BLACK;
//		}
	}

	@Override
	public RGBColor shade2(ShadeRec sr) {
//		if (sr.normal.scale(-1).dot(sr.ray.direction) > 0.0) {
		Vector vector = sr.localHitPoint.toVector3D().normalize();
		sr.localHitPoint = vector.toPoint3D();
			return emissiveTexture.getColor(sr).scale(ls);
//		}
//		else {
//			return RGBColor.BLACK;
//		}
	}

	@Override
	public RGBColor totalBRDF(ShadeRec sr, Vector wo, Vector wi) {
		return RGBColor.BLACK;
	}

	public double getLs() {
		return ls;
	}

	public void setLs(double ls) {
		this.ls = ls;
	}
	
	public Texture getCe() {
		return emissiveTexture;
	}

	public void setCe(Texture texture) {
		this.emissiveTexture = texture;
	}
	public RGBColor getLe(ShadeRec sr){
		return emissiveTexture.getColor(sr).scale(ls);
	}
}
