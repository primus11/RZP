package Streamer;
import java.net.InetAddress;


public class Receiver {
	boolean hasFormat = false;
	public String ip;
	public int port;
	public InetAddress addr;
	
	//public int voteID = -1;
	
	//Receiver(String ip, int port) {
	Receiver(InetAddress addr) {
		this.addr = addr;
		this.ip = addr.getHostAddress();
		this.port = Client.rcvPort;//TODO
	}
	
	public boolean hasAddr(InetAddress addr) {
		return this.addr.equals(addr);
	}
}
