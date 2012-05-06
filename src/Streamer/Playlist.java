package Streamer;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Playlist
{
	//private String filename = System.getProperty("user.dir") + "\\src\\chiptune\\Control Room 001.wav";
	private String filename = "D:\\wavAppended.wav";
	
	File soundFile;
	protected AudioInputStream audioInputStream;
	
	AudioFormat format;
	DataLine.Info info;
	byte[] fileBytes;
	
	boolean firstTime = true;
	
	Playlist() {
	}
	
	void nextFile() {
		soundFile = new File(filename);
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		format = audioInputStream.getFormat();
		info = new DataLine.Info(SourceDataLine.class, format);
	}
	
	public Packet getNextPacket() {
		byte[] nextBytes = new byte[200];
		int nBytes = -1;
		
		if (!firstTime)
			try {
				//nextBytes = audioInputStream.read();
				nBytes = audioInputStream.read(nextBytes, 0, nextBytes.length);
				//System.out.println(nextByte);
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			firstTime = false;
		
		if (nBytes != nextBytes.length && nBytes > 0) {
			byte[] tmp = new byte[nBytes];
			for (int i=0; i<nBytes; i++)
				tmp[i] = nextBytes[i];
			nextBytes = tmp;
		}
			
		//if (nextByte == -1) {
		if (nBytes == -1) {
			nextFile();
			//return new Packet(this.format, this.info);
			return new Packet(this.format);
		}
		
		return new Packet(nextBytes);
	}
	
	public Packet getFormatPacket() {
		//return new Packet(this.format, this.info);
		return new Packet(this.format);
	}
}
