//package boundingVolumeHierarchy;
//
//
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import shape.Shape;
//import math.Point;
//
//public class BVHManagerCfrJerre {
//
//	public static final double kEpsilon = 1e-5;
//	
//	public static int coordinateToCompare;
//
//	public  static CompositeAABBox getBoundingVolumeHierarchy(List<Shape> shapes){
//	
//			List<CompositeAABBox> bboxList = getBoundingBoxList(shapes);
//			CompositeAABBox comp = constructfiniteBoundingBoxHierarchy(bboxList);
//			return comp;
//	}
//	
//	private static List<CompositeAABBox> getBoundingBoxList(List<Shape> shapes){
//		List<CompositeAABBox> bboxList = new ArrayList<CompositeAABBox>();
//		for(Shape shape: shapes){
//			//bboxList.add(shape.getAABoundingBox());
//			bboxList.add(shape.getBoundingVolumeHierarchy());
//		}
//		return bboxList;
//	}
//	public static CompositeAABBox constructfiniteBoundingBoxHierarchy(List<CompositeAABBox> boxes){
//		if(boxes.size() > 1){
//			switchTurn(boxes);
//			boxes = mergeSort(boxes);
////			List<CompositeAABBox> subBoxes = new ArrayList<BoundingBox>();
////			subBoxes.add(constructBoundingBoxHierarchy(boxes.subList(0, boxes.size()/2)));
////			subBoxes.add(constructBoundingBoxHierarchy(boxes.subList(boxes.size()/2, boxes.size())));
//
//			CompositeAABBox leftBox = constructfiniteBoundingBoxHierarchy(boxes.subList(0, boxes.size()/2));
//			CompositeAABBox rightBox = constructfiniteBoundingBoxHierarchy(boxes.subList(boxes.size()/2, boxes.size()));
//			
//			CompoundAABBox parentBox = new CompoundAABBox();
//			parentBox.addObject(leftBox);
//			parentBox.addObject(rightBox);
//			parentBox.getBoundingBoxFromScratch();
//			return parentBox;
//		}
//		else if(boxes.size() == 0)
//			throw new IllegalArgumentException("List of boxes is empty!");
//		else{
//			return boxes.get(0);
//		}
//	}
//	
//	public static int compare(CompositeAABBox one, CompositeAABBox other) {
//		switch(coordinateToCompare){
//		case 1:
//			return Double.compare(one.p0.x + (one.p1.x - one.p0.x)/2, other.p0.x + (other.p1.x - other.p0.x)/2);
//		case 2:
//			return Double.compare(one.p0.y + (one.p1.y - one.p0.y)/2, other.p0.y + (other.p1.y - other.p0.y)/2);
//		case 3:
//			return Double.compare(one.p0.z + (one.p1.z - one.p0.z)/2, other.p0.z + (other.p1.z - other.p0.z)/2);
//		default:
//			throw new IllegalArgumentException("Axis to sort on not recognized!");
//		}
//	}
//	
//	
//
//
//	
//	public static List<CompositeAABBox> mergeSort(List<CompositeAABBox> unsortedList)
//	{
//		CompositeAABBox[] arrayToBeSorted = unsortedList.toArray(new CompositeAABBox[unsortedList.size()]);
//		CompositeAABBox[] temporaryArray = new CompositeAABBox[arrayToBeSorted.length];
//		mergeSort(arrayToBeSorted, temporaryArray,  0,  arrayToBeSorted.length - 1);
//		return Arrays.asList(arrayToBeSorted);
//	}
//
//
//	private static void mergeSort(CompositeAABBox [] arrayToBeSorted, CompositeAABBox[] temporaryArray, int left, int right)
//	{
//		if( left < right )
//		{
//			int center = (left + right) / 2;
//			mergeSort(arrayToBeSorted, temporaryArray, left, center);
//			mergeSort(arrayToBeSorted, temporaryArray, center + 1, right);
//			merge(arrayToBeSorted, temporaryArray, left, center + 1, right);
//		}
//	}
//
//
//    private static void merge(CompositeAABBox[] arrayToBeSorted, CompositeAABBox[] temporaryArray, int left, int right, int rightEnd )
//    {
//        int leftEnd = right - 1;
//        int k = left;
//        int num = rightEnd - left + 1;
//
//        while(left <= leftEnd && right <= rightEnd)
//            if(compare(arrayToBeSorted[left],(arrayToBeSorted[right])) <= 0)
//            	temporaryArray[k++] = arrayToBeSorted[left++];
//            else
//            	temporaryArray[k++] = arrayToBeSorted[right++];
//
//        while(left <= leftEnd)
//        	temporaryArray[k++] = arrayToBeSorted[left++];
//
//        while(right <= rightEnd)
//        	temporaryArray[k++] = arrayToBeSorted[right++];
//
//        for(int i = 0; i < num; i++, rightEnd--)
//        	arrayToBeSorted[rightEnd] = temporaryArray[rightEnd];
//    }
//    
//	private static void switchTurn(List<CompositeAABBox> boxes){
//		CompoundAABBox superBox = new CompoundAABBox();
//		
//		for(CompositeAABBox box:boxes){
//			superBox.addObject(box);
//		}
//		superBox.getBoundingBoxFromScratch();
//		
//		double xInterval = superBox.getP1().x - superBox.getP0().x;
//		double yInterval = superBox.getP1().y - superBox.getP0().y;
//		double zInterval = superBox.getP1().z - superBox.getP0().z;
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
//	
//}
