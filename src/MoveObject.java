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
	protected float angle;
	
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
		
		this.angle = 0;
		
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
				this.imgWidth = 300;
				this.imgHeight = 300;
				break;
			case MOVE_TYPE_CHUCK:
				this.imgWidth = (float) 2.0 * DEFAULT_IMAGE_SIZE;
				this.imgHeight = (float) 2.0 * DEFAULT_IMAGE_SIZE;
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
			this.imgWidth = (float) (this.currentY > this.parent.height/2 - 70 ? 0.8 * this.currentY : 0.4 * this.currentY);
			this.imgHeight = (float) (this.currentY > this.parent.height/2 - 70 ? 0.8 * this.currentY : 0.4 * this.currentY);
		}
		
		if (this.currentX > this.parent.width + this.imgWidth) {
			this.enabled = false;
		}
	}
	
	private void moveFullScreen() {
		if (this.enabled && (this.calcTimeRelative() > 1.0)) {
			this.enabled = false;
		}
	}
	
	private void moveRotation() {
		float relative = this.calcTimeRelative();		
		this.currentX = this.parent.width/2 - this.imgWidth/2;
		this.currentY = this.parent.height/2 - this.imgHeight/2;
		
		if (relative < 0.5) {
			//moving down
			this.angle = 50 * relative;
			this.currentX += this.imgWidth * relative;
			this.currentY += this.imgHeight * relative;
		} else if (relative > 0.5 && relative < 0.6) {
			//down position
			this.angle = 50;
			this.currentX = this.parent.width/2 + this.imgWidth/2;
			this.currentY = this.parent.height/2 + this.imgHeight/2;
		} else if (this.angle > 50) {
			this.angle = 0;
		} else if (this.angle > 0) {
			this.angle = 360 - this.angle;
		}
		
		if (relative > 1.0) {
			this.enabled = false;
		}
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
					this.moveFullScreen();
					break;
				case MOVE_TYPE_BOMB:
				case MOVE_TYPE_BUBBLE:
				case MOVE_TYPE_ROBOT_1:
				case MOVE_TYPE_STAR:
					this.moveRandomSinusoid();
					break;
				case MOVE_TYPE_NOISE:
					this.moveMiddleTrack();
					break;
				case MOVE_TYPE_HAMMER:
					this.moveRotation();
					break;
				case MOVE_TYPE_CHUCK:
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
			System.out.println("ANGLE: " + this.angle);
			System.out.println("CURRENT X: " + this.currentX);
			System.out.println("CURRENT Y: " + this.currentY);
			*/
			this.parent.pushMatrix();
			this.parent.translate(this.currentX, this.currentY);
			this.parent.rotate(PApplet.radians(this.angle));
			this.parent.image(this.image, 0, 0, this.imgWidth, this.imgHeight);
			this.parent.popMatrix();
		}
	}

}
