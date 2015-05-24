package boundingVolumeHierarchy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import shape.Shape;

public class BVHManager {
	
	public BVHManager(){}
	
	public CompositeAABBox getBoundingVolumeHierarchy(List<Shape> shapes){
		List<CompositeAABBox> bboxList = getBoundingBoxList(shapes);
		return getBoundingVolumeHierarchy2(bboxList);
	}
	
	private List<CompositeAABBox> getBoundingBoxList(List<Shape> shapes){
		List<CompositeAABBox> bboxList = new ArrayList<CompositeAABBox>();
		for(Shape shape: shapes){
			bboxList.add(shape.getBoundingVolumeHierarchy());
		}
		return bboxList;
	}

	private CompositeAABBox getBoundingVolumeHierarchy2(List<CompositeAABBox> subList) {
		
		if(subList.size() > 1){
			AABBComparator comparator = new AABBComparator();
			comparator.instantiateComparingAxis(subList);
			Collections.sort(subList, comparator);
	
			CompositeAABBox leftBox = getBoundingVolumeHierarchy2(subList.subList(0, subList.size()/2));
			CompositeAABBox rightBox = getBoundingVolumeHierarchy2(subList.subList(subList.size()/2, subList.size()));
			CompoundAABBox parentBox = new CompoundAABBox();
			parentBox.addObject(leftBox);
			parentBox.addObject(rightBox);
			parentBox.getBoundingBoxFromScratch();
			return parentBox;
			
		}else if (subList.size() == 1){
			return subList.get(0);
		}else {
			CompoundAABBox parentBox = new CompoundAABBox();
			return parentBox;
		}
	}
}
