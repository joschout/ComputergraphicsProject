//package boundingVolumeHierarchy;
//
//import java.util.Comparator;
//
//public class AABBoxComparator implements Comparator<CompositeAABBox> {
//
//	private int coordinateToCompare;
//	
//	public AABBoxComparator(int i){
//		if(! (i == 1 || i == 2 || i == 3)){
//			throw new  IllegalArgumentException(" The given int should be equal to 1, 2 or 3");
//		}
//		coordinateToCompare = i;
//	}
//	
//	@Override
//	public int compare(CompositeAABBox box1, CompositeAABBox box2) {
//		switch (coordinateToCompare) {
//		case 1:
//			return compareX(box1, box2);
//		case 2:
//			return compareY(box1, box2);
//		case 3:
//			return compareZ(box1, box2);
//		default:
//			return -2;
//		}
//		
//		
//		
//	}
//
//	private int compareZ(CompositeAABBox box1, CompositeAABBox box2) {
//		if(box1.getP0().z < box2.getP0().z){
//			return -1;
//		}
//		if(box1.getP0().z > box2.getP0().z){
//			return 1;
//		}
//		
//		return 0;
//	}
//
//	private int compareY(CompositeAABBox box1, CompositeAABBox box2) {
//		if(box1.getP0().y < box2.getP0().y){
//			return -1;
//		}
//		if(box1.getP0().y > box2.getP0().y){
//			return 1;
//		}
//		
//		return 0;
//	}
//
//	private int compareX(CompositeAABBox box1, CompositeAABBox box2) {
//		if(box1.getP0().x < box2.getP0().x){
//			return -1;
//		}
//		if(box1.getP0().x > box2.getP0().x){
//			return 1;
//		}
//		
//		return 0;
//	}
//
//}
