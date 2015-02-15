package rayTracers;

import java.awt.Color;

import math.Ray;
import shape.World;

public class Tracer {

	protected World world;
	
	public Tracer(){
		world = null;
	}
	
	public Tracer(World world){
		this.world = world;
	}
	
	public Color traceRay(Ray ray){
		return Color.BLACK;
	}
	
}
