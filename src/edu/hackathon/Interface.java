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
        
        int chordBeat = 0;
        int beats = 0;
        
        String chords = "";
        String queue = "";
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
                    if (melodyNote == offNote) {
                    	String noteNameThing =  NOTE_NAMES[melodyNote % 12] + getNoteEquivOctave(melodyNote/12 - 1) + 
                    			getNoteType((double)(ticks - start_tick) / sequence.getResolution()) + " ";
                    	melody += noteNameThing;
                    	melodyNote = -1;
                    }
                    //System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                } else if (sm.getCommand() == NOTE_ON) {
                    int key = sm.getData1();
                    int octave = (key / 12)-1;
                    int note = key % 12;
                    String noteName = NOTE_NAMES[note];
                    int velocity = sm.getData2();
                    if(key != melodyNote) {
	                    if(melodyNote != -1)
	                    {
	                    	String noteNameThing =  NOTE_NAMES[melodyNote % 12] + getNoteEquivOctave(melodyNote/12 - 1) + 
	                    			getNoteType((double)(ticks - start_tick) / sequence.getResolution()) + " ";
	                    	melody += noteNameThing;
	                    	/*queue += noteNameThing;
                			beats += (int)((double)(ticks - start_tick) / sequence.getResolution());
                			if (beats > chordBeat) {
                				System.out.println("After \'" + queue + "\', what chord is played?");
                				String chordName = scanner.next();
                				System.out.println("How many beats does " + chordName + " play for?");
                				int count = scanner.nextInt();
                				beats += count * sequence.getResolution();
                				chords += chordName + getNoteType(count) + " ";
                				queue = "";
                			}*/
	                    }
	                    
	                    start_tick = ticks;
	                    melodyNote = key;
                    }
                }
            } else {
                //System.out.println("Other message: " + message.getClass());
            }
            
            //System.out.println();
        }

        System.out.println("Give me the title! ");
        String title = scanner.nextLine();
        
        System.out.println("Give me the composer! ");
        String composer = scanner.nextLine();
        
        System.out.println("Give me the time signature! (top/bottom) ");
        String time = scanner.nextLine();
        
        System.out.println("Give me the key signature! " );
        String key = scanner.nextLine();
        
		LeadSheet entertainer = new LeadSheet(title + ", " + composer + ", " + time + ", " + key + ", " + melody + ", " + chords);
				// + "Di Eq C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. Di D#i Ei C+q Ei C+q Ei C+h. Ai Gi F#i Ai C+i E+q D+i C+i Ai D+h. Di D#i Ei C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. D+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q Bi D+q C+h. Di D#q C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. Di D#i Ei C+q Ei C+q Ei C+h. Ai Gi F#i Ai C+i E+q D+i C+i Ai D+h. Di D#i Ei C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. D+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q Bi D+q C+h Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i Gi E+i F+i G+i A+i G+i E+i F+i G+q A+i G+q E+i F+i F#+i G+q A+i G+q. A+i A#+i B+h A+i F#+i D+i G+h Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i C+h Gi F#i Gi C+q Ai C+q Ai C+i Ai Gi C+i E+i G+q E+i C+i Gi Aq C+q E+i D+q C+h. Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i Gi E+i F+i G+i A+i G+i E+i F+i G+q A+i G+q E+i F+i F#+i G+q A+i G+q. A+i A#+i B+h A+i F#+i D+i G+h Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i C+h Gi F#i Gi C+q Ai C+q Ai C+i Ai Gi C+i E+i G+q E+i C+i Gi Aq C+q E+i D+q C+h. Di D#q C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. Di D#i Ei C+q Ei C+q Ei C+h. Ai Gi F#i Ai C+i E+q D+i C+i Ai D+h. Di D#i Ei C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. D+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q Bi D+q C+h, "
				//+ " Ci C#i Ei C+q Ei C+q Ei t(C+i;C+q) Rq Ri C+i D+i D#+i E+i C+i D+i t(E+i;E+i) Bi D+q C+q,"
				//+ " Rq Ch C7h Fw Ch Gmaj7h Cw");
		Improviser joplin = new Improviser(entertainer);
		System.out.println("Give me an output file name (include .mid)!" );
		joplin.makeMusic(scanner.nextLine());
        
        System.out.println(title + ", " + composer + ", " + time + ", " + key + ", " + melody + ", " + chords);
        
        
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
