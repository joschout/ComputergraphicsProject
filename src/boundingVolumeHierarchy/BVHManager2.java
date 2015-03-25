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

public class BVHManager2 {
	
	public BVHManager2(){
		
	}

	//private final static Logger LOGGER = Logger.getLogger(BVHManager2.class.getName()); 
	private final static String pathName = "BVHManager2Log.txt";

	
	public CompositeAABBox getBoundingVolumeHierarchy(List<Shape> shapes){
		
//		try{
//			String textToPrint = "#### start of getBoundingVolumeHierarchy(List<Shape> shapes) ###" + "\n";
//		
//			appendWriteToFile(pathName, textToPrint);
//		}catch(IOException ex){
//			System.out.println("IOException thrown when trying to write to file");
//		}
		
		List<CompositeAABBox> bboxList = getBoundingBoxList(shapes);
		return getBoundingVolumeHierarchy2(bboxList);
	}
	
	private CompositeAABBox getBoundingVolumeHierarchy2(List<CompositeAABBox> subList) {
		
//		try{
//			String textToPrint = "################ start of getBoundingVolumeHierarchy2(List<CompositeAABBox> subList) ###############" + "\n";
//		
//			appendWriteToFile(pathName, textToPrint);
//		}catch(IOException ex){
//			System.out.println("IOException thrown when trying to write to file");
//		}
//		
		
		
		if(subList.size() > 1){
			AABBComparator2 comparator = new AABBComparator2();
			comparator.instantiateComparingAxis(subList);
			Collections.sort(subList, comparator);
			
			
			
//			try{
//				String textToPrint = "======================== START OF BBOXLIST =========================" + "\n";
//				textToPrint = textToPrint + "sorted by AXIS: " + comparator.coordinateToCompare + "\n";
//				for(CompositeAABBox box: subList){
//					textToPrint = textToPrint + ((CompositeAABBox) box).toString() + "\n";
//				}
//				textToPrint = textToPrint + "======================== END OF BBOXLIST =========================" + "\n";
//				appendWriteToFile(pathName, textToPrint);
//			}catch(IOException ex){
//				System.out.println("IOException thrown when trying to write to file");
//			}
//			
			
			
			
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
			throw new IllegalArgumentException("List of boxes is empty!");
		}
	}


//	public void appendWriteToFile(String fileName, String text) throws IOException{
//		File file = new File(fileName);
//		
//		if (!file.exists()) {
//			file.createNewFile();
//		}
//		
//		BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
//		writer.write(text);
//		writer.close();
//		
//		System.out.println("File written to: " + file.getAbsolutePath().toString());
//	}
	
	
	
	
	
	



	private List<CompositeAABBox> getBoundingBoxList(List<Shape> shapes){
		List<CompositeAABBox> bboxList = new ArrayList<CompositeAABBox>();
		for(Shape shape: shapes){
			//bboxList.add(shape.getAABoundingBox());
			bboxList.add(shape.getBoundingVolumeHierarchy());
		}
		return bboxList;
	}
//
//	private static CompositeAABBox sort(List<CompositeAABBox> bboxList, int split, AABBComparator2 comparator){
//		if( bboxList == null){
//			throw new IllegalArgumentException(" The given list is null");
//		}
//		//=== ALS NOG MAAR ELEMENT OVER, RETURN DAT ELEMENT ====// 
//		if(bboxList.size() == 1){
//			return bboxList.get(0);
//		}
//		
//		
//		comparator.instantiateComparingAxis(bboxList);
//
//		//===== SORTEER DE LIJST ====//
//		Collections.sort(bboxList, comparator);
//		
//		
//		
//		//RECURSIE OP LINKERHELFT VAN LIJST --> RETURNS CompositeBBox
//		List<CompositeAABBox> bboxLeft = new ArrayList<CompositeAABBox>();
//		for(int i = 0; i < split; i++){
//			bboxLeft.add(bboxList.get(i));
//		}
//		CompositeAABBox leftBox = sort(bboxLeft, bboxLeft.size()/2, comparator);
//		
//		//RECURSIE OP RECHTERHELFT VAN LIJST --> RETURNS CompositeBBox
//		List<CompositeAABBox> bboxRight = new ArrayList<CompositeAABBox>();
//		for(int i = split; i < bboxList.size(); i++){
//			bboxRight.add(bboxList.get(i));
//		}
//		CompositeAABBox rightBox = sort(bboxRight, bboxRight.size()/2, comparator);
//		
//		//STEEK DE 2 SUBBOXES IN 1 BOX
//		CompoundAABBox parentBox = new CompoundAABBox();
//		parentBox.addObject(leftBox);
//		parentBox.addObject(rightBox);
//		parentBox.getBoundingBoxFromScratch();
//		return parentBox;
//		
//	}
}
