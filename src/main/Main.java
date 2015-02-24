package main;

import gui.ImagePanel;
import gui.ProgressReporter;
import gui.RenderFrame;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import math.Ray;
import sampling.Sample;
import shape.World;
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
		//the amount of pixels in the horizontal direction
		int width = 640;
		//the amount of pixels in the vertical direction
		int height = 640;

		// parse the command line arguments
		for (int i = 0; i < arguments.length; ++i) {
			if (arguments[i].startsWith("-")) {
				try {
					if (arguments[i].equals("-width"))
						width = Integer.parseInt(arguments[++i]);
					else if (arguments[i].equals("-height"))
						height = Integer.parseInt(arguments[++i]);
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

		// validate the input
		validateInput(width, height);

		// initialize the graphical user interface
		ImagePanel panel = new ImagePanel(width, height);
		RenderFrame frame = new RenderFrame("Renderer", panel);

		// initialize the progress reporter
		ProgressReporter reporter = new ProgressReporter("Rendering", 40, width
				* height, false);
		reporter.addProgressListener(frame);

		World world = new World();
		world.build(width, height);

		// render the scene
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// create a ray through the center of the pixel.
				Ray ray = world.camera.generateRay(new Sample(x + 0.5, y + 0.5));

//				Shape hittedShape = null;
//				ShadeRec sr = new ShadeRec(world);
//				for (Shape shape : world.shapes){
//					if (shape.intersect(ray, sr)) {
//						hittedShape = shape;
//						sr.ray = ray;
//						break;
//					}
//				}
//				if( hittedShape  == null){
//					panel.set(x, y, world.backgroundColor);
//				}else{
//					sr.hitPoint = ray.origin.add(ray.direction.scale(sr.t));
//					RGBColor color = hittedShape.getMaterial().shade(sr);
//					panel.set(x, y, color.convertToColor());
//				}
				
				RGBColor color = world.tracer.traceRay(ray);
				panel.set(x, y, color.convertToColor());	
			}
			reporter.update(height);
		}
		reporter.done();

		// save the output
		try {
			ImageIO.write(panel.getImage(), "png", new File("output.png"));
		} catch (IOException e) {
		}
	}


	private static void validateInput(int width, int height) {
		if (width <= 0)
			throw new IllegalArgumentException("the given width cannot be "
					+ "smaller than or equal to zero!");
		if (height <= 0)
			throw new IllegalArgumentException("the given height cannot be "
					+ "smaller than or equal to zero!");
	}
}


