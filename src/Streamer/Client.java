package Streamer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

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
	static String host = "127.0.0.1";
	DatagramSocket sendSocket;
	
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
			socket = new DatagramSocket(Client.rcvPort);
			sendSocket = new DatagramSocket(Client.sndPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		//Utils.sendUDP(Codes.JOIN, Client.host, Server.recvPort, Client.sndPort); //can fail! (send until packet received back - TODO)
		Utils.sendUDP(Codes.JOIN, Client.host, Server.recvPort, sendSocket);
		
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
		/*while (auline.available() < packet.nextBytes.length)
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		//System.out.println(packet.nextBytes.length);
		/*while(auline.isActive())
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		int offset = 0;
		while (offset < packet.nextBytes.length)
			offset += auline.write(packet.nextBytes, offset, packet.nextBytes.length-offset);
		//offset += auline.write(packet.nextBytes, 0, packet.nextBytes.length);
	}
	
	void mainLoop() {
		byte[] buffer = new byte[262144];
		//byte[] buffer = new byte[1024];
		int lastID = -1;
		
		ArrayList<Packet> waitingPackets = new ArrayList<Packet>();
		int oldest = 100;
		
		DatagramPacket UDPpacket = new DatagramPacket(buffer, buffer.length);
		
		while (true) {
			try {
				this.socket.receive(UDPpacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/*for (int i=0; i<UDPpacket.getLength(); i++)
				if (UDPpacket.getData()[i] != buffer[i])
					System.out.println("diff");*/
			

			Packet packet = (Packet) Utils.bytesToObj( Utils.truncate(UDPpacket.getData(), UDPpacket.getLength()) );
			
			if (packet.isFormat())
				Utils.sendUDP(Codes.FORMAT, Client.host, Server.recvPort, sendSocket);
			
			//System.out.println(packet.id + " " + packet.isFormat());
			
			//if (!packet.isFormat()
			//	System.out.println(packet.checkSum + " " + packet.sumBytes());
			
			if (packet.id != lastID+1) {
				waitingPackets.add(packet);
				
				//Packet minID = waitingPackets.get(0);
				int minIdx = waitingPackets.size()-1;
				for (int i=waitingPackets.size()-2; i>=0; i--) {
					if (waitingPackets.get(i).id >= lastID + oldest || waitingPackets.get(i).id < lastID+1) {
						waitingPackets.remove(i);
						minIdx--;
						continue;
					}
					
					if (waitingPackets.get(i).id < waitingPackets.get(minIdx).id)
						minIdx = i;
				}
				
				//System.out.println("minId: " + waitingPackets.get(minIdx).id + " " + lastID);
				
				packet = waitingPackets.get(minIdx);
				waitingPackets.remove(minIdx);
				if (packet.id >= lastID + oldest) {
					lastID = packet.id - 1;
				}
				else if (packet.id == lastID + 1) {
				}
			}
			
			//if not in order
			/*if (packet.id != lastID+1 && !packet.isFormat()) {
				//System.out.println(packet.id + " " + lastID+1);
				System.out.println("not in order. id: " + packet.id + " lastid+1: " + (lastID+1));
				continue;
			}
			else*/
				lastID = packet.id;
			
			//System.out.println(packet.id);
			
			if ( packet.isFormat() ) {
				//System.out.println("wewe");
				setFormat(packet);
				//Utils.sendUDP(Codes.FORMAT, Client.host, Server.recvPort, Client.sndPort);
				//Utils.sendUDP(Codes.FORMAT, Client.host, Server.recvPort, sendSocket);
			}
			else {
				//System.out.println("client " + packet.nextBytes + " " + new String(packet.nextBytes) + " " + packet.id);
				//if (packet.id % 2 == 0)
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
