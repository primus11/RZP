import java.io.*;
import java.util.*;

public class Track implements Runnable {
	public int x;
	public int y;
	PlayWave pw;
	ArrayList<String> samples;
	ArrayList<String> images;
	ArrayList<Integer> lengths;
	VisualizationFrame frame;
	
	public Track(int x, int y) {
		this.x = x;	
		this.y = y;
		samples = new ArrayList<String>();
	}
	
	public void addSamplesToTrack(ArrayList<String> samples) {
		this.samples = samples;
		
		this.images = new ArrayList<String>();
		this.lengths = new ArrayList<Integer>();
		try {
			File folder = new File("visualizationImages").getCanonicalFile();
			File[] listOfFiles = folder.listFiles();
			for(int i=0;i<samples.size(); i++) {
				File tmp = listOfFiles[i % listOfFiles.length];
				if (tmp.isFile()) {
					this.images.add(tmp.getAbsolutePath());
					this.lengths.add(Math.max((int) PlayWave.getDurationOfWavInSeconds(new File(samples.get(i))), 1));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	private void prepareVisualizationFrame() {
		this.frame = new VisualizationFrame(this.images, this.lengths);
	}

	@Override
	public void run() {
		if (samples.size() > 0) {
			this.prepareVisualizationFrame();
		}
		
		for(int i=0;i<samples.size();i++) {
			PlayWave pw = new PlayWave(samples.get(i).toString());
			pw.start();
			frame.next();
			try {
				pw.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	
}
