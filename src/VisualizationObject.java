import java.io.*;
import java.util.*;


public class VisualizationObject {

	private String imageFilename;
	private String soundFilename;
	private float length;
	private int moveType;
	
	private Map<String, String> map = new HashMap<String, String>();
	
	public VisualizationObject(String soundFilename) {
		this.soundFilename = soundFilename;
		
		this.createMaps();
		String imageFile = this.findKeyMap();
		this.imageFilename = "visualizationImages\\" + imageFile;
		this.moveType = this.getImageMoveType(imageFile);
		this.length = (float) PlayWave.getDurationOfWavInSeconds(new File(this.soundFilename));
	}
	
	public String getImageFileName() {
		return this.imageFilename;
	}
	
	public String getSoundFilename() {
		return this.soundFilename;
	}
	
	public float getLength() {
		return this.length;
	}
	
	public int getMoveType() {
		return this.moveType;
	}
	
	private String findKeyMap() {
		Iterator<String> it = this.map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (this.soundFilename.indexOf(key) != -1) {
				return this.map.get(key);				
			}
		}
		
		return "Ball.png";
	}
	
	private int getImageMoveType(String imageFileName) {
		switch (imageFileName) {
			case "Bubble.png":
				return MoveObject.MOVE_TYPE_BUBBLE;
			case "Star.png":
				return MoveObject.MOVE_TYPE_STAR;
			case "Bomb.png":
				return MoveObject.MOVE_TYPE_BOMB;
			case "Hammer.png":
				return MoveObject.MOVE_TYPE_HAMMER;
			case "Switch.png":
				return MoveObject.MOVE_TYPE_SWITCH;
			case "Robot1.png":
				return MoveObject.MOVE_TYPE_ROBOT_1;
			case "Monster.png":
				return MoveObject.MOVE_TYPE_MONSTER;
			case "Robot2.png":
				return MoveObject.MOVE_TYPE_ROBOT_2;
			case "Helicopter.png":
				return MoveObject.MOVE_TYPE_HELICOPTER;
			case "Frog.png":
				return MoveObject.MOVE_TYPE_FROG;
			case "Jump.png":
				return MoveObject.MOVE_TYPE_JUMP;
			case "Chuck.png":
				return MoveObject.MOVE_TYPE_CHUCK;
			case "UFO.png":
				return MoveObject.MOVE_TYPE_UFO;
			case "Noise.png":
				return MoveObject.MOVE_TYPE_NOISE;
			case "Ball.png":
				return MoveObject.MOVE_TYPE_BALL;
			case "Electric.jpg":
				return MoveObject.MOVE_TYPE_ELECTRIC;
			case "Beam.jpg":
				return MoveObject.MOVE_TYPE_BEAM;
			default:
				return MoveObject.MOVE_TYPE_ROBOT_1;
		}
	}
	
	private void createMaps() {
		this.map.put("Silence", "Silence.png");
		this.map.put("Bass", "Bubble.png");
		this.map.put("Blip", "Star.png");
		this.map.put("Bouncer", "Bomb.png");
		this.map.put("Chip", "Hammer.png");
		this.map.put("Click", "Switch.png");
		this.map.put("Control Room", "Robot1.png");
		this.map.put("Creature", "Monster.png");
		this.map.put("Droid Helper", "Robot2.png");
		this.map.put("Effect", "Helicopter.png");
		this.map.put("Engine", "Star.png");
		this.map.put("Frog", "Frog.png");
		this.map.put("Jump", "Jump.png");
		this.map.put("Kick", "Chuck.png");
		this.map.put("Lead", "Jump.png");
		this.map.put("Lift", "UFO.png");
		this.map.put("Monster", "Monster.png");
		this.map.put("Noise", "Noise.png");
		this.map.put("Phat Retro", "Bubble.png");
		this.map.put("Power", "Ball.png");
		this.map.put("Quick", "Electric.jpg");
		this.map.put("Robot Talk", "Robot1.png");
		this.map.put("Snare", "Hammer.png");
		this.map.put("Vocalish", "Monster.png");
		this.map.put("Zap", "Beam.jpg");
		this.map.put("Zapper", "Beam.jpg");
		this.map.put("Zoom Down", "Robot2.png");
	}
	
	
}
