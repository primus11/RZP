import processing.core.*;
import javax.swing.*;

public class VisualizationFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VisualizationFrame() {
        this.setSize(600, 600);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setBounds(20, 20, 600, 600);
        PApplet sketch = new MidiSketch();
        panel.add(sketch);
        this.add(panel);
        sketch.init(); //start execution of the sketch
        this.setVisible(true);
    }

}
