//package boundingVolumeHierarchy;
//
//import java.util.Comparator;
//import java.util.List;
//
//import math.Point;
//
//public class AABBComparator3 implements Comparator<CompositeAABBox> {
//	public static final double kEpsilon = 1e-5;
//	
//	/**
//	 * 1 = x
//	 * 2 = y
//	 * 3 = z
//	 */
//	public int coordinateToCompare;
//
//
//	public AABBComparator3() {
//		coordinateToCompare = 1;
//	}
//
////	public void instantiateComparingAxis(List<CompositeAABBox> boxes){
////		double pXMin = Double.POSITIVE_INFINITY;
////		double pYMin = Double.POSITIVE_INFINITY;
////		double pZMin = Double.POSITIVE_INFINITY;
////
////		double pXMax = Double.NEGATIVE_INFINITY;
////		double pYMax = Double.NEGATIVE_INFINITY;
////		double pZMax= Double.NEGATIVE_INFINITY;
////
////		//zoek de min en max waarden in de 3 richtingen van de middelpunten
////		for(CompositeAABBox box: boxes){
////			Point midpointOfBox = box.getMidpoint();
////
////			if(midpointOfBox.x < pXMin){
////				pXMin = midpointOfBox.x;
////			}
////			if(midpointOfBox.x > pXMax){
////				pXMax = midpointOfBox.x;
////			}
////
////			if(midpointOfBox.y < pYMin){
////				pYMin = midpointOfBox.y;
////			}
////			if(midpointOfBox.y > pYMax){
////				pYMax = midpointOfBox.y;
////			}	
////
////			if(midpointOfBox.z < pZMin){
////				pZMin = midpointOfBox.z;
////			}
////			if(midpointOfBox.z > pZMax){
////				pZMax = midpointOfBox.z;
////			}		
////		}
////
////		double xRange = Math.abs(pXMax-pXMin);
////		double yRange = Math.abs(pYMax-pYMin);
////		double zRange = Math.abs(pZMax-pZMin);
////
////
////		if( xRange > yRange && xRange > zRange){
////			coordinateToCompare = 1;
////		}
////		if( yRange > xRange && yRange > zRange){
////			coordinateToCompare = 2;
////		}
////		if( zRange > xRange && zRange > yRange){
////			coordinateToCompare = 3;
////		}
////
////
////	}
//
//
//	@Override
//	public int compare(CompositeAABBox box1, CompositeAABBox box2) {
//		switch (this.coordinateToCompare) {
//		case 1:
//			return compareX(box1, box2);
//		case 2:
//			return compareY(box1, box2);
//		case 3:
//			return compareZ(box1, box2);
//		default:
//			return -2;
//		}
//	}
//
//	private int compareZ(CompositeAABBox box1, CompositeAABBox box2) {
//		if(box1.getMidpoint().z < box2.getMidpoint().z){
//			return -1;
//		}
//		if(box1.getMidpoint().z > box2.getMidpoint().z){
//			return 1;
//		}
//
//		return 0;
//	}
//
//	private int compareY(CompositeAABBox box1, CompositeAABBox box2) {
//		if(box1.getMidpoint().y < box2.getMidpoint().y){
//			return -1;
//		}
//		if(box1.getMidpoint().y > box2.getMidpoint().y){
//			return 1;
//		}
//
//		return 0;
//	}
//
//	private int compareX(CompositeAABBox box1, CompositeAABBox box2) {
//		if(box1.getMidpoint().x < box2.getMidpoint().x){
//			return -1;
//		}
//		if(box1.getMidpoint().x > box2.getMidpoint().x){
//			return 1;
//		}
//
//		return 0;
//	}
//	public void instantiateComparingAxis(List<CompositeAABBox> boxes){
//		Point p0 = computeMinCoords(boxes);
//		Point p1 = computeMaxCoords(boxes);
//		double xInterval = p1.x - p0.x;
//		double yInterval = p1.y - p0.y;
//		double zInterval = p1.z- p0.z;
//		double max;
//		if(xInterval > yInterval){
//			coordinateToCompare = 1;
//			max = xInterval;
//		}
//		else{
//			coordinateToCompare = 2;
//			max = yInterval;
//		}
//		if(zInterval > max)
//			coordinateToCompare = 3;
//	}
//
//	private Point computeMinCoords(List<CompositeAABBox> boxes){
//		double p0X = Double.MAX_VALUE;
//		double p0Y = Double.MAX_VALUE;
//		double p0Z = Double.MAX_VALUE;
//
//		for(CompositeAABBox box: boxes){
//			if(box.getP0().x < p0X){
//				p0X = box.getP0().x;
//			}
//			if(box.getP0().y < p0Y){
//				p0Y = box.getP0().y;
//			}
//			if(box.getP0().z < p0Z){
//				p0Z = box.getP0().z;
//			}	
//		}
//		return new Point(p0X - kEpsilon, p0Y -kEpsilon, p0Z -kEpsilon);
//	}
//
//	private Point computeMaxCoords(List<CompositeAABBox> boxes){
//		double p1X = -Double.MAX_VALUE;
//		double p1Y = -Double.MAX_VALUE;
//		double p1Z = -Double.MAX_VALUE;
//
//		for(CompositeAABBox box: boxes){
//			if(box.getP1().x > p1X){
//				p1X = box.getP1().x;
//			}
//			if(box.getP1().y > p1Y){
//				p1Y = box.getP1().y;
//			}
//			if(box.getP1().z > p1Z){
//				p1Z = box.getP1().z;
//			}	
//		}
//		return new Point(p1X + kEpsilon, p1Y + kEpsilon, p1Z + kEpsilon);
//	}
//
//
//
//
//
//}
