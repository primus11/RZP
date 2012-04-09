import java.awt.Color;

public class ChiptuneSample {
	private int pos_x;
	private int pos_y;
	private String filename; //Arcade neki.wav
	private String name;     //Arcade
	private Color color;
	
	public ChiptuneSample(String filename) {
		this.filename = filename;
		String first = filename.split(" ")[0];
		System.out.println(first);
		this.name = first;
		this.color = Color.getHSBColor((float)Math.random()*359, 8, 94);
	}
	public void setPosXY(int x, int y) {
		this.pos_x = x;
		this.pos_y = y;
	}

	public int getPosX() {
		return pos_x;	
	}
	
	public int getPosY() {
		return pos_y;	
	}
	
	public String getName() {
		return name;	
	}
	
	public String getFilename() {
		return filename;	
	}
	
	public Color getColor() {
		return color;
	}
}
