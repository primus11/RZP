package Streamer;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class SendToClients extends Thread {
	BlockingQueue<Packet> queue;
	//Receiver[] receivers;
	ArrayList<Receiver> receivers;
	Packet format;
	
	public SendToClients(BlockingQueue<Packet> queue, ArrayList receivers) {
		this.queue = queue;
		this.receivers = receivers;
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
				format = packet;
				
				for (int i=0; i<this.receivers.size(); i++)
					receivers.get(i).hasFormat = false;
			}
			
			for (int i=0; i<receivers.size(); i++) {
				if (receivers.get(i).hasFormat)
					packet.send(receivers.get(i));
				else
					format.send(receivers.get(i));
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
