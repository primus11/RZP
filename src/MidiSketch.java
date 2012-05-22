import java.util.*;
import processing.core.*;

public class MidiSketch extends PApplet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int currImg = 0;
	private int numImages = 0;
	private PImage[] images;
	
	private MoveObject[] moveObjects;
	
	public void setup() {
		size(300, 300);
		background(255);
		
		frameRate(60);
		noStroke();
		smooth();
	}
	
	public void draw() {
		background(255);
		for(int i=0; i<this.numImages; i++) {
			this.moveObjects[i].move();
			this.moveObjects[i].display();
		}
		
	}
	
	public void initImages(ArrayList<String> imgList, ArrayList<Integer> lengths) {
		this.numImages = imgList.size();
		this.images = new PImage[this.numImages];
		this.moveObjects = new MoveObject[this.numImages];
		
		for(int i=0; i<imgList.size(); i++) {
			this.images[i] = loadImage(imgList.get(i).toString());
			this.moveObjects[i] = new MoveObject(this, this.images[i], MoveObject.MOVE_TYPE_ROBOT, lengths.get(i));
		}
	}
	
	public void nextImage() {
		this.currImg = (this.currImg + 1) % this.numImages;
		
		this.moveObjects[this.currImg].enabled = true;
	}

}
