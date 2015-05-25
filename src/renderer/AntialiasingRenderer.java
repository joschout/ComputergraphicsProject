package renderer;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.World;
import math.Ray;
import rayTracers.Tracer;
import sampling.JitteredSampleFactory;
import sampling.Sample;
import sampling.SquareSampleFactory;
import util.RGBColor;

public class AntialiasingRenderer extends Renderer {
	
	
	public static JitteredSampleFactory sampleFactory;
	
	private int sqrtOfNumberOfRaysPerPixel;

	@Override
	public void render(World world, Tracer tracer) {

		int nbOfRaysPerPixel = sqrtOfNumberOfRaysPerPixel * sqrtOfNumberOfRaysPerPixel;
		sampleFactory = new JitteredSampleFactory(sqrtOfNumberOfRaysPerPixel);
		
		// render the scene
				for (int x = 0; x < imageWidth; ++x) {
					for (int y = 0; y < imageHeight; ++y) {
//						if (x == 245 && y == 375) {
//							System.out.println("dit is de te testen pixel,"
//									+ "zet hier een breakpoint");
//						}
						
						// create a ray through the center of the pixel.
						sampleFactory.reset(x + 0.5, y + 0.5);
						
						RGBColor averageColor = RGBColor.BLACK;
						for(int sampleNb = 0; sampleNb < nbOfRaysPerPixel; sampleNb++){
							//SampleFactory sampleFactory = new SampleFactory();
							Sample sample = sampleFactory.getNextSample();
							Ray ray = world.camera.generateRay(sample);
							RGBColor color = tracer.traceRay(ray);
							averageColor = averageColor.unboundedAdd(color);
						}
						averageColor = averageColor.scale(1.0/nbOfRaysPerPixel).gammaCorrect();
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
