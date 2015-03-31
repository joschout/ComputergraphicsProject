package renderer;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.World;
import math.Ray;
import rayTracers.Tracer;
import sampling.JitteredSampleFactory;
import sampling.Sample;
import sampling.SampleFactory;
import util.RGBColor;

public class AntialiasingRenderer extends Renderer {
	
	
	private int sqrtOfNumberOfRaysPerPixel;

	@Override
	public void render(World world, Tracer tracer) {
		
		
		int nbOfRaysPerPixel = sqrtOfNumberOfRaysPerPixel * sqrtOfNumberOfRaysPerPixel;
		// render the scene
				for (int x = 0; x < imageWidth; ++x) {
					for (int y = 0; y < imageHeight; ++y) {
						// create a ray through the center of the pixel.
						JitteredSampleFactory sampleFactory 
							= new JitteredSampleFactory(x + 0.5, y + 0.5, 1, 1, sqrtOfNumberOfRaysPerPixel);
						
						RGBColor averageColor = new RGBColor(0);
						for(int sampleNb = 0; sampleNb <= nbOfRaysPerPixel; sampleNb++){
							//SampleFactory sampleFactory = new SampleFactory();
							Sample sample = sampleFactory.getNextSample();
							Ray ray = world.camera.generateRay(sample);
							RGBColor color = tracer.traceRay(ray);
							averageColor = averageColor.unboundedAdd(color);
						}
						averageColor = averageColor.scale(1.0/nbOfRaysPerPixel);
						panel.set(x, y, averageColor.convertToColor());	
					}
					reporter.update(imageHeight);
				}
				reporter.done();

				System.out.println("Max number of intersections per pixel : " + world.maxBVHCounter);

				// save the output
				try {
					ImageIO.write(panel.getImage(), "png", new File("output.png"));
				} catch (IOException e) {
				}	

	}

	public AntialiasingRenderer(int imageWidth, int imageHeight) {
		super(imageWidth, imageHeight);
		this.sqrtOfNumberOfRaysPerPixel = 2;
	}
	
	
	public AntialiasingRenderer(int imageWidth, int imageHeight,
			int sqrtOfNumberOfRaysPerPixel) {
		super(imageWidth, imageHeight);
		this.sqrtOfNumberOfRaysPerPixel = sqrtOfNumberOfRaysPerPixel;
	}

	public int getSqrtOfNumberOfRaysPerPixel() {
		return sqrtOfNumberOfRaysPerPixel;
	}

	public void setSqrtOfNumberOfRaysPerPixel(int sqrtOfNumberOfRaysPerPixel) {
		this.sqrtOfNumberOfRaysPerPixel = sqrtOfNumberOfRaysPerPixel;
	}

}
