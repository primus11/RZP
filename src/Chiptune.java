import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class Chiptune extends Applet implements MouseListener {
	private static final long serialVersionUID = 1L;
	ArrayList<ChiptuneSample> samples = new ArrayList<ChiptuneSample>();
	
	ArrayList<String> track1_samples;
	ArrayList<String> track2_samples;
	ArrayList<String> track3_samples;
	Track track1;
	Track track2;
	Track track3;
	int selectedTrack = 1;
	Color selectedColor = Color.BLACK;
	
	Player player;
	
	public void init() {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
			
		//this.setSize(d.width-16, d.height);
		this.setSize(1008, 600);
			File folder;
			try {
				folder = new File("chiptune").getCanonicalFile();
				File[] listOfFiles = folder.listFiles();
				for(int i=0;i<listOfFiles.length;i++){
			    	File f = listOfFiles[i];
			    	if(f.isFile()) {
			    		String name = f.getName();
			    		samples.add(new ChiptuneSample(name));	
			    	}    	
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		track1_samples = new ArrayList<String>();	
		track2_samples = new ArrayList<String>();
		track3_samples = new ArrayList<String>();
		track1 = new Track(80, 300);
		track2 = new Track(80, 340);
		track3 = new Track(80, 380);
		addMouseListener(this);
		
		super.init();
	}
	
	public void paint(Graphics g) {
		int x=1; //Lokacija x
		int y=1; //Lokacija y
		int sirina_p = 53;  //Širina pravokotnika
		int visina_p = 30;  //Višina pravokotnika
		for(int i=0;i<samples.size();i++) {
			ChiptuneSample cs = samples.get(i);
			cs.setPosXY(x, y);
			g.setColor(Color.BLACK);
			g.drawRect(cs.getPosX()-1, cs.getPosY()-1, sirina_p, visina_p);
			g.setColor(cs.getColor());
			g.fillRect(cs.getPosX(), cs.getPosY(), sirina_p-1, visina_p-1);
			g.setColor(Color.BLACK);
			g.drawString(cs.getName(), cs.getPosX()+3,cs.getPosY()+20);
			x+=sirina_p;
			if((i+1)%19==0) {x=1; y+=visina_p;}
		}	
		System.out.println(String.valueOf(x) +" "+ String.valueOf(y));
		
		g.drawLine(80, 300, 970, 300);
		g.drawLine(80, 340, 970, 340);
		g.drawLine(80, 380, 970, 380);
		g.drawLine(80, 420, 970, 420);
		g.drawRect(30, 310, 20, 20);
		g.drawRect(30, 350, 20, 20);
		g.drawRect(30, 390, 20, 20);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(30, 310, 20, 20);
		g.fillRect(30, 350, 20, 20);
		g.fillRect(30, 390, 20, 20);
		
		drawMainButton(g, "Play");
		drawMainButton(g, "Stop");
		drawMainButton(g, "Save");
		drawMainButton(g, "Rand");
		drawMainButton(g, "Add");
		drawMainButton(g, "Del");
		
		paintSelectedTrackButton(g, 1);
		super.paint(g);
	}
	
	public void drawMainButton(Graphics g, String name) {
		if(name.equals("Play")) {
			g.setColor(Color.BLACK);
			g.drawRect(80, 450, 35, 35);
			g.setColor(new Color(123, 143, 244));
			g.fillRect(80, 450, 35, 35);
			g.setColor(Color.BLACK);
			g.drawString("Play", 85, 473);
		} else if(name.equals("Stop")) {
			g.setColor(Color.BLACK);
			g.drawRect(130, 450, 35, 35);
			g.setColor(new Color(123, 143, 244));
			g.fillRect(130, 450, 35, 35);
			g.setColor(Color.BLACK);
			g.drawString("Stop", 135, 473);
		} else if(name.equals("Save")) {
			g.setColor(Color.BLACK);
			g.drawRect(180, 450, 35, 35);
			g.setColor(new Color(123, 143, 244));
			g.fillRect(180, 450, 35, 35);
			g.setColor(Color.BLACK);
			g.drawString("Save", 185, 473);
		} else if(name.equals("Rand")) {
			g.setColor(Color.BLACK);
			g.drawRect(835, 450, 35, 35);
			g.setColor(new Color(123, 143, 244));
			g.fillRect(835, 450, 35, 35);
			g.setColor(Color.BLACK);
			g.drawString("Rand", 838, 473);
		} else if(name.equals("Add")) {
			g.setColor(Color.BLACK);
			g.drawRect(885, 450, 35, 35);
			g.setColor(new Color(123, 143, 244));
			g.fillRect(885, 450, 35, 35);
			g.setColor(Color.BLACK);
			g.drawString("Add", 893, 473);
		} else if(name.equals("Del")) {
			g.setColor(Color.BLACK);
		    g.drawRect(935, 450, 35, 35);
			g.setColor(new Color(123, 143, 244));
			g.fillRect(935, 450, 35, 35);
			g.setColor(Color.BLACK);
			g.drawString("Del", 943, 473);
		}
		super.paint(g);
	}
	
	public void paintSelectedTrackButton(Graphics g, int i) {
		if(i==1) {
			g.setColor(Color.GRAY);
			g.fillRect(30, 310, 20, 20);
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(30, 350, 20, 20);
			g.fillRect(30, 390, 20, 20);
		} else if(i==2) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(30, 310, 20, 20);
			g.fillRect(30, 390, 20, 20);
			g.setColor(Color.GRAY);
			g.fillRect(30, 350, 20, 20);
		} else if(i==3) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(30, 310, 20, 20);
			g.fillRect(30, 350, 20, 20);
			g.setColor(Color.GRAY);
			g.fillRect(30, 390, 20, 20);
		}
		super.paint(g);
	}
	
	public void paintSamplesToTrack(Graphics g, Track t, double seconds) {
		System.out.println(seconds);
		g.setColor(selectedColor);
		g.fillRect(t.x, t.y, (int)Math.ceil(seconds*10), 40);
		g.setColor(Color.BLACK);
		g.drawRect(t.x, t.y, (int)Math.ceil(seconds*10), 40);	
		t.x += (int)Math.ceil(seconds*10);
		super.paint(g);
	}
	
	public void removeSamplesFromTrack(Graphics g) {
		int y = 0;
		switch(selectedTrack) {
			case 1: y=300; break; 
			case 2: y=340; break;
			case 3: y=380; break;
		}
		g.setColor(this.getBackground());
		g.fillRect(80, y+1, 890, 40-1);
		super.paint(g);
	}
	
	public void mouseClicked(MouseEvent arg0) {
		ChiptuneSample cs = getChiptuneSample(arg0.getX(),arg0.getY());
		String filename;
		if(cs == null) {
			filename = "";
		} else {
			filename = new File("chiptune").getAbsolutePath()+"\\"+cs.getFilename();
		}
		switch(arg0.getModifiers()) {
			case InputEvent.BUTTON1_MASK: {

					// Play Button
					if(arg0.getX() >= 80 && arg0.getY() >= 450 &&
					   arg0.getX() <= 80+35 && arg0.getY() <= 450+35) {	
					   if(track1_samples.size() > 0) {   
						   track1.addSamplesToTrack(track1_samples);
						   track2.addSamplesToTrack(track2_samples);
						   track3.addSamplesToTrack(track3_samples);
						   (player = new Player(track1, track2, track3)).play();
						   System.out.println("Playing...");
					   }
					}
					// Stop Button
					if(arg0.getX() >= 130 && arg0.getY() >= 450 &&
					   arg0.getX() <= 130+35 && arg0.getY() <= 450+35) {
					   if(track1_samples.size() > 0) {
						   player.stop();
						   System.out.println("Stoping...");
					   }
					}
					// Save Button
					if(arg0.getX() >= 180 && arg0.getY() >= 450 &&
				       arg0.getX() <= 180+35 && arg0.getY() <= 450+35) {
					   try {
						WavAppender.append(track1_samples, track2_samples, track3_samples);
					} catch (UnsupportedAudioFileException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					// Rand Button
					if(arg0.getX() >= 835 && arg0.getY() >= 450 &&
				       arg0.getX() <= 835+35 && arg0.getY() <= 450+35) {
						String[] files = null;
						File folder;
						try {
							folder = new File("chiptune").getCanonicalFile();
							File[] listOfFiles = folder.listFiles();
							files = new String[listOfFiles.length];
							for(int i=0;i<listOfFiles.length;i++){
						    	File f = listOfFiles[i];
						    	if(f.isFile()) {
						    		//samples.add(new ChiptuneSample(f.getName()));	
						    		System.out.println(f.getAbsolutePath());
						    		files[i] = f.getAbsolutePath();
						    	}    	
						    }
						} catch (IOException e) {
							e.printStackTrace();
						}
						String file;
						switch(selectedTrack) {
							case 1:
								while(true) {
									int rand = (int) (Math.random()*files.length);
									System.out.println(rand);
									file = files[rand];
									if(track1.x + (int)Math.ceil(PlayWave.getDurationOfWavInSeconds(new File(file))*10) >= 970) {
										break;
									} else {
										selectedColor = getSampleColor(file);
										track1_samples.add(file);
										paintSamplesToTrack(this.getGraphics(), track1, PlayWave.getDurationOfWavInSeconds(new File(file)));
									}
								}
								break;
							case 2:
								while(true) {
									int rand = (int) (Math.random()*files.length);
									System.out.println(rand);
									file = files[rand];
									if(track2.x + (int)Math.ceil(PlayWave.getDurationOfWavInSeconds(new File(file))*10) >= 970) {
										break;
									} else {
										selectedColor = getSampleColor(file);
										track2_samples.add(file);
										paintSamplesToTrack(this.getGraphics(), track2, PlayWave.getDurationOfWavInSeconds(new File(file)));
									}
								}
								break;
							case 3:
								while(true) {
									int rand = (int) (Math.random()*files.length);
									System.out.println(rand);
									file = files[rand];
									if(track3.x + (int)Math.ceil(PlayWave.getDurationOfWavInSeconds(new File(file))*10) >= 970) {
										break;
									} else {
										selectedColor = getSampleColor(file);
										track3_samples.add(file);
										paintSamplesToTrack(this.getGraphics(), track3, PlayWave.getDurationOfWavInSeconds(new File(file)));
									}
								}
								break;
						}
					}
					// Add Button
					if(arg0.getX() >= 885 && arg0.getY() >= 450 &&
				       arg0.getX() <= 885+35 && arg0.getY() <= 450+35) {
						String s = JOptionPane.showInputDialog(null, "Vnesi število ponovitev zadnjega vzorca: ", 1);
						int st = Integer.parseInt(s);
						Track t = null;
						ArrayList<String> track_samples = null;
						switch(selectedTrack){
							case 1: t = track1; track_samples = track1_samples; break;
							case 2: t = track2; track_samples = track2_samples; break;
							case 3: t = track3; track_samples = track3_samples; break;
						}
						String last_filename = track_samples.get(track_samples.size()-1); 
						System.out.println(last_filename);
						for(int i=0;i<st;i++) {
							if(t.x + (int)Math.ceil(
									PlayWave.getDurationOfWavInSeconds(
											new File(last_filename))*10
											) < 970) {
							switch(selectedTrack){
								case 1: 
									track1_samples.add(last_filename);
									paintSamplesToTrack(this.getGraphics(), track1, PlayWave.getDurationOfWavInSeconds(new File(last_filename)));	
								break;
								case 2:
									track2_samples.add(last_filename);
									paintSamplesToTrack(this.getGraphics(), track2, PlayWave.getDurationOfWavInSeconds(new File(last_filename)));	
								break;
								case 3: 
									track3_samples.add(last_filename);
									paintSamplesToTrack(this.getGraphics(), track3, PlayWave.getDurationOfWavInSeconds(new File(last_filename)));	
								break;
							}		
							} else {
								break;
							}
						}		
					}
					// Del Button
					if(arg0.getX() >= 935 && arg0.getY() >= 450 &&
				       arg0.getX() <= 935+35 && arg0.getY() <= 450+35) {
						System.out.println("Del");
						switch(selectedTrack) {
							case 1:
								track1_samples.clear();
								track1.setX(80);
								break;
							case 2:
								track2_samples.clear();
								track2.setX(80);
								break;
							case 3:
								track3_samples.clear();
								track3.setX(80);
								break;
						}
						removeSamplesFromTrack(this.getGraphics());
					}
					// First Track
					if((arg0.getX() >= 30 && arg0.getY() >=310 &&
					   arg0.getX() <= 30+20 && arg0.getY() <= 310+20) ||
					   (arg0.getX() >= 80 && arg0.getY() >=300 &&
					   arg0.getX() <= 970 && arg0.getY() <= 340)) {
						this.paintSelectedTrackButton(this.getGraphics(),1);
						selectedTrack = 1;
					}
					// Second Track
					if((arg0.getX() >= 30 && arg0.getY() >= 350 &&
					   arg0.getX() <= 30+20 && arg0.getY() <= 350+20) ||
					   (arg0.getX() >= 80 && arg0.getY() >=340 &&
					    arg0.getX() <= 970 && arg0.getY() <= 380)){
						this.paintSelectedTrackButton(this.getGraphics(),2);
						selectedTrack = 2;
					}
					// Third Track
					if((arg0.getX() >= 30 && arg0.getY() >= 390 &&
					   arg0.getX() <= 30+20 && arg0.getY() <= 390+20) ||
					   (arg0.getX() >= 80 && arg0.getY() >= 380 &&
					   arg0.getX() <= 970 && arg0.getY() <= 420)) {
						this.paintSelectedTrackButton(this.getGraphics(),3);
						selectedTrack = 3;
					}
				if(cs != null) {
					selectedColor = cs.getColor();
					switch(selectedTrack) {
					case 1: 
						track1_samples.add(filename);
						paintSamplesToTrack(this.getGraphics(), track1, PlayWave.getDurationOfWavInSeconds(new File(filename)));
						break;
					case 2:
						track2_samples.add(filename);
						paintSamplesToTrack(this.getGraphics(), track2, PlayWave.getDurationOfWavInSeconds(new File(filename)));
						break;
					case 3:
						track3_samples.add(filename);
						paintSamplesToTrack(this.getGraphics(), track3, PlayWave.getDurationOfWavInSeconds(new File(filename)));
						break;
					}
				}	
				break;
			}
			case InputEvent.BUTTON3_MASK: {
				PlayWave apw = null;
				apw = new PlayWave(filename);
				apw.start();		
				break;
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub		
	}
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	private ChiptuneSample getChiptuneSample(int x, int y) {
		for(int i=0;i<samples.size();i++) {
			if(x >= samples.get(i).getPosX() && 
			   x <= samples.get(i).getPosX()+53 &&
			   y >= samples.get(i).getPosY() &&
			   y <= samples.get(i).getPosY() + 30) {
				return samples.get(i);
			} 
		}
		return null;
	}
	
	private Color getSampleColor(String filename) {
		String sub = filename.substring(2, filename.length());
		String[] spl = sub.split("\\\\");
		String name = spl[spl.length-1];
		for(int i=0;i<samples.size();i++) {
			if(samples.get(i).getFilename().compareTo(name)==0) {
				return samples.get(i).getColor();
			} 
		}
		return null;	
	}
}
