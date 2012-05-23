package Streamer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class Client {
	public static int rcvPort = 3426;
	public static int sndPort = 3427;
	static String host = "127.0.0.1";
	DatagramSocket sendSocket;
	
	ClientReceiver receiver;
	BlockingQueue<Packet> queue;
	
	SourceDataLine auline = null;
	Position curPosition;
	
	enum Position { 
        LEFT, RIGHT, NORMAL
    };
	
	public static void main(String[] args)
	{
		Client client = new Client();
		client.start();
	}
	
	public void start() {
		try {
			sendSocket = new DatagramSocket(Client.sndPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		this.queue = new LinkedBlockingQueue<Packet>();
		this.receiver = new ClientReceiver(this.queue, this.sendSocket);
		
		Utils.sendUDP(Codes.JOIN, Client.host, Server.recvPort, sendSocket); //can fail! (send until packet received back - TODO)
		
		mainLoop();
	}
	
	void setFormat(Packet packet) {
		if (auline != null && auline.isOpen()) {
			auline.drain();
			auline.flush();
			auline.close();
		}
		
		try {
			AudioFormat format = packet.audioFormatInfo.getAudioFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		this.curPosition = Position.NORMAL;
		if (auline.isControlSupported(FloatControl.Type.PAN)) {
			FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
			if (this.curPosition == Position.RIGHT)
				pan.setValue(1.0f);
			else if (this.curPosition == Position.LEFT)
				pan.setValue(-1.0f);
		}
		
		auline.start();
	}
	
	void playBytes(Packet packet) {
		int offset = 0;
		while (offset < packet.nextBytes.length)
			offset += auline.write(packet.nextBytes, offset, packet.nextBytes.length-offset);
	}
	
	void mainLoop() {
		int lastID = -1;
		
		ArrayList<Packet> waitingPackets = new ArrayList<Packet>();
		int oldest = 100;
		
		Packet lastPacket = null;
		Packet packet;
		
		int times = 0;
		
		while (true) {
			times++;
			
			while (!this.queue.isEmpty())
				try {
					waitingPackets.add(this.queue.take());
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			
			if (waitingPackets.isEmpty())
				try {
					Thread.sleep(1);
					continue;
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			if (times%100 == 0) {
				if (waitingPackets.size() > 500)
					Utils.sendUDP(Codes.SLOWER, Client.host, Server.recvPort, sendSocket);
				if (waitingPackets.size() < 200)
					Utils.sendUDP(Codes.FASTER, Client.host, Server.recvPort, sendSocket);
			}
			
			//find earliest
			int minIdx = waitingPackets.size()-1;
			for (int i=waitingPackets.size()-2; i>=0; i--) {
				//if (waitingPackets.get(i).id >= lastID + oldest || waitingPackets.get(i).id < lastID+1) {
				if (waitingPackets.get(i).id < lastID+1) {
						waitingPackets.remove(i);
					minIdx--;
					continue;
				}
				
				if (waitingPackets.get(i).id < waitingPackets.get(minIdx).id)
					minIdx = i;
			}
			packet = waitingPackets.get(minIdx);
			
			/*
			 * could be useful at some point - better to play previous (or waiting) than skipping xxx packets
			if (false && packet.id != lastID + 1 && lastPacket != null) {
				packet = lastPacket;
				packet.id = lastID + 1;
			}
			else*/
				waitingPackets.remove(minIdx);
				
			lastID = packet.id;
			
			if ( packet.isFormat() ) {
				setFormat(packet);
			}
			else if ( auline != null ) {
				playBytes(packet);
			}
			
			lastPacket = packet;
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
