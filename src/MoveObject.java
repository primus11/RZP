import processing.core.*;


public class MoveObject {
	
	private static final float DEFAULT_IMAGE_SIZE = 50;

	public static final int MOVE_TYPE_BUBBLE     = 1;
	public static final int MOVE_TYPE_STAR       = 2;
	public static final int MOVE_TYPE_BOMB       = 3;
	public static final int MOVE_TYPE_HAMMER     = 4;
	public static final int MOVE_TYPE_SWITCH     = 5;
	public static final int MOVE_TYPE_ROBOT_1    = 6;
	public static final int MOVE_TYPE_ROBOT_2    = 7;
	public static final int MOVE_TYPE_MONSTER    = 8;
	public static final int MOVE_TYPE_HELICOPTER = 9;
	public static final int MOVE_TYPE_FROG       = 10;
	public static final int MOVE_TYPE_JUMP       = 11;
	public static final int MOVE_TYPE_CHUCK      = 12;
	public static final int MOVE_TYPE_UFO        = 13;
	public static final int MOVE_TYPE_NOISE      = 14;
	public static final int MOVE_TYPE_BALL       = 15;
	public static final int MOVE_TYPE_ELECTRIC   = 16;
	public static final int MOVE_TYPE_BEAM       = 17;
	
		
	protected float currentX, currentY;
	protected float length;
	protected float imgWidth, imgHeight;
	protected boolean scaling;
	
	protected long startTime;
	protected boolean timing;
	
	//helicopter move
	protected float direction;
	
	protected int moveType;
	protected PImage image;
	protected PApplet parent;
	
	public boolean enabled;
	
	public MoveObject(PApplet parent, PImage image, int moveType, float length) {
		this.parent = parent;
		this.image = image;
		this.length = length;
		this.moveType = moveType;
		
		this.direction = 1;
		
		this.currentX = 0;
		this.currentY = 0;
		
		this.enabled = false;
		this.timing = false;
		this.scaling = false;
		
		this.setImgSize();
	}
	
	private void setImgSize() {
		this.imgWidth = DEFAULT_IMAGE_SIZE;
		this.imgHeight = DEFAULT_IMAGE_SIZE;
		
		switch (this.moveType) {
			case MOVE_TYPE_BOMB:
			case MOVE_TYPE_STAR:
			case MOVE_TYPE_BUBBLE:
			case MOVE_TYPE_ROBOT_1:
				this.scaling = true;			
				break;
			case MOVE_TYPE_BEAM:
			case MOVE_TYPE_ELECTRIC:
				this.imgWidth = this.parent.width;
				this.imgHeight = this.parent.height;
				break;
			case MOVE_TYPE_CHUCK:
				this.imgWidth = (float) 1.8 * DEFAULT_IMAGE_SIZE;
				this.imgHeight = (float) 1.8 * DEFAULT_IMAGE_SIZE;
				break;
			case MOVE_TYPE_BALL:
				this.imgWidth = (float) 0.6 * DEFAULT_IMAGE_SIZE;
				this.imgHeight = (float) 0.6 * DEFAULT_IMAGE_SIZE;
				break;
			default:
				break;
		}
	}
	
	private float calcTimeRelative() {
		long diff = System.currentTimeMillis() - this.startTime; 
		return (float) PApplet.abs(diff) / (this.length * 1000);
	}
	
	private void moveBottomTrack() {
		this.currentX = this.calcTimeRelative() * this.parent.width;
		//this.currentX += (this.parent.width / this.length);
		this.currentY = this.parent.height - this.imgHeight;
		
		if (this.currentX > this.parent.width + this.imgWidth) {
			this.enabled = false;
		}	
	}
	
	private void moveMiddleTrack() {
		this.currentX = this.calcTimeRelative() * this.parent.width;
		//this.currentX += (this.parent.width / this.length);
		this.currentY = this.parent.height/2 - this.imgHeight/2;
		
		if (this.currentX > this.parent.width + this.imgWidth) {
			this.enabled = false;
		}
	}
	
	private void moveSinusoid() {
		this.currentX = this.calcTimeRelative() * 360;
		//this.currentX += (360 / this.length); //how many degrees add per move
		this.currentY = this.parent.height - PApplet.abs(PApplet.sin(PApplet.radians(this.currentX)) * 75) - this.imgHeight;
		
		if (this.currentX > this.parent.width + this.imgWidth) {
			this.enabled = false;
		}
	}
	
	private void moveRandomSinusoid() {
		this.currentX = this.calcTimeRelative() * this.parent.width;
		float angle = this.calcTimeRelative() * 720;
		//this.currentX += (180 / this.length); //how many degrees add per move
		this.currentY = this.parent.height/2 - (PApplet.sin(PApplet.radians(angle))*20) - 70;
		
		if (this.scaling) {
			this.imgWidth = (float) (0.6 * this.currentY);
			this.imgHeight = this.imgWidth;
		}
		
		if (this.currentX > this.parent.width + this.imgWidth) {
			this.enabled = false;
		}
	}
	
	private void moveRotation() {
		float angle = PApplet.PI/2 * this.calcTimeRelative();
		if ((this.direction == 1) && (angle > PApplet.PI/4)) {
			this.direction = -1;
		}
		
		this.parent.pushMatrix();
		this.parent.rotate(PApplet.radians(angle));
		this.parent.image(this.image, this.parent.width/2, this.parent.height/2, this.imgWidth, this.imgHeight);
		this.parent.popMatrix();
	}
	
	private void moveMiddleSinusoid() {
		this.currentX = this.calcTimeRelative() * this.parent.width;
		float angle = this.calcTimeRelative() * 180;
		//this.currentX += (180 / this.length); //how many degrees add per move
		this.currentY = this.parent.height/2 - (PApplet.sin(PApplet.radians(angle))*35);
		
		if (this.currentX > this.parent.width + this.imgWidth) {
			this.enabled = false;
		}
	}
	
	private void moveHelicopter() {
		this.currentX = this.parent.width/2 - this.imgWidth/2;
		
		if ((this.direction == 1) && (this.currentY > this.parent.height/2 - this.imgHeight/2)) {
			this.direction = -1;
		}
		this.currentY = this.calcTimeRelative() * this.parent.height * this.direction;
		//this.currentY += (this.parent.height / this.length) * this.direction;
		
		if (this.currentY < -this.imgHeight) {
			this.enabled = false;
		}
	}

	public void move() {
		if (this.enabled) {
			if (!this.timing) {
				this.startTime = System.currentTimeMillis();
				this.timing = true;
			}
			
			//System.out.println("MOVE TYPE: " + this.moveType);
			
			switch (this.moveType) {
				case MOVE_TYPE_BALL:
				case MOVE_TYPE_FROG:
				case MOVE_TYPE_JUMP:
					this.moveSinusoid();
					break;
				case MOVE_TYPE_BEAM:
				case MOVE_TYPE_ELECTRIC:
					//nothing to move, only display
					break;
				case MOVE_TYPE_BOMB:
				case MOVE_TYPE_BUBBLE:
				case MOVE_TYPE_ROBOT_1:
				case MOVE_TYPE_STAR:
					this.moveRandomSinusoid();
					break;
				case MOVE_TYPE_CHUCK:
				case MOVE_TYPE_NOISE:
					this.moveMiddleTrack();
					break;
				case MOVE_TYPE_HAMMER:
					//this.moveRotation();
					break;
				case MOVE_TYPE_HELICOPTER:
				case MOVE_TYPE_UFO:
					this.moveMiddleSinusoid();
					break;
				case MOVE_TYPE_MONSTER:
				case MOVE_TYPE_ROBOT_2:
				case MOVE_TYPE_SWITCH:
					this.moveBottomTrack();
					break;
				default:
					this.moveBottomTrack();
					break;
			}
		}
	}
	
	public void display() {
		if (this.enabled) {
			/*
			if (this.scaling) {
				this.direction = (Math.random() > 0.5 ? -this.direction : this.direction);
				
				float scaleFactor = (float) (0.5 * this.direction);
				this.imgWidth += this.imgWidth * scaleFactor;
				this.imgHeight += this.imgHeight * scaleFactor;
				
				this.imgWidth = Math.max(Math.min(this.imgWidth, 100), 20);
				this.imgHeight = Math.max(Math.min(this.imgHeight, 100), 20);
			}
			*/
			this.parent.pushMatrix();
			this.parent.image(this.image, this.currentX, this.currentY, this.imgWidth, this.imgHeight);
			this.parent.popMatrix();
		}
	}

}
