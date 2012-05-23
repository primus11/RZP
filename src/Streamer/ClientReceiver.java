package Streamer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class ClientReceiver extends Thread {
	BlockingQueue<Packet> queue;
	DatagramSocket socket;
	DatagramSocket sendSocket;
	
	public ClientReceiver(BlockingQueue<Packet> queue, DatagramSocket sendSocket) {
		this.queue = queue;
		this.sendSocket = sendSocket;
		
		try {
			socket = new DatagramSocket(Client.rcvPort);
		} catch (SocketException e) {
			e.printStackTrace();
		} 
		
		this.start();
	}	
	
	@Override
	public void run() {
		byte[] buffer = new byte[262144];
		DatagramPacket UDPpacket = new DatagramPacket(buffer, buffer.length);
		
		while (true) {
			try {
				this.socket.receive(UDPpacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Packet packet = (Packet) Utils.bytesToObj( Utils.truncate(UDPpacket.getData(), UDPpacket.getLength()) );
			
			if (packet.isFormat())
				Utils.sendUDP(Codes.FORMAT, Client.host, Server.recvPort, sendSocket);
			
			try {
				this.queue.put(packet);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
