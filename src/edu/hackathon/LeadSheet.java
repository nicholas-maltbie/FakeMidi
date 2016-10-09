package edu.hackathon;

import java.awt.List;
import java.util.ArrayList;

/**
 * A Lead Sheet is a melody and collection of chords that a musician can use to 
 * improvise a song. 
 * 
 * A Lead Sheet has a
 * 	Time Signature
 *	Key
 *	Chord progression
 *	Melody
 *	Possibly Lyrics
 * 
 * A LeadSheet can be saved in a string in the format as follows:
 * Title, Composer, timeTop/timeBottom, key, chords, notes
 * 
 * Example of a test song:
 * 	"Old devil Moon, E.Y. Harburg, 2/2, F, Fw | Ebi. Ebs Ebi Ebs Ebq Gq, "
 * 	
 * 	a t(not;note) means note and note are tied
 * 
 * 	"The Entertainer, Scott Joplin, 4/4, C, Ei C+q Ei C+q Ei t(C+i;C+q) Rq Ri C+i D+i D#+i E+i C+i D+i t(E+i;E+i) B+i D+q C+q,
 * 		Ch Cmaj7h Fw Ch Gmaj7h C"
 * 
 * notes are saved as follows: notelength
 * 	note or rest, for example:
 * 		C (middle C)
 * 		C+ (C one octave up)
 * 		C+3 (C three octaves up)
 * 		C- (C one octave down)
 * 		Bf (B flat)
 * 		R (rest)
 * 		E# (E sharp)
 * 	length is the type of note (A . after the letter means a dotted note):
 * 		whole is w	(4 beats)
 * 		half h		(2 beats)
 * 		quarter q	(1 beat)
 * 		eighth i	(1/2 beat)
 * 		sixteenth s	(1/4 beat)
 * 		thirty-second t
 * 		sixty-fourth x
 * 		one-twenty-eighth o
 * 
 * chords are saved as follows: {chord_name:length, chord_name:length, r, ...}
 *	chord_name is the name of the chord, for example:
 * 		C (C major)
 * 		Bb (B flat major)
 *  	Gm (G minor)
 *  	G#m	(G# minor)
 *  	Bbmaj7 (B flat mojor 7)
 *  	F#min (F sharp minor)
 *  length is the same as before
 *	length is the length in beats until the next chord occurs
 * 
 * @author Nicholas Maltbie
 *
 */
public class LeadSheet {
	
	private int timeTop, timeBottom;
	private String title, composer, key;
	private ArrayList<ChordEvent> chordProgression;
	private ArrayList<NoteEvent> melody;
	
	public static final int TICKS_PER_BEAT = 24;
	public static final int DEFAULT_VELOCITY = 60;
	
	public LeadSheet(String leadSheet) {
		String[] parts = leadSheet.split(",");
		title = parts[0].trim();
		composer = parts[1].trim();
		String timeSignature = parts[2].trim();
		timeTop = Integer.parseInt(timeSignature.split("/")[0].trim());
		timeBottom = Integer.parseInt(timeSignature.split("/")[1].trim());
		key = parts[3].trim();
		
		melody = new ArrayList<>();
		for (String noteString : parts[4].trim().split(" ")) {
			int typeLength = 1;
			if (noteString.charAt(noteString.length() - 1) == '.')
				typeLength = 2;
			String note = noteString.substring(0, noteString.length() - typeLength);
			String type = noteString.substring(noteString.length() - typeLength);
			Note noteValue = new Note(note);
			int ticks = getTicks(type.substring(0, 1));
			if (typeLength == 2)
				ticks = (int)(ticks * 1.5);
			melody.add(new NoteEvent(ticks, 60, noteValue));
		}
		
		chordProgression = new ArrayList<>();
		for (String chordString : parts[5].trim().split(" ")) {
			int typeLength = 1;
			if (chordString.charAt(chordString.length() - 1) == '.')
				typeLength = 2;
			chordString = chordString.substring(0, chordString.length() - typeLength);
			String type = chordString.substring(chordString.length() - typeLength);
			int ticks = getTicks(type.substring(0, 1));
			if (typeLength == 2)
				ticks = (int)(ticks * 1.5);
			
			String numValue = "";
			if (Character.isDigit(chordString.charAt(chordString.length() - 1))) {
				numValue = chordString.substring(chordString.length() - 1);
				chordString = chordString.substring(0, chordString.length() - 1);
			}
			String noteValue = "";
			int noteLength = 1;
			if (chordString.charAt(1) == '#' || chordString.charAt(1) == 'b') 
				noteLength = 2;
			noteValue = chordString.substring(0, noteLength);
			chordString = chordString.substring(noteLength);
			
			String typeValue = "maj";
			if (chordString.equals("min") || chordString.equals("m"))
				typeValue = "min";
			
			chordString = noteValue + ":" + typeValue;
			if (!numValue.isEmpty())
				chordString += ":" + numValue;
			
			chordProgression.add(new ChordEvent(ticks, 60, new Chord(chordString)));
		}
		
	}
	
	public static int getTicks(String noteType) {
		char tickchar = noteType.charAt(noteType.length() - 1);
		switch (tickchar) {
			case 'w': return 24 * 4;
			case 'h': return 24 * 2;
			case 'q': return 24;
			case 'i': return 24 / 4;
			case 's': return 24 / 8;
			case 't': return 1;
			case 'x': return 1;
			case 'o': return 1;
			default: return 1;
		}
	}
	
	public ChordEvent[] getChordProgression() {
		return chordProgression.toArray(new ChordEvent[chordProgression.size()]);
	}
	
	public NoteEvent[] getMelody() {
		return melody.toArray(new NoteEvent[melody.size()]);
	}
}
