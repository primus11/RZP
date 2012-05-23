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
	public static int nextId = 0;
	int id;
	
	byte[] nextBytes;
	//int checkSum;
	
	AudioFormatInfo audioFormatInfo = null;
	
	byte[] bytes = null;
	
	public Packet(byte[] nextBytes) {
		this.nextBytes = nextBytes;
		//this.checkSum = sumBytes();

		init();
	}
	
	//public Packet(AudioFormat format, DataLine.Info info) {
	public Packet(AudioFormat format) {
		this.audioFormatInfo = new AudioFormatInfo(format);
		
		init();
	}
	
	void init() {
		this.id = Packet.nextId++;
		this.bytes = Utils.ObjToBytes(this);
	}
	
	public int sumBytes() {
		int sum = 0;
		for (int i=0; i<nextBytes.length; i++)
			sum += nextBytes[i];
		return sum;
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
	
	public void send(Receiver receiver, DatagramSocket tsocket) {
		send(receiver.ip, receiver.port, tsocket);
	}
	
	public void send(String address, int port, DatagramSocket tsocket) {
		Utils.sendUDP(this.bytes, address, port, tsocket);
	}
}
