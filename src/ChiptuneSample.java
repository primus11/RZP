import java.awt.Color;
import java.util.ArrayList;

public class ChiptuneSample {
	private int pos_x;
	private int pos_y;
	private String filename; //Arcade neki.wav
	private String name;     //Arcade
	private Color color;
	private static ArrayList<String> d = new ArrayList<String>();
	private static ArrayList<Color> c = new ArrayList<Color>(); 
	
	public ChiptuneSample(String filename) {
		this.filename = filename;
		String first = filename.split(" ")[0];
		
		if(!d.contains((String)first)) {
			d.add(first);
			c.add(new Color(
					(int)(Math.random()*150 + 100), 
					(int)(Math.random()*150 + 100), 
					(int)(Math.random()*150 + 100),
					(int)(Math.random()*150 + 100)
					));
		}
		this.name = first;
		//this.color = Color.getHSBColor((float)Math.random()*359, 8, 94);
		this.color = c.get(d.indexOf(first));
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
