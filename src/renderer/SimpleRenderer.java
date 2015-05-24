package renderer;

import gui.ImagePanel;
import gui.ProgressReporter;
import gui.RenderFrame;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.World;
import math.Ray;
import rayTracers.BVHFalseColorImageTracer;
import rayTracers.BVHTracer;
import rayTracers.Tracer;
import sampling.Sample;
import sampling.SquareSampleFactory;
import sampling.SingleSampleFactory;
import util.RGBColor;

public class SimpleRenderer extends Renderer {

	public SimpleRenderer(int imageWidth, int imageHeight) {
		super(imageWidth, imageHeight);
	}

	
	public void render(World world, Tracer tracer){
		// render the scene
		for (int x = 0; x < imageWidth; ++x) {
			for (int y = 0; y < imageHeight; ++y) {
				// create a ray through the center of the pixel.
				
				SingleSampleFactory sampleFactory = new SingleSampleFactory(x + 0.5, y + 0.5);
				
				Sample sample = sampleFactory.getNextSample();
				
				Ray ray = world.camera.generateRay(sample);
				RGBColor color = tracer.traceRay(ray).gammaCorrect();
				panel.set(x, y, color.convertToColor());	
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
	
	
//	public void falseColorRender(World world, Tracer tracer){
//		
//		render(world, tracer);
//		
//		int maxBVHCount = world.maxBVHCounter;
//	
//		// initialize the graphical user interface
//		ImagePanel panel2 = new ImagePanel(imageWidth, imageHeight);
//		RenderFrame frame2 = new RenderFrame("False Color Image", panel2);
//
//		// initialize the progress reporter
//		ProgressReporter reporter2 = new ProgressReporter("Rendering", 40, imageWidth
//				* imageHeight, false);
//		reporter2.addProgressListener(frame2);
//		
//		BVHFalseColorImageTracer tracer2 = new BVHFalseColorImageTracer(world);
//		
//		BVHFalseColorImageTracer.setNrOfIntersectionsClamper(maxBVHCount);
//		
//		// render the scene
//				for (int x = 0; x < imageWidth; ++x) {
//					for (int y = 0; y < imageHeight; ++y) {
//						// create a ray through the center of the pixel.
//						Ray ray = world.camera.generateRay(new Sample(x + 0.5, y + 0.5));
//						RGBColor color = tracer2.traceRay(ray).gammaCorrect();
//						panel2.set(x, y, color.convertToColor());	
//					}
//					reporter2.update(imageHeight);
//				}
//				reporter2.done();
//
//				// save the output
//				try {
//					ImageIO.write(panel2.getImage(), "png", new File("outputFalseColor.png"));
//				} catch (IOException e) {
//				}	
//		
//	}
}
