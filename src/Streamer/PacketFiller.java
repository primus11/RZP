package Streamer;
import java.util.concurrent.BlockingQueue;


public class PacketFiller extends Thread {
	BlockingQueue<Packet> queue;
	Playlist playlist;
	private int sleep = 1;
	
	public PacketFiller(BlockingQueue<Packet> queue, Playlist playlist) {
		this.queue = queue;
		this.playlist = playlist;
		this.start();
	}
	
	public void faster() {
		this.sleep--;
		if (this.sleep < 1)
			this.sleep = 1;
	}
	
	public void slower() {
		this.sleep++;
		if (this.sleep > 20)
			this.sleep = 20;
	}
	
	public void run() {
		while (true) {
			//System.out.println(queue.size());
			if (queue.size() < 1000) {
				try {
					//Packet packet = this.playlist.getNextPacket();
					//packet.bytes = Utils.ObjToBytes(packet);
					//queue.put(packet);
					queue.put(this.playlist.getNextPacket());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//else
				//sleep
			
			try {
				Thread.sleep(this.sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
