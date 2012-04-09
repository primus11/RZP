import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class WavAppender {
	
	public static void append() throws UnsupportedAudioFileException, IOException {	
		
		File folder = new File("./chiptune").getCanonicalFile();
	    File[] listOfFiles = folder.listFiles();
	    
	    for(int i=0;i<listOfFiles.length;i++){
	    	File f = listOfFiles[i];
	    	if(f.isFile()) {
	    		System.out.println(f.getName());		
	    	}    	
	    }/*
		AudioInputStream clip1 = AudioSystem.getAudioInputStream(new File("Arcade Echo FX 001.wav"));
        AudioInputStream clip2 = AudioSystem.getAudioInputStream(new File("Bass 001.wav"));

        AudioInputStream appendedFiles = 
                new AudioInputStream( 
                    new SequenceInputStream(clip1, clip2),     
                    clip1.getFormat(), 
                    clip1.getFrameLength() + clip2.getFrameLength());

        AudioSystem.write(appendedFiles, 
                AudioFileFormat.Type.WAVE, 
                new File("D:\\wavAppended.wav"));*/
	}
}
