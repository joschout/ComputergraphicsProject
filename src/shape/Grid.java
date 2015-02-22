package shape;

import java.util.ArrayList;
import java.util.List;

import util.ShadeRec;
import math.Ray;
import math.Transformation;
import math.Point;
import math.Vector;

public class Grid extends CompoundObject {

	private List<Shape> cells;
	private BoundingBox boundingBoxOfGrid;
	private int nx;
	private int ny;
	private int nz;

	public Grid(Transformation transformation){
		super(transformation);
		boundingBoxOfGrid = null;
		nx = 0;
		ny = 0;
		nz = 0;
	}

	public BoundingBox getBoundingBoxOfGrid(){
		return boundingBoxOfGrid;
	}

	public void setUpCells(){

		// find the minimum and maximum coordinates of the grid
		Point p0 = computeMinCoords();
		Point p1 = computeMaxCoords();
		setBoundingBoxOfGrid(p0,p1);

		// store the min & max coords of the grid in the bounding box of the grid
		setBoundingBoxOfGrid(p0, p1);
		
		// compute the number of cells in the x-, y- and z- directions
		//width of the grid in the x-direction
		double wx = boundingBoxOfGrid.p1.x - boundingBoxOfGrid.p0.x;
		//width of the grid in the y-direction
		double wy = boundingBoxOfGrid.p1.y - boundingBoxOfGrid.p0.y;
		//width of the grid in the z-direction
		double wz = boundingBoxOfGrid.p1.z - boundingBoxOfGrid.p0.z;
		
		//multiplying factor that allows us to vary the number of cells
		double multiplierM = 2.0;
		double inverseOfS = Math.pow(shapes.size()/(wx*wy*wz), 0.33333333);
		nx = (int)(multiplierM*wx*inverseOfS) +1;
		ny = (int)(multiplierM*wy*inverseOfS) +1;
		nz = (int)(multiplierM*wz*inverseOfS) +1;
		
		int numberOfCells = nx*ny*nz;
		List <Integer> counts = new ArrayList<Integer>();
		for(int j = 0; j < numberOfCells; j++){
			cells.add(j, null);
			counts.add(j, 0);
		}
				
		
		for(Shape shape: shapes){
			BoundingBox bbOfObject = shape.getBoundingBox();
			
			Vector bbObjectP0MinP0 = bbOfObject.p0.subtract(p0);
			Vector bbObjectP1MinP0 = bbOfObject.p1.subtract(p0);
			Vector p1MinP0 = p1.subtract(p0);
			
			int ixmin = clamp((int)(bbObjectP0MinP0.x * nx /p1MinP0.x), 0, nx-1);
			int iymin = clamp((int)(bbObjectP0MinP0.y * ny /p1MinP0.y), 0, ny-1);;
			int izmin = clamp((int)(bbObjectP0MinP0.z * nz /p1MinP0.z), 0, nz-1);;
			int ixmax = clamp((int)(bbObjectP1MinP0.x * nx /p1MinP0.x), 0, nx-1);;
			int iymax = clamp((int)(bbObjectP1MinP0.y * ny /p1MinP0.y), 0, ny-1);;
			int izmax = clamp((int)(bbObjectP1MinP0.z * nz /p1MinP0.z), 0, nz-1);;
			
			for(int iz = izmin; iz <= izmax; iz++){
				for(int iy = iymin; iy <= iymax; iy++){
					for(int ix = ixmin; ix <= ixmax; ix++){
						
						int index = ix + nx * iy +nx*ny*iz;
						
						if(counts.get(index) == 0){
							cells.set(index, shape);
							counts.set(index, 1);
						}
						else{
							if(counts.get(index) == 1){
								//create a new compound object
								CompoundObject compoundObject = new CompoundObject(Transformation.createIdentity());
								
								//add the object already in the cell to the compound object
								compoundObject.addObject(cells.get(index));
								
								//add the new object to the compound object
								compoundObject.addObject(shape);
								
								//store the compound object in the current cell
								cells.set(index, compoundObject);
								
								//index = 2
								counts.set(index, 2);
								
							}else { //counts.get(index) >1
								//just add the current object to the compound object
								CompoundObject compoundObject = (CompoundObject) cells.get(index);
								compoundObject.addObject(shape);
								
								counts.set(index, counts.get(index) +1);
							}
						}
					}
				}
			}
		}
		
		counts.clear();
		shapes = new ArrayList<Shape>();
		
	}
	
	private int clamp(int x, int min, int max){
		if(x < min){
			return min;
		} if(x > max){
			return max;
		}
		return x;
	}

	public boolean intersect(Ray ray, ShadeRec sr){
		double ox = ray.origin.x;
		double oy = ray.origin.y;
		double oz = ray.origin.z;
		double dx = ray.direction.x;
		double dy = ray.direction.y;
		double dz = ray.direction.z;

		double x0 = boundingBoxOfGrid.p0.x;
		double y0 = boundingBoxOfGrid.p0.y;
		double z0 = boundingBoxOfGrid.p0.z;
		double x1 = boundingBoxOfGrid.p1.x;
		double y1 = boundingBoxOfGrid.p1.y;
		double z1 = boundingBoxOfGrid.p1.z;
		
		double tx_min, ty_min, tz_min;
		double tx_max, ty_max, tz_max; 
		
		// the following code includes modifications from Shirley and Morley (2003)
		
		double a = 1.0 / dx;
		if (a >= 0) {
			tx_min = (x0 - ox) * a;
			tx_max = (x1 - ox) * a;
		}
		else {
			tx_min = (x1 - ox) * a;
			tx_max = (x0 - ox) * a;
		}
		
		double b = 1.0 / dy;
		if (b >= 0) {
			ty_min = (y0 - oy) * b;
			ty_max = (y1 - oy) * b;
		}
		else {
			ty_min = (y1 - oy) * b;
			ty_max = (y0 - oy) * b;
		}
		
		double c = 1.0 / dz;
		if (c >= 0) {
			tz_min = (z0 - oz) * c;
			tz_max = (z1 - oz) * c;
		}
		else {
			tz_min = (z1 - oz) * c;
			tz_max = (z0 - oz) * c;
		}
		
		double t0, t1;
		
		if (tx_min > ty_min)
			t0 = tx_min;
		else
			t0 = ty_min;
			
		if (tz_min > t0)
			t0 = tz_min;
			
		if (tx_max < ty_max)
			t1 = tx_max;
		else
			t1 = ty_max;
			
		if (tz_max < t1)
			t1 = tz_max;
				
		if (t0 > t1)
			return(false);
		
				
		// initial cell coordinates
		
		int ix, iy, iz;
		
		if (boundingBoxOfGrid.isInside(ray.origin)) {  			// does the ray start inside the grid?
			ix = clamp((int)((ox - x0) * nx / (x1 - x0)), 0, nx - 1);
			iy = clamp((int)((oy - y0) * ny / (y1 - y0)), 0, ny - 1);
			iz = clamp((int)((oz - z0) * nz / (z1 - z0)), 0, nz - 1);
		}
		else {
			Point p = ray.origin.add( ray.direction.scale(t0));  // initial hit point with grid's bounding box
			ix = clamp((int)((p.x - x0) * nx / (x1 - x0)), 0, nx - 1);
			iy = clamp((int)((p.y - y0) * ny / (y1 - y0)), 0, ny - 1);
			iz = clamp((int)((p.z - z0) * nz / (z1 - z0)), 0, nz - 1);
		}
		
		// ray parameter increments per cell in the x, y, and z directions
		
		double dtx = (tx_max - tx_min) / nx;
		double dty = (ty_max - ty_min) / ny;
		double dtz = (tz_max - tz_min) / nz;
			
		double 	tx_next, ty_next, tz_next;
		int 	ix_step, iy_step, iz_step;
		int 	ix_stop, iy_stop, iz_stop;
		
		if (dx > 0) {
			tx_next = tx_min + (ix + 1) * dtx;
			ix_step = +1;
			ix_stop = nx;
		}
		else {
			tx_next = tx_min + (nx - ix) * dtx;
			ix_step = -1;
			ix_stop = -1;
		}
		
		if (dx == 0.0) {
			tx_next = Double.MAX_VALUE;
			ix_step = -1;
			ix_stop = -1;
		}
		
		
		if (dy > 0) {
			ty_next = ty_min + (iy + 1) * dty;
			iy_step = +1;
			iy_stop = ny;
		}
		else {
			ty_next = ty_min + (ny - iy) * dty;
			iy_step = -1;
			iy_stop = -1;
		}
		
		if (dy == 0.0) {
			ty_next = Double.MAX_VALUE;
			iy_step = -1;
			iy_stop = -1;
		}
			
		if (dz > 0) {
			tz_next = tz_min + (iz + 1) * dtz;
			iz_step = +1;
			iz_stop = nz;
		}
		else {
			tz_next = tz_min + (nz - iz) * dtz;
			iz_step = -1;
			iz_stop = -1;
		}
		
		if (dz == 0.0) {
			tz_next = Double.MAX_VALUE;
			iz_step = -1;
			iz_stop = -1;
		}
		
			
		// traverse the grid
		
		while (true) {	
			Shape object = cells.get(ix + nx * iy + nx * ny * iz);
			
			if (tx_next < ty_next && tx_next < tz_next) {
				if (object != null && object.intersect(ray, sr) && sr.t < tx_next) {
					sr.material = object.getMaterial();
					return (true);
				}
				
				tx_next += dtx;
				ix += ix_step;
							
				if (ix == ix_stop)
					return (false);
			} 
			else { 	
				if (ty_next < tz_next) {
					if (object != null && object.intersect(ray, sr) && sr.t < ty_next) {
						sr.material = object.getMaterial();
						return (true);
					}
					
					ty_next += dty;
					iy += iy_step;
									
					if (iy == iy_stop)
						return (false);
			 	}
			 	else {		
					if (object != null && object.intersect(ray, sr) && sr.t <  tz_next) {
						sr.material = object.getMaterial();
						return (true);
					}
					
					tz_next += dtz;
					iz += iz_step;
									
					if (iz == iz_stop)
						return (false);
			 	}
			}
		}
		
		
		
		
		
	}
	
	
	private void setBoundingBoxOfGrid(Point p0, Point p1) {
		this.boundingBoxOfGrid = new BoundingBox(p0, p1);

	}

	private Point computeMinCoords(){
		double p0X = Double.MAX_VALUE;
		double p0Y = Double.MAX_VALUE;
		double p0Z = Double.MAX_VALUE;

		BoundingBox temp;
		for(int j = 0; j < shapes.size(); j++){
			temp = shapes.get(j).getBoundingBox();

			if(temp.p0.x < p0X){
				p0X = temp.p0.x;
			}
			if(temp.p0.y < p0Y){
				p0Y = temp.p0.y;
			}
			if(temp.p0.z < p0Z){
				p0Z = temp.p0.z;
			}	
		}

		return new Point(p0X - kEpsilon, p0Y - kEpsilon, p0Z - kEpsilon);
	}

	private Point computeMaxCoords(){
		double p1X = Double.MAX_VALUE;
		double p1Y = Double.MAX_VALUE;
		double p1Z = Double.MAX_VALUE;

		BoundingBox temp;
		for(Shape shape: shapes){
			temp = shape.getBoundingBox();

			if(temp.p1.x > p1X){
				p1X = temp.p1.x;
			}
			if(temp.p1.y > p1Y){
				p1Y = temp.p1.y;
			}
			if(temp.p1.z > p1Z){
				p1Z = temp.p1.z;
			}	
		}

		return new Point(p1X + kEpsilon, p1Y + kEpsilon, p1Z + kEpsilon);
	}



}
