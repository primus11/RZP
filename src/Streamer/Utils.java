package Streamer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Utils {
	public static Object bytesToObj(byte[] bytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] ObjToBytes(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
	public static void sendUDP(byte[] bytes, String address, int recvPort, int sndPort) {
		DatagramSocket tsocket = null;
		try {
			tsocket = new DatagramSocket(sndPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		DatagramPacket packet = null;
		try {
			packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(address), recvPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		try {
			tsocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tsocket.close();
	}
}
