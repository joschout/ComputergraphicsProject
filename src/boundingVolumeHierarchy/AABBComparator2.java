package boundingVolumeHierarchy;

import java.util.Comparator;
import java.util.List;

import math.Point;

public class AABBComparator2 implements Comparator<CompositeAABBox> {

	/**
	 * 1 = x
	 * 2 = y
	 * 3 = z
	 */
	public int coordinateToCompare;

	
	public AABBComparator2() {
		coordinateToCompare = 1;
	}
	
	public void instantiateComparingAxis(List<CompositeAABBox> boxes){
		double pXMin = Double.POSITIVE_INFINITY;
		double pYMin = Double.POSITIVE_INFINITY;
		double pZMin = Double.POSITIVE_INFINITY;
		
		double pXMax = Double.NEGATIVE_INFINITY;
		double pYMax = Double.NEGATIVE_INFINITY;
		double pZMax= Double.NEGATIVE_INFINITY;
		
		//zoek de min en max waarden in de 3 richtingen van de middelpunten
		for(CompositeAABBox box: boxes){
			Point midpointOfBox = box.getMidpoint();
			
			if(midpointOfBox.x < pXMin){
				pXMin = midpointOfBox.x;
			}
			if(midpointOfBox.x > pXMax){
				pXMax = midpointOfBox.x;
			}
			
			if(midpointOfBox.y < pYMin){
				pYMin = midpointOfBox.y;
			}
			if(midpointOfBox.y > pYMax){
				pYMax = midpointOfBox.y;
			}	
			
			if(midpointOfBox.z < pZMin){
				pZMin = midpointOfBox.z;
			}
			if(midpointOfBox.z > pZMax){
				pZMax = midpointOfBox.z;
			}		
		}
		
		double xRange = Math.abs(pXMax-pXMin);
		double yRange = Math.abs(pYMax-pYMin);
		double zRange = Math.abs(pZMax-pZMin);
		
		
		if( xRange > yRange && xRange > zRange){
			coordinateToCompare = 1;
		}
		if( yRange > xRange && yRange > zRange){
			coordinateToCompare = 2;
		}
		if( zRange > xRange && zRange > yRange){
			coordinateToCompare = 3;
		}
		
		
	}

	
	@Override
	public int compare(CompositeAABBox box1, CompositeAABBox box2) {
		switch (this.coordinateToCompare) {
		case 1:
			return compareX(box1, box2);
		case 2:
			return compareY(box1, box2);
		case 3:
			return compareZ(box1, box2);
		default:
			return -2;
		}
	}
		
	private int compareZ(CompositeAABBox box1, CompositeAABBox box2) {
		if(box1.getMidpoint().z < box2.getMidpoint().z){
			return -1;
		}
		if(box1.getMidpoint().z > box2.getMidpoint().z){
			return -1;
		}
		
		return 0;
	}

	private int compareY(CompositeAABBox box1, CompositeAABBox box2) {
		if(box1.getMidpoint().y < box2.getMidpoint().y){
			return -1;
		}
		if(box1.getMidpoint().y > box2.getMidpoint().y){
			return -1;
		}
		
		return 0;
	}

	private int compareX(CompositeAABBox box1, CompositeAABBox box2) {
		if(box1.getMidpoint().x < box2.getMidpoint().x){
			return -1;
		}
		if(box1.getMidpoint().x > box2.getMidpoint().x){
			return -1;
		}
		
		return 0;
	}
	

}
