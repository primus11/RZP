package Streamer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;


public class RecvFromClients extends Thread {
	Playlist playlist;
	//Receiver[] receivers;
	ArrayList<Receiver> receivers;
	DatagramSocket socket;
	
	public RecvFromClients(Playlist playlist, ArrayList receivers, int recvPort) {
		this.playlist = playlist;
		this.receivers = receivers;

		try {
			this.socket = new DatagramSocket(recvPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		this.start();
	}
	
	public void run() {
		byte[] buffer = new byte[1024];
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		while (true) {
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			byte[] cmd = packet.getData();
			
			InetAddress addr = packet.getAddress();
			
			//if (buffer[0] == 0 && buffer[1] == 0)
			if (Codes.equal(Codes.JOIN, cmd))
				addReceiver( addr );
			else if (Codes.equal(Codes.FORMAT, cmd))
			//else if (buffer[0] == 0 && buffer[1] == 1)
				formatOK( addr );
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	Receiver getReceiver(InetAddress addr) {
		for (int i=0; i<receivers.size(); i++)
			if ( receivers.get(i).hasAddr(addr) )
				return receivers.get(i);
		return null;
	}
	
	void addReceiver(InetAddress addr) {
		if (getReceiver(addr) == null)
			receivers.add( new Receiver(addr) );
	}
	
	void formatOK(InetAddress addr) {
		Receiver receiver = getReceiver(addr);
		if (receiver != null)
			receiver.hasFormat = true;
	}
}
