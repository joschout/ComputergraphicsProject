package boundingVolumeHierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shape.Shape;

public class BVHManager2 {

	
	public  static CompositeAABBox getBoundingVolumeHierarchy(List<Shape> shapes){
		
		List<CompositeAABBox> bboxList = new ArrayList<CompositeAABBox>();
		for(Shape shape: shapes){
			//bboxList.add(shape.getAABoundingBox());
			bboxList.add(shape.getBoundingVolumeHierarchy());
		}
		AABBComparator2 comparator = new AABBComparator2();
		return split(bboxList, bboxList.size()/2, comparator);
	}



	private static CompositeAABBox split(List<CompositeAABBox> bboxList, int split, AABBComparator2 comparator){
		if( bboxList == null){
			throw new IllegalArgumentException(" The given list is null");
		}
		//=== ALS NOG MAAR ELEMENT OVER, RETURN DAT ELEMENT ====// 
		if(bboxList.size() == 1){
			return bboxList.get(0);
		}
		
		
		comparator.instantiateComparingAxis(bboxList);

		//===== SORTEER DE LIJST ====//
		Collections.sort(bboxList, comparator);
		
		
		
		//RECURSIE OP LINKERHELFT VAN LIJST --> RETURNS CompositeBBox
		List<CompositeAABBox> bboxLeft = new ArrayList<CompositeAABBox>();
		for(int i = 0; i < split; i++){
			bboxLeft.add(bboxList.get(i));
		}
		CompositeAABBox leftBox = split(bboxLeft, bboxLeft.size()/2, comparator);
		
		//RECURSIE OP RECHTERHELFT VAN LIJST --> RETURNS CompositeBBox
		List<CompositeAABBox> bboxRight = new ArrayList<CompositeAABBox>();
		for(int i = split; i < bboxList.size(); i++){
			bboxRight.add(bboxList.get(i));
		}
		CompositeAABBox rightBox = split(bboxRight, bboxRight.size()/2, comparator);
		
		//STEEK DE 2 SUBBOXES IN 1 BOX
		CompoundAABBox parentBox = new CompoundAABBox();
		parentBox.addObject(leftBox);
		parentBox.addObject(rightBox);
		parentBox.getBoundingBoxFromScratch();
		return parentBox;
		
	}
}
