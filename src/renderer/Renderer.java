package renderer;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.World;
import math.Ray;
import rayTracers.BVHFalseColorImageTracer;
import rayTracers.Tracer;
import sampling.Sample;
import sampling.SquareSampleFactory;
import util.RGBColor;
import gui.ImagePanel;
import gui.ProgressReporter;
import gui.RenderFrame;

public abstract class Renderer {
	
	public int imageWidth;
	public int imageHeight;
	public ProgressReporter reporter;
	public ImagePanel panel;
	
	public Renderer(int imageWidth, int imageHeight){
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		
		// initialize the graphical user interface
		panel = new ImagePanel(imageWidth, imageHeight);
		RenderFrame frame = new RenderFrame("Renderer", panel);
		// initialize the progress reporter
		reporter = new ProgressReporter("Rendering", 40, imageWidth
						* imageHeight, false);
		reporter.addProgressListener(frame);	
	}
	
	//public abstract void render(World world, Tracer tracer, SampleFactory sampleFactory);
	public abstract void render(World world, Tracer tracer);
	
	public void falseColorRender(World world, Tracer tracer){
		
		render(world, tracer);
		
		int maxBVHCount = world.maxBVHCounter;
	
		// initialize the graphical user interface
		ImagePanel panel2 = new ImagePanel(imageWidth, imageHeight);
		RenderFrame frame2 = new RenderFrame("False Color Image", panel2);

		// initialize the progress reporter
		ProgressReporter reporter2 = new ProgressReporter("Rendering", 40, imageWidth
				* imageHeight, false);
		reporter2.addProgressListener(frame2);
		reporter2.setQuiet(true);
		
		BVHFalseColorImageTracer tracer2 = new BVHFalseColorImageTracer(world);
		
		BVHFalseColorImageTracer.setNrOfIntersectionsClamper(maxBVHCount);
		
		// render the scene
				for (int x = 0; x < imageWidth; ++x) {
					for (int y = 0; y < imageHeight; ++y) {
						// create a ray through the center of the pixel.
						Ray ray = world.camera.generateRay(new Sample(x + 0.5, y + 0.5));
						RGBColor color = tracer2.traceRay(ray).gammaCorrect();
						panel2.set(x, y, color.convertToColor());	
					}
					reporter2.update(imageHeight);
				}
				reporter2.done();

				// save the output
				try {
					ImageIO.write(panel2.getImage(), "png", new File("outputFalseColor.png"));
				} catch (IOException e) {
				}	
		
	}
}
