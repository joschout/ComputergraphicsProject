package main;

import gui.ImagePanel;
import gui.ProgressReporter;
import gui.RenderFrame;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import math.Ray;
import rayTracers.BVHFalseColorImageTracer;
import rayTracers.Tracer;
import renderer.Renderer;
import renderer.SimpleRenderer;
import sampling.Sample;
import util.RGBColor;


/**
 * Entry point of your renderer.
 * 
 * @author Niels Billen
 * @version 1.0
 */
public class Main {



	/**
	 * Entry point of your renderer.
	 * 
	 * @param arguments
	 *            command line arguments.
	 */
	public static void main(String[] arguments) {

		/*
		 * imageResolution[0] = width : the amount of pixels in the horizontal direction
		 * imageResolution[1] = height : the amount of pixels in the vertical direction
		 */
		int[] imageResolution  = new int[2];
		imageResolution[0] = 640;
		imageResolution[1] = 640;

		parseArgs(arguments, imageResolution);
		// validate the input
		validateInput(imageResolution);

//		// initialize the graphical user interface
//		ImagePanel panel = new ImagePanel(imageResolution[0], imageResolution[1]);
//		RenderFrame frame = new RenderFrame("Renderer", panel);
//
//		// initialize the progress reporter
//		ProgressReporter reporter = new ProgressReporter("Rendering", 40, imageResolution[0]
//				* imageResolution[1], false);
//		reporter.addProgressListener(frame);

		World world = new World();
		world.build(imageResolution[0], imageResolution[1]);

		//Tracer tracer = new MultipleObjectsTracer(world);
		//Tracer tracer = new DepthTracer(world);
		//Tracer tracer = new NormalFalseColorImagetracer(world);
		//Tracer tracer = new NormalBVHTracer(world);
		//Tracer tracer = new BVHTracer(world);
		Tracer tracer = new BVHFalseColorImageTracer(world);
		//Tracer tracer = new BVHFalseColorGrayTracer(world);
		
		Renderer renderer = new SimpleRenderer(imageResolution[0], imageResolution[1]);
		renderer.render(world, tracer);
		
//		// render the scene
//		for (int x = 0; x < imageResolution[0]; ++x) {
//			for (int y = 0; y < imageResolution[1]; ++y) {
//				// create a ray through the center of the pixel.
//				Ray ray = world.camera.generateRay(new Sample(x + 0.5, y + 0.5));
//
//				RGBColor color = world.tracer.traceRay(ray);
//
//
//				panel.set(x, y, color.convertToColor());	
//			}
//			reporter.update(imageResolution[1]);
//		}
//		reporter.done();
//
//		System.out.println("Max number of intersections per pixel : " +world.maxBVHCounter);
//
//		// save the output
//		try {
//			ImageIO.write(panel.getImage(), "png", new File("output.png"));
//		} catch (IOException e) {
//		}
	}

	private static void parseArgs(String[] arguments, int[] imageResolution){
		// parse the command line arguments
		for (int i = 0; i < arguments.length; ++i) {
			if (arguments[i].startsWith("-")) {
				try {
					if (arguments[i].equals("-width"))
						imageResolution[0] = Integer.parseInt(arguments[++i]);
					else if (arguments[i].equals("-height"))
						imageResolution[1] = Integer.parseInt(arguments[++i]);
					else if (arguments[i].equals("-help")) {
						System.out.println("usage: "
								+ "[-width  width of the image] "
								+ "[-height  height of the image]");
						return;
					} else {
						System.err.format("unknown flag \"%s\" encountered!\n",
								arguments[i]);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.format("could not find a value for "
							+ "flag \"%s\"\n!", arguments[i]);
				}
			} else
				System.err.format("unknown value \"%s\" encountered! "
						+ "This will be skipped!\n", arguments[i]);
		}
	}

	private static void validateInput(int[] imageResolution ) {
		if (imageResolution[0]  <= 0)
			throw new IllegalArgumentException("the given width cannot be "
					+ "smaller than or equal to zero!");
		if (imageResolution[1]  <= 0)
			throw new IllegalArgumentException("the given height cannot be "
					+ "smaller than or equal to zero!");
	}
}


