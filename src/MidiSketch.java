import processing.core.*;

public class MidiSketch extends PApplet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setup() {
		size(400, 400);
		background(0);
	}
	
	public void draw() {
		background(0);
	    fill(200);
	    ellipseMode(CENTER);
	    ellipse(mouseX,mouseY,40,40);
	}

}
