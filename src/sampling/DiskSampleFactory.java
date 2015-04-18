package sampling;

/**
 * Concentric mapping as done in Chapter 6 p .119
 * 
 * @author Jonas
 *
 */
public class DiskSampleFactory implements SampleFactory{

	
	public DiskSampleFactory(SquareSampleFactory sampleFactory){
		this.sampleFactory = sampleFactory;
	}
	
	public DiskSampleFactory() {
		this.sampleFactory = new RandomSampleFactory(0, 0, 2, 2);
	}
	
	
	public SquareSampleFactory sampleFactory;
	
	@Override
	public Sample getNextSample() {
		
		double radius;
		double phi;
		Sample squareSample = sampleFactory.getNextSample();
			
		if(squareSample.x > -squareSample.y){//sectors 1 and 2
			if(squareSample.x > squareSample.y){// sector 1
				radius = squareSample.x;
				phi = squareSample.y/squareSample.x;
			
			}else {//sector 2
				radius = squareSample.y;
				phi = 2.0 - squareSample.x/squareSample.y;
			}
		}else {//sectors 3 and 4
			if (squareSample.x < squareSample.y) { // sector 3
				radius = - squareSample.y;
				phi = 4.0 + squareSample.y/squareSample.x;
			}else {// sector 4
				radius = - squareSample.y;
				if (squareSample.y != 0.0) {
					phi = 6.0 - squareSample.x/squareSample.y;
				}else {
					phi = 0.0;
				}
			}
		}
		
		phi = phi * Math.PI/4.0;
		
		double diskSampleX = radius * Math.cos(phi);
		double diskSampleY = radius * Math.sin(phi);
		
		return new Sample(diskSampleX, diskSampleY);
	}

}
