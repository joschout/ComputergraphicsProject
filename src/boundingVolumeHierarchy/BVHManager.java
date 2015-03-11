package boundingVolumeHierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shape.Shape;

public class BVHManager {

public  static CompositeAABBox getBoundingVolumeHierarchy(List<Shape> shapes){
		
		List<CompositeAABBox> bboxList = new ArrayList<CompositeAABBox>();
		for(Shape shape: shapes){
			//bboxList.add(shape.getAABoundingBox());
			bboxList.add(shape.getBoundingVolumeHierarchy());
		}
		return split(bboxList, bboxList.size()/2, 1);
	}
	private static CompositeAABBox split(List<CompositeAABBox> bboxList, int split, int coordToSort){
		if( bboxList == null){
			throw new IllegalArgumentException(" The given list is null");
		}
		//=== ALS NOG MAAR ELEMENT OVER, RETURN DAT ELEMENT ====// 
		if(bboxList.size() == 1){
			return bboxList.get(0);
		}
		
		if(! (coordToSort == 1 || coordToSort == 2 || coordToSort == 3)){
			throw new  IllegalArgumentException(" The given int should be equal to 1, 2 or 3");
		}
		if( split > bboxList.size()-1 || split < 0){
			throw new IllegalArgumentException(" The given split should be between 1 and bboxList.size()-1");
		}
	
		//===== SORTEER DE LIJST ====//
		Collections.sort(bboxList, new AABBoxComparator(coordToSort));
		
		//==== COORDINAaT WAARVOLGENS VOLGENDE KEER TE SORTEREN ===///
		int newCoordToSort;
		if(coordToSort == 3){
			newCoordToSort = 1;
		}else{
			newCoordToSort = coordToSort + 1;
		}
		
		
		//RECURSIE OP LINKERHELFT VAN LIJST --> RETURNS CompositeBBox
		List<CompositeAABBox> bboxLeft = new ArrayList<CompositeAABBox>();
		for(int i = 0; i < split; i++){
			bboxLeft.add(bboxList.get(i));
		}
		CompositeAABBox leftBox = split(bboxLeft, bboxLeft.size()/2, newCoordToSort);
		
		//RECURSIE OP RECHTERHELFT VAN LIJST --> RETURNS CompositeBBox
		List<CompositeAABBox> bboxRight = new ArrayList<CompositeAABBox>();
		for(int i = split; i < bboxList.size(); i++){
			bboxRight.add(bboxList.get(i));
		}
		CompositeAABBox rightBox = split(bboxRight, bboxRight.size()/2, newCoordToSort);
		
		//STEEK DE 2 SUBBOXES IN 1 BOX
		CompoundAABBox parentBox = new CompoundAABBox();
		parentBox.addObject(leftBox);
		parentBox.addObject(rightBox);
		parentBox.getBoundingBoxFromScratch();
		return parentBox;
		
	}
}
