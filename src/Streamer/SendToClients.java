package Streamer;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class SendToClients extends Thread {
	BlockingQueue<Packet> queue;
	ArrayList<Receiver> receivers;
	Packet format;
	
	DatagramSocket tsocket;
	
	public SendToClients(BlockingQueue<Packet> queue, ArrayList receivers) {
		this.queue = queue;
		this.receivers = receivers;
		
		try {
			tsocket = new DatagramSocket(Server.sndPort);
		} catch (SocketException e) {
			e.printStackTrace();
		} 
		
		this.start();
	}
	
	@Override
	public void run() {
		while (true) {
			Packet packet = null;
			try {
				packet = queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if ( packet.isFormat() ) {
				while (queue.size() < 100 )
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
				format = packet;
				
				//for (int i=0; i<this.receivers.size(); i++)
				//	receivers.get(i).hasFormat = false;
			}
			
			for (int i=0; i<receivers.size(); i++) {
				if (!receivers.get(i).hasFormat)
					format.send(receivers.get(i), tsocket);
				
				if (!packet.isFormat())
					packet.send(receivers.get(i), tsocket);
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//tsocket.close();
	}
}
