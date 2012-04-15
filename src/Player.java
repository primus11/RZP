
public class Player {
	Track t1;
	Track t2;
	Track t3;
	Thread n1;
	Thread n2;
	Thread n3;
	
	public Player(Track t1, Track t2, Track t3) {
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		n1 = new Thread(t1);
		n2 = new Thread(t2);
		n3 = new Thread(t3);
	}
	
	public void play() {
		n1.start();
		n2.start();
		n3.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
		n1.stop();
		n2.stop();
		n3.stop();
	}
}
