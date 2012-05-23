package Streamer;

public class Codes {
	public static byte[] JOIN = {0, 0};
	public static byte[] FORMAT = {0, 1};
	
	public static byte[] FASTER = {1, 0};
	public static byte[] SLOWER = {1, 1};
	
	public static boolean equal(byte[] code, byte[] cmd) {
		for (int i=0; i<code.length; i++)
			if (code[i] != cmd[i])
				return false;
		return true;
	}
}
