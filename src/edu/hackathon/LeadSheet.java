package edu.hackathon;

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
 * 	"Old devil Moon, E.Y. Harburg, 2/2, F, Fw | Ebi. Ebs Ebi Ebs Ebq Gq, 
 * 
 * notes are saved as follows: note
 * 	note or rest, for example:
 * 		C (middle C)
 * 		C+ (C one octave up)
 * 		C+3 (C three octaves up)
 * 		C- (C one octave down)
 * 		Bf (B flat)
 * 		r (rest)
 * 		E# (E sharp)
 * 
 * chords are saved as follows: {chord_name:length, chord_name:length, r, ...}
 *	chord_name is the name of the chord, for example:
 * 		C (C major)
 * 		Bf (B flat major)
 *  	Gm (G minor)
 *  	Bfmaj7 (B flat mojor 7)
 *	length is the length in beats until the next chord occurs
 * 
 * @author Nicholas Maltbie
 *
 */
public class LeadSheet {
	
	private int timeTop, timeBottom;
	private String title, composer, key, chordProgression, melody;
	
	public LeadSheet(String leadSheet) {
		
	}
}
