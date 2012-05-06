package Streamer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class Client {
	public static int rcvPort = 3426;
	public static int sndPort = 3427;
	DatagramSocket socket;
	
	SourceDataLine auline = null;
	//PlayWave.Position curPosition;
	
	public static void main(String[] args)
	{
		Client client = new Client();
		client.start();
	}
	
	public void start() {
		try {
			socket = new DatagramSocket(Client.rcvPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		Utils.sendUDP(Codes.JOIN, "127.0.0.1", Server.recvPort, Client.sndPort); //can fail! (send until packet received back - TODO)
		
		mainLoop();
	}
	
	void setFormat(Packet packet) {
		if (auline != null && auline.isOpen()) {
			auline.drain();
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
		
		/*if (auline.isControlSupported(FloatControl.Type.PAN)) {
			FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
			if (curPosition == PlayWave.Position.RIGHT)
				pan.setValue(1.0f);
			else if (curPosition == PlayWave.Position.LEFT)
				pan.setValue(-1.0f);
		}*/
		
		auline.start();
	}
	
	void playBytes(Packet packet) {
		//System.out.println("playBytes");
		auline.write(packet.nextBytes, 0, packet.nextBytes.length);
	}
	
	void mainLoop() {
		byte[] buffer = new byte[2048];
		
		while (true) {
			DatagramPacket UDPpacket = new DatagramPacket(buffer, buffer.length);
			
			try {
				this.socket.receive(UDPpacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Packet packet = (Packet) Utils.bytesToObj(buffer);
			
			if ( packet.isFormat() ) {
				setFormat(packet);
				Utils.sendUDP(Codes.FORMAT, "127.0.0.1", Server.recvPort, Client.sndPort);
			}
			else {
				playBytes(packet);
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
