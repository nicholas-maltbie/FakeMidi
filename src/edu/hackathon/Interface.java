package edu.hackathon;

import java.io.File;
import java.util.Scanner;

import javax.sound.midi.*;

public class Interface {
	public static final int NOTE_ON = ShortMessage.NOTE_ON;
    public static final int NOTE_OFF = ShortMessage.NOTE_OFF;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public static void main(String[] args) throws Exception {
    	Scanner scanner = new Scanner(System.in);
    	
    	System.out.println("What is the name of your file? (include the .mid)");
    	
        Sequence sequence = MidiSystem.getSequence(new File(scanner.nextLine()));

        int trackNumber = 0;
        
        System.out.println("Which track number has the melody? (0 - " + (sequence.getTracks().length - 1) + ") ");
        
        Track track = sequence.getTracks()[scanner.nextInt()];
        
        System.out.println("Track " + trackNumber + ": size = " + track.size());
        System.out.println();
        
        System.out.println("Tempo is: " + sequence.getResolution());
        int melodyNote = -1;
        
        String melody = "";
        
        long start_tick = 0;
        long ticks = 0;
        for (int i=0; i < track.size(); i++) { 
            MidiEvent event = track.get(i);
            //System.out.print("@" + event.getTick() + " ");
            ticks = event.getTick();
            MidiMessage message = event.getMessage();
            if (message instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) message;
                //System.out.print("Channel: " + sm.getChannel() + " ");
                if (sm.getCommand() == NOTE_OFF) {
                    int key = sm.getData1();
                    int octave = (key / 12)-1;
                    int note = key % 12;
                    String noteName = NOTE_NAMES[note];
                    int velocity = sm.getData2();
                    int offNote = key;
                    if (melodyNote == offNote) {melodyNote = -1;
                    }
                    //System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                } else if (sm.getCommand() == NOTE_ON) {
                    int key = sm.getData1();
                    int octave = (key / 12)-1;
                    int note = key % 12;
                    String noteName = NOTE_NAMES[note];
                    int velocity = sm.getData2();
                    if(melodyNote != -1)
                    	melody += NOTE_NAMES[melodyNote % 12] + getNoteEquivOctave(melodyNote/12 - 1) + getNoteType((double)(ticks - start_tick) / sequence.getResolution()) + " ";
                    start_tick = ticks;
                    melodyNote = key;
                }
            } else {
                //System.out.println("Other message: " + message.getClass());
            }
            
            //System.out.println();
        }
        
        System.out.println(melody);

    }
    
    public static String getNoteType(double beats) {

    	if (beats >= 6)
    		return "w.";
    	else if (beats >= 4)
    		return "w";
    	else if (beats >= 3)
    		return "h.";
    	else if (beats >= 2)
    		return "h";
    	else if (beats >= 1.5)
    		return "q.";
    	else if (beats >= 1)
    		return "q";
    	else if (beats >= .75)
    		return "i.";
    	else if (beats >= .5)
    		return "i";
    	return "s";
    }

    public static String getNoteEquivOctave(int octave) {
    	octave -= 4;
    	if (octave > 1) {
    		return "+" + octave;
    	} else if (octave > 0) {
    		return "+";
    	} else if (octave < 0) {
    		return "-";
    	} else if (octave < -1) {
    		return "-" + octave;
    	}
    	return "";
    }
    
}
