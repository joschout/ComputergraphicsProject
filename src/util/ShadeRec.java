package util;

import java.awt.Color;

import rayTracers.Tracer;
import main.World;
import material.Material;
import math.Point;
import math.Ray;
import math.Vector;

public class ShadeRec {
	
	public boolean hasHitAnObject; // has the ray hit an object?
	public Material material;
	public Point hitPoint; // world coordinates of hit point on the transformed object
	public Point localHitPoint; // world coordinates of hit point of the untransformed object
	public Vector normal; //normal at hit point
	public Color color;
	public World world; // world reference for shading
	public Ray ray; //for specular highlights
	public Vector direction; //for area light
	public int depth; // recursion depth
	public double t;
	public double tbox;
	public double u;
	public double v;
	public int bvhCounter;
	public Tracer tracer;
	
	public ShadeRec(World world){
		hasHitAnObject = false;
		localHitPoint = null;
		normal = null;
		color = Color.BLACK;
		this.world = world;
		t = Double.MAX_VALUE;
		bvhCounter = 0;
		tbox = Double.MAX_VALUE;
		tracer = null;
		depth = 0;
	}
	
	public ShadeRec(ShadeRec sr){
		this.hasHitAnObject = sr.hasHitAnObject;
		this.material = sr.material;
		this.localHitPoint = new Point(sr.localHitPoint);
		this.normal = new Vector(sr.normal);
		this.color = new Color(sr.color.getAlpha(), sr.color.getRed(),sr.color.getGreen(),sr.color.getBlue());
		this.ray = new Ray(sr.ray);
		this.direction = new Vector(sr.direction);
		this.depth = sr.depth;
		this.u = sr.u;
		this.v = sr.v;
		this.bvhCounter = sr.bvhCounter;
		this.tbox = sr.tbox;
		this.tracer = sr.tracer;
	}
}
