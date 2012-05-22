package Streamer;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.sampled.SourceDataLine;


public class Server {
	public static int recvPort = 6545;
	public static int sndPort = 6546;
	
	SendToClients sendToClients;
	PacketFiller packetFiller;
	RecvFromClients recvFromClients;

	Playlist playlist;
	BlockingQueue<Packet> packetQueue;
	//Receiver[] receivers;
	ArrayList<Receiver> receivers;
	
	public static void main(String[] args)
	{
		Server server = new Server();
		server.start();
		
		try {
			server.sendToClients.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		this.playlist = new Playlist();
		this.packetQueue = new LinkedBlockingQueue<Packet>();
		this.receivers = new ArrayList<Receiver>();
		
		this.sendToClients = new SendToClients(this.packetQueue, this.receivers);
		this.packetFiller = new PacketFiller(this.packetQueue, this.playlist);
		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		this.recvFromClients = new RecvFromClients(this.playlist, this.receivers, Server.recvPort);
	}
}
