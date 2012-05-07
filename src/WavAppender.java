import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Vector;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WavAppender {	
	@SuppressWarnings("unchecked")
	public static void append(ArrayList<String> t1, ArrayList<String> t2, ArrayList<String> t3) throws UnsupportedAudioFileException, IOException {	
	    Vector<AudioInputStream> clips1 = new Vector<AudioInputStream>();
	    AudioInputStream appendedFiles1 = null;
	    Vector<AudioInputStream> clips2 = new Vector<AudioInputStream>();
        AudioInputStream appendedFiles2 = null;
        Vector<AudioInputStream> clips3 = new Vector<AudioInputStream>();
        AudioInputStream appendedFiles3 = null;
	    int dolzina1 = 0;
	    int dolzina2 = 0;
	    int dolzina3 = 0;
	    AudioInputStream result = null;
        
	    if(t1.size() > 0) {
        for(int i=0;i<t1.size();i++) {
        	//System.out.println(t1.get(i).toString());
        	clips1.addElement(AudioSystem.getAudioInputStream(new File(t1.get(i).toString())));
        } 

        for(int i=0;i<clips1.size();i++) {
        	dolzina1 += clips1.get(i).getFrameLength();
        }
        appendedFiles1 = 
            new AudioInputStream(  
                new SequenceInputStream(clips1.elements()),     
                clips1.get(0).getFormat(), 
                dolzina1);
		}
        
        if(t2.size() > 0) {
        for(int i=0;i<t2.size();i++) {
        	//System.out.println(t2.get(i).toString());
        	clips2.addElement(AudioSystem.getAudioInputStream(new File(t2.get(i).toString())));
        } 

        for(int i=0;i<clips2.size();i++) {
        	dolzina2 += clips2.get(i).getFrameLength();
        }
        appendedFiles2 = 
            new AudioInputStream(  
                new SequenceInputStream(clips2.elements()),     
                clips2.get(0).getFormat(), 
                dolzina2);
        }
        
        if(t3.size() > 0) {
        for(int i=0;i<t3.size();i++) {
        	//System.out.println(t3.get(i).toString());
        	clips3.addElement(AudioSystem.getAudioInputStream(new File(t3.get(i).toString())));
        } 

        for(int i=0;i<clips3.size();i++) {
        	dolzina3 += clips3.get(i).getFrameLength();
        }
        appendedFiles3 = 
            new AudioInputStream(  
                new SequenceInputStream(clips3.elements()),     
                clips3.get(0).getFormat(), 
                dolzina3);
        }
       
        System.out.println(dolzina1);
        System.out.println(dolzina2);
        System.out.println(dolzina3);
        
        //this checks could be done better
        if (dolzina2 == 0)
        	result = appendedFiles1;
        else if (dolzina3 == 0)
        	result = merge(appendedFiles1, appendedFiles2);
        else
        	result = merge(merge(appendedFiles1, appendedFiles2), appendedFiles3);

		AudioSystem.write(
				result,
				AudioFileFormat.Type.WAVE,
				new File("D:\\wavAppended.wav")
		);
		
        System.out.println("File saved.");
	}
	
	static ByteArrayOutputStream getAudioBytes(AudioInputStream audio) {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		byte[] buf = new byte[1024];
		int c;
		try {
			while ((c = audio.read(buf, 0, buf.length)) != -1)
				result.write(buf, 0, c);
			
			//audio.reset();
			result.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	static AudioInputStream merge(AudioInputStream audio1, AudioInputStream audio2) {
		byte[] bytes1 = getAudioBytes(audio1).toByteArray();
		byte[] bytes2 = getAudioBytes(audio2).toByteArray();
		
		ByteArrayOutputStream byteResult = new ByteArrayOutputStream();
		
		int maxLength = Math.max(bytes1.length, bytes2.length);
		for (int i=0; i<maxLength; i++) {
			if (i >= bytes1.length)
				byteResult.write(bytes2[i]);
			else if (i >= bytes2.length)
				byteResult.write(bytes1[i]);
			else
				byteResult.write((bytes1[i]+bytes2[i]) / 2);
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(byteResult.toByteArray());
		
		return new AudioInputStream( bais, audio1.getFormat(), Math.max(audio1.getFrameLength(), audio2.getFrameLength()) );
	}
}
