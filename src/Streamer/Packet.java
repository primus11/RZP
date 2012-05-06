package Streamer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;


public class Packet implements Serializable {
	//boolean type;
	
	byte[] nextBytes;
	
	//AudioFormat format;
	//DataLine.Info info;
	AudioFormatInfo audioFormatInfo = null;
	
	byte[] bytes = null;
	
	public Packet(byte[] nextBytes) {
		//this.nextBytes = new byte[1];
		//this.nextBytes[0] = nextByte;
		this.nextBytes = nextBytes;
		this.bytes = Utils.ObjToBytes(this);
		//byte[] by = Utils.ObjToBytes(this.format);
	}
	
	//public Packet(AudioFormat format, DataLine.Info info) {
	public Packet(AudioFormat format) {
		//this.format = format;
		//this.info = info;
		this.audioFormatInfo = new AudioFormatInfo(format);
		
		this.bytes = Utils.ObjToBytes(this);
	}
	
	public boolean isFormat() {
		return (this.audioFormatInfo != null);
	}
	
	public void send(String address, int port, int transmitterPort) {
		DatagramSocket tsocket = null;
		try {
			tsocket = new DatagramSocket(transmitterPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		DatagramPacket packet = null;
		try {
			packet = new DatagramPacket(this.bytes, this.bytes.length, InetAddress.getByName(address), port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		try {
			tsocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tsocket.close();
	}
	
	public void send(Receiver receiver) {
		send(receiver.ip, receiver.port, Server.sndPort);
	}
}
