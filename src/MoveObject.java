import processing.core.*;


public class MoveObject {
	
	public static final int MOVE_TYPE_ROBOT = 1;
	public static final int MOVE_TYPE_FROG = 2;
	public static final int MOVE_TYPE_HELICOPTER = 3;
		
	protected float currentX, currentY;
	protected int length;
	
	protected long startTime;
	protected long endTime;
	protected boolean timing;
	
	//helicopter move
	protected float direction;
	
	protected int moveType;
	protected PImage image;
	protected PApplet parent;
	
	public boolean enabled;
	
	public MoveObject(PApplet parent, PImage image, int moveType, int length) {
		this.parent = parent;
		this.image = image;
		this.length = length;
		this.moveType = moveType;
		
		this.direction = 1;
		
		this.currentX = 0;
		this.currentY = 0;
		
		this.enabled = false;
		this.timing = false;
	}
	
	private float calcTimeRelative() {
		long diff = System.currentTimeMillis() - this.startTime; 
		return (float) PApplet.abs(diff) / (this.length * 1000);
	}
	
	private void moveRobot() {
		this.currentX = this.calcTimeRelative() * this.parent.width;
		//this.currentX += (this.parent.width / this.length);
		this.currentY = this.parent.height - 50;
		
		if (this.currentX > this.parent.width + 50) {
			this.enabled = false;
		}	
	}
	
	private void moveFrog() {
		this.currentX = this.calcTimeRelative() * 360;
		//this.currentX += (360 / this.length); //how many degrees add per move
		this.currentY = this.parent.height - PApplet.abs(PApplet.sin(PApplet.radians(this.currentX)) * 75) - 50;
		
		if (this.currentX > this.parent.width + 50) {
			this.enabled = false;
		}
	}
	
	private void moveHelicopter() {
		this.currentX = this.parent.width/2 - 25;
		
		if ((this.direction == 1) && (this.currentY > this.parent.height/2 - 25)) {
			this.direction = -1;
		}
		this.currentY = this.calcTimeRelative() * this.parent.height;
		//this.currentY += (this.parent.height / this.length) * this.direction;
		
		if (this.currentY < -50) {
			this.enabled = false;
		}
	}

	public void move() {
		if (this.enabled) {
			
			if (!this.timing) {
				this.startTime = System.currentTimeMillis();
				this.timing = true;
			}
			
			switch (this.moveType) {
			case MOVE_TYPE_ROBOT:
				this.moveRobot();
				break;
			case MOVE_TYPE_FROG:
				this.moveFrog();
				break;
			case MOVE_TYPE_HELICOPTER:
				this.moveHelicopter();
				break;
			default:
				this.moveRobot();
				break;
			}
		}
	}
	
	public void display() {
		if (this.enabled) {
			System.out.println("Current X: " + this.currentX);
			System.out.println("Current Y: " + this.currentY);
			this.parent.image(this.image, this.currentX, this.currentY, 50, 50);
		}
	}

}
