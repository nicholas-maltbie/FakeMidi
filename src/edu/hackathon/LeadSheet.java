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
 * 	length is the type of note:
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
 * 		Bf (B flat major)
 *  	Gm (G minor)
 *  	Bfmaj7 (B flat mojor 7)
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
			
		}
		
	}
	
	public ChordEvent[] getChordProgression() {
		return chordProgression.toArray(new ChordEvent[chordProgression.size()]);
	}
	
	public NoteEvent[] getMelody() {
		return melody.toArray(new NoteEvent[melody.size()]);
	}
}
