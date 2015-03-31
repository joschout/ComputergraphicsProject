package renderer;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.World;
import math.Ray;
import rayTracers.Tracer;
import sampling.Sample;
import sampling.SampleFactory;
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
}
