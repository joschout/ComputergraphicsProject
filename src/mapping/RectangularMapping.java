//package mapping;
//
//import math.Point;
//import shape.Cylinder;
//import shape.Parallelogram;
//import texture.TexelCoordinates;
//
//public class RectangularMapping extends Mapping {
//
//	
//	private Parallelogram parallelogram;
//	
//	public RectangularMapping(Parallelogram parallelogram) {
//		if (parallelogram == null){
//			throw new IllegalArgumentException("the given parallelogram is null");
//		}
//		this.parallelogram = parallelogram;
//	}
//	
//	public RectangularMapping() {
//		this.parallelogram = new Parallelogram();
//	}
//	
//	
//	
//	
//	@Override
//	public TexelCoordinates getTexelCoordinates(Point localHitpoint, int hres,
//			int vres) {
//		
//		
//		//map to (x,z) element of [-1,1]x[-1,1]
//		
//		
//		
//		
//		//map u and v to the texel coordinates
//		int xp = (int) ((hres - 1) * u);
//		int yp = (int) ((vres - 1) * v);
//		return new TexelCoordinates(xp, yp);
//
//	}
//
//}
