package Streamer;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


class Sound
{
	String filename;
	
	public Sound(String filename) {
		this.filename = filename;
	}
}

class audioFilter implements FilenameFilter {
	@Override
	public boolean accept(File dir, String name) {
		return (name.endsWith(".wav"));
	}
}

public class Playlist
{
	ArrayList<Sound> sounds;
	int pointer = 0;
	
	File soundFile;
	protected AudioInputStream audioInputStream;
	
	AudioFormat format;
	//DataLine.Info info;
	byte[] fileBytes;
	
	boolean firstTime = true;
	
	SourceDataLine auline = null;//for testing
	private Position curPosition;
	
	enum Position { 
        LEFT, RIGHT, NORMAL
    };
	
	Playlist() {
		sounds = new ArrayList<Sound>();
		addFile("D:\\wavAppended.wav");
		addFile("D:\\wavAppended.wav");
		addFile("D:\\wavAppended.wav");
		addFile("D:\\wavAppended.wav");
		addFile("D:\\wavAppended.wav");
		addFile("D:\\wavAppended.wav");
		addFile("D:\\wavAppended.wav");
		addFile("D:\\wavAppended.wav");
		//addFolder(System.getProperty("user.dir") + "\\src\\chiptune\\");
	}   
	
	public void addFile(String filename) {
		sounds.add(new Sound(filename));
	}
	
	public void addFolder(String path) {
		try {
			File folder = new File(path).getCanonicalFile();
			String[] files = folder.list(new audioFilter());
			
			for (int i=0; i<files.length; i++)
				addFile(path + files[i]);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void nextFile() {
		soundFile = new File(sounds.get(pointer++).filename);
		if (pointer >= sounds.size())
			pointer = 0;
		
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		format = audioInputStream.getFormat();
	}
	
	public Packet getNextPacket() {
		byte[] nextBytes = new byte[200];
		int nBytes = -1;
		
		if (!firstTime)
			try {
				nBytes = audioInputStream.read(nextBytes, 0, nextBytes.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			firstTime = false;
			
		if (nBytes < 0) {
			nextFile();
			return new Packet(this.format);
		}
		
		if (nBytes != nextBytes.length)
			nextBytes = cut(nextBytes, nBytes);
		
		Packet packet = new Packet(nextBytes);
		
		return packet;
	}
	
	byte[] cut(byte[] bytes, int size) {
		byte[] result = new byte[size];
		for (int i=0; i<size; i++)
			result[i] = bytes[i];
		return result;
	}
	
	public Packet getFormatPacket() {
		return new Packet(this.format);
	}
}
