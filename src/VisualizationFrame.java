import java.util.*;
import javax.swing.*;

public class VisualizationFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MidiSketch sketch;

	public VisualizationFrame(ArrayList<String> images, ArrayList<Integer> lengths) {
        this.setSize(340, 350);
        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setBounds(20, 20, 340, 340);
        this.sketch = new MidiSketch();
        panel.add(this.sketch);
        this.add(panel);
        
        this.sketch.initImages(images, lengths);
        this.sketch.init();
        
        this.setVisible(true);
    }
	
	public void next() {
		this.sketch.nextImage();
	}

}
