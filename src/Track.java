import java.util.ArrayList;

public class Track implements Runnable {
	public int x;
	public int y;
	PlayWave pw;
	ArrayList<String> samples;
	
	public Track(int x, int y) {
		this.x = x;	
		this.y = y;
		samples = new ArrayList<String>();
	}
	
	public void addSamplesToTrack(ArrayList<String> samples) {
		this.samples = samples;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void run() {
			for(int i=0;i<samples.size();i++) {
				PlayWave pw = new PlayWave(samples.get(i).toString());
				pw.start();
				try {
					pw.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
	}
	
}
