import java.io.*;
import java.util.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Track implements Runnable {
	public int x;
	public int y;
	//private PlayWave pw;
	private ArrayList<String> samples;
	private ArrayList<VisualizationObject> visObjects;
	private VisualizationFrame frame;
	
	enum Position { 
        LEFT, RIGHT, NORMAL
    };
	
	private Position curPosition;
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb
	
	public Track(int x, int y) {
		this.x = x;	
		this.y = y;
		samples = new ArrayList<String>();
	}
	
	public void addSamplesToTrack(ArrayList<String> samples) {
		this.samples = samples;
		
		this.visObjects = new ArrayList<VisualizationObject>();
		for (int i=0; i<samples.size(); i++) {
			this.visObjects.add(new VisualizationObject(samples.get(i)));
		}
		
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	private void prepareVisualizationFrame() {
		this.frame = new VisualizationFrame(this.visObjects);
	}

	@Override
	public void run() {
		if (samples.size() > 0) {
			this.prepareVisualizationFrame();
		}
		
		for(int i=0;i<samples.size();i++) {
			/*
			PlayWave pw = new PlayWave(samples.get(i).toString());
			pw.start();
			frame.next();
			try {
				pw.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
			frame.next();
			String sample = samples.get(i).toString();
			this.playSound(sample);
			try {
				Thread.sleep((long) (PlayWave.getDurationOfWavInSeconds(new File(sample)) * 1000 * 1.01));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	
	private void playSound(String filename) {
		File soundFile = new File(filename);
        if (!soundFile.exists()) { 
            System.err.println("Wave file not found: " + filename);
            return;
        } 
 
        AudioInputStream audioInputStream = null;
        try { 
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException e1) { 
            e1.printStackTrace();
            return;
        } catch (IOException e1) { 
            e1.printStackTrace();
            return;
        } 
 
        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
 
        try { 
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (LineUnavailableException e) { 
            e.printStackTrace();
            return;
        } catch (Exception e) { 
            e.printStackTrace();
            return;
        } 
 
        if (auline.isControlSupported(FloatControl.Type.PAN)) { 
            FloatControl pan = (FloatControl) auline
                    .getControl(FloatControl.Type.PAN);
            if (this.curPosition == Position.RIGHT) 
                pan.setValue(1.0f);
            else if (this.curPosition == Position.LEFT) 
                pan.setValue(-1.0f);
        } 
 
        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
 
        try { 
            while (nBytesRead != -1) { 
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) 
                    auline.write(abData, 0, nBytesRead);
            } 
        } catch (IOException e) { 
            e.printStackTrace();
            return;
        } finally { 
            auline.drain();
            auline.close();
        } 
	}
	
}
