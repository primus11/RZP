
public class Player {
	Track t1;
	Track t2;
	Track t3;
	
	public Player(Track t1, Track t2, Track t3) {
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}
	
	public void play() {
		(new Thread(t1)).start();
		(new Thread(t2)).start();
		(new Thread(t3)).start();
	}
}
