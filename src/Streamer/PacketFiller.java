package Streamer;
import java.util.concurrent.BlockingQueue;


public class PacketFiller extends Thread {
	BlockingQueue<Packet> queue;
	Playlist playlist;
	
	public PacketFiller(BlockingQueue<Packet> queue, Playlist playlist) {
		this.queue = queue;
		this.playlist = playlist;
		this.start();
	}
	
	public void run() {
		while (true) {
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
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
