package Streamer;
import java.io.Serializable;

import javax.sound.sampled.AudioFormat;


public class AudioFormatInfo implements Serializable {
	float sampleRate;
	int sampleSizeInBits;
	int channels;
	boolean signed;
	boolean bigEndian;
	
	public AudioFormatInfo(AudioFormat format) {
		this.sampleRate = format.getSampleRate();
		this.sampleSizeInBits = format.getSampleSizeInBits();
		this.channels = format.getChannels();
		this.signed = true; //??
		this.bigEndian = format.isBigEndian();
	}
	
	public AudioFormat getAudioFormat() {
		return new AudioFormat(this.sampleRate, this.sampleSizeInBits, this.channels, this.signed, this.bigEndian);
	}
}
