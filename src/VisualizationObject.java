import java.util.*;


public class VisualizationObject {

	private String imageFilename;
	private String soundFilename;
	private int length;
	private int moveType;
	
	private Map<String, String> map = new HashMap<String, String>();
	
	public VisualizationObject(String soundFilename) {
		this.soundFilename = soundFilename;
		
		
	}
	
	public String getImageFileName() {
		return this.imageFilename;
	}
	
	public String getSoundFilename() {
		return this.soundFilename;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public int getMoveType() {
		return this.moveType;
	}
	
	private void createMaps() {
		this.map.put("Silence", "");
		this.map.put("Bass", "Bubble.jpg");
		this.map.put("Blip", "Star.jpg");
		this.map.put("Bouncer", "Bomb.jpg");
		this.map.put("Chip", "Hammer.png");
		this.map.put("Click", "Switch.jpg");
		this.map.put("Control Room", "Robot.jpg");
		this.map.put("Creature", "Monster.jpg");
		this.map.put("Droid Helper", "Robot.jpg");
		this.map.put("Effect", "Helicopter.jpg");
		this.map.put("Engine Bass", "Star.jpg");
		this.map.put("Frog", "Frog.jpg");
		this.map.put("Jump", "Jump.jpg");
		this.map.put("Kick", "Chuck.jpg");
		this.map.put("Lead", "Jump.jpg");
		this.map.put("Lift", "UFO.jpg");
		this.map.put("Monster", "Monster.jpg");
		this.map.put("Noise", "Noise.jpg");
		this.map.put("Phat Retro", "Bubble.jpg");
		this.map.put("Power", "Ball.jpg");
		this.map.put("Quick Bass", "Electric.jpg");
		this.map.put("Robot Talk", "Robot.jpg");
		this.map.put("Snare", "Hammer.png");
		this.map.put("Vocalish", "Monster.jpg");
		this.map.put("Zap", "Beam.jpg");
		this.map.put("Zapper", "Beam.jpg");
		this.map.put("Zoom Down", "Robot.jpg");
	}
	
	
}
