package main;

import gui.ImagePanel;
import gui.ProgressReporter;
import gui.RenderFrame;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import math.Ray;
import rayTracers.BVHFalseColorImageTracer;
import rayTracers.BVHTracer;
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

		World world = new World();
		world.build(imageResolution[0], imageResolution[1]);

		//Tracer tracer = new MultipleObjectsTracer(world);
		//Tracer tracer = new DepthTracer(world);
		//Tracer tracer = new NormalFalseColorImagetracer(world);
		//Tracer tracer = new NormalBVHTracer(world);
		//Tracer tracer = new BVHTracer(world);
		//Tracer tracer = new BVHFalseColorImageTracer(world);
		//Tracer tracer = new BVHFalseColorGrayTracer(world);
		
//		Renderer renderer = new SimpleRenderer(imageResolution[0], imageResolution[1]);
//		renderer.render(world, tracer);
		
		
		BVHTracer tracer = new BVHTracer(world);
		SimpleRenderer renderer = new SimpleRenderer(imageResolution[0], imageResolution[1]);
		renderer.falseColorRender(world, tracer);
		
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


