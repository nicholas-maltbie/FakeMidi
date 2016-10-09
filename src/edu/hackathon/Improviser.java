package edu.hackathon;

public class Improviser {

	public static final int COLORFUL_NOTE_LENGTH = 6;
	
	private LeadSheet inspiration;
	
	public Improviser(LeadSheet leadSheet) {
		inspiration = leadSheet;
	}
	
	public void makeMusic(String title) {
		AidedMidi helper = new AidedMidi();
		
		int tick = 0;
		int chordIndex = 0;
		int chordTick = 0;
		ChordEvent[] progression = inspiration.getChordProgression();
		Note nonRest = null;
		NoteEvent[] melody = inspiration.getMelody();
		int i = 0;
		while(nonRest == null && i < melody.length) {
			nonRest = melody[i].getNote();
			i++;
		}
		
		for(int note = 0; note < melody.length; note++) {
			NoteEvent event = melody[note];
			
			NoteEvent next = null;
			if (note < melody.length - 1)
				next = melody[note + 1];
			Note current = event.getNote();
			

			if (!event.getNote().isRest()) { 

				//If next three notes are all ascending or descending, add a color note
				
				if (isAscending(melody, note, 4) || isDescending(melody, note, 4)) {
					Note colorful = getColorNote(melody, note);
					//System.out.println(colorful);
					helper.addNote(colorful, 10, tick - COLORFUL_NOTE_LENGTH / 2, 60);
				}
				helper.addNote(event.getNote(), event.getTicks(), tick, event.getVelocity());
			}
			
			while(chordTick <= tick && chordIndex < progression.length) {
				ChordEvent c = progression[chordIndex];
				if (!c.getChord().isBreak()) {
					if (next != null && next.getNote().isRest()) {
						int added = playBrokenChord(c, tick, helper, Direction.UP_DOWN, StyleTiming.MIDDLE);
						added = added * 10 / 7;
						chordTick += added;
						tick += added;
					}
					else {
						playChordRightHand(c, tick, nonRest, helper);
						addRootOctave(c, chordTick, nonRest.getOctaveDown(), helper);
					}
					//c.addSelfToMidi(helper, chordTick);
				}
				chordIndex++;
				chordTick += c.getTicks();
				//At the end of a song, mix it up a bit!
				if (next == null) {
					playEndingInversions(melody, helper, c, chordTick + 4);
				}
			}
			
			if (!current.isRest())
				nonRest = current;
			tick += event.getTicks();
			
		}
		helper.writeToFile(title);
	}
	
	enum Direction {UP, DOWN, UP_DOWN, DOWN_UP};
	enum StyleTiming {FIRST, LAST, MIDDLE};
	
	public boolean isDescending(NoteEvent[] melody, int index, int length) {
		if (index + length > melody.length)
			return false;
		for (int i = index; i < index + length - 1; i++) {
			if (melody[i].getNote().getNumber() <= melody[i + 1].getNote().getNumber())
				return false;
		}
		return true;
	}
	
	public boolean isAscending(NoteEvent[] melody, int index, int length) {
		if (index + length > melody.length)
			return false;
		for (int i = index; i < index + length; i++) {
			if (melody[i].getNote().getNumber() >= melody[i + 1].getNote().getNumber())
				return false;
		}
		return true;
	}
	
	public boolean isBeforeChange(NoteEvent[] melody, int index) {
		if (index == 0 || index == (melody.length - 1))
			return false;
		int currentNote = melody[index].getNote().getNumber();
		int noteBefore = melody[index - 1].getNote().getNumber();
		int noteAfter = melody[index + 1].getNote().getNumber();
		if (noteBefore == currentNote || noteAfter == currentNote || noteBefore >= currentNote && currentNote >= noteAfter || noteBefore <= currentNote && currentNote <= noteAfter)
			return false;
		return true;
	}
	
	public Note getColorNote(NoteEvent[] melody, int index) {
		int generalDir = 0;
		for(int l = melody.length - 1; l > 1 && l > melody.length - 5; l--) {
			generalDir += melody[l - 1].getNote().getDist(melody[l].getNote());
		}
		Note currentNote = melody[index].getNote();
		if (generalDir < 0)
			currentNote = currentNote.getHalfStepDown();
		if (generalDir > 0)
			currentNote = currentNote.getHalfStepUp();
		return currentNote;
	}
	
	public void playEndingInversions(NoteEvent[] melody, AidedMidi helper, ChordEvent last, int tick) {

			//Get the melody direction for the last 5 notes
			int generalDir = 0;
			for(int l = melody.length - 1; l > 1 && l > melody.length - 5; l--) {
				generalDir += melody[l - 1].getNote().getDist(melody[l].getNote());
			}
			
			ChordEvent[] funChords = new ChordEvent[3];
			Chord chord = last.getChord();
			
			if (generalDir < 0) {
				chord = chord.getInversionDown(1);
				for(int i = 0; i < funChords.length; i++) {
					funChords[i] = new ChordEvent(last.getTicks() * (i) / 5, last.getVelocity() * (i + 2) / 2, chord);
					chord = chord.getInversionUp(2);
				}
			}
			if (generalDir > 0) {
				chord = chord.getInversionUp(1);
				for(int i = 0; i < funChords.length; i++) {
					funChords[i] = new ChordEvent(last.getTicks() * (i) / 5, last.getVelocity() * (i + 2) / 2, chord);
					chord = chord.getInversionDown(2);
				}
			}
			
			for (ChordEvent event : funChords) {
				event.addSelfToMidi(helper, tick);
				tick += event.getTicks() - 2;
			}
	}
	
	public int playBrokenChord(ChordEvent chord, int tick, AidedMidi helper, Direction dir, StyleTiming timing) {
		int ticks = chord.getTicks() / (chord.getChord().getNotes().length + 1);
		int added = -chord.getTicks();
		
		Note[] notes = chord.getChord().getNotes();
		
		if (dir == Direction.UP) {
			//do nothing, this is already done for us
		} else if (dir == Direction.DOWN) {
			Note[] reversed = new Note[notes.length];
			for(int i = 0; i < reversed.length; i++)
				reversed[i] = notes[notes.length - 1 - i];
			notes = reversed;
		} else if (dir == Direction.UP_DOWN) {
			Note[] notesPlusOne = new Note[notes.length + 1];
			for(int i = 0; i < notes.length; i++) 
				notesPlusOne[i] = notes[i];
			notesPlusOne[notesPlusOne.length - 1] = notes[notes.length - 1];
			notes = notesPlusOne;
		} else if (dir == Direction.UP_DOWN) {
			Note[] reversedPlusOne = new Note[notes.length + 1];
			for(int i = 0; i < reversedPlusOne.length; i++)
				reversedPlusOne[i] = notes[notes.length - 1 - i];
			reversedPlusOne[reversedPlusOne.length - 1] = notes[1];
			notes = reversedPlusOne;
		}
		
		for(int i = 0; i < notes.length; i++) {
			int len = ticks;
			if (timing == StyleTiming.FIRST && i == 0) {
				len *= 2;
			} else if (timing == StyleTiming.LAST && i == notes.length - 1) {
				len *= 2;
			} else if (timing == StyleTiming.MIDDLE && i > 0 && i < notes.length) {
				len = len * 5 / 4;
			}
			helper.addNote(notes[i], len, tick, chord.getVelocity() * 3 / 4);
			
			tick += len;
			added += len;
		}
		
		return added;
		
	}
	
	public void playChordRightHand(ChordEvent chord, int tick, Note high, AidedMidi helper) {

		Note[] chordNotes = chord.getChord().getNotes();
		for (int k = 1; k < chordNotes.length; k++) {
			Note note = chordNotes[k];
			while (note.getNumber() > high.getNumber()) {
				note = note.getOctaveDown();
			}
			helper.addNote(note, chord.getTicks(), tick, chord.getVelocity() * 3 / 5);
		}
	}
	
	public void addRootOctave(ChordEvent chord, int tick, Note high, AidedMidi helper) {
		Note root = chord.getChord().getRoot();
		while(root.getNumber() > high.getNumber()) {
			root = root.getOctaveDown();
		}
		root.getOctaveDown();
		helper.addNote(root, chord.getTicks(), tick, chord.getVelocity() * 3 / 5);
		helper.addNote(root.getOctaveDown(), chord.getTicks(), tick, chord.getVelocity() * 3 / 5);
	}
	
	
	public static void main(String[] args) {
		LeadSheet entertainer = new LeadSheet("The Entertainer, Scott Joplin, 4/4, C," 
				+ "Di Eq C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. Di D#i Ei C+q Ei C+q Ei C+h. Ai Gi F#i Ai C+i E+q D+i C+i Ai D+h. Di D#i Ei C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. D+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q Bi D+q C+h. Di D#q C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. Di D#i Ei C+q Ei C+q Ei C+h. Ai Gi F#i Ai C+i E+q D+i C+i Ai D+h. Di D#i Ei C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. D+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q Bi D+q C+h Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i Gi E+i F+i G+i A+i G+i E+i F+i G+q A+i G+q E+i F+i F#+i G+q A+i G+q. A+i A#+i B+h A+i F#+i D+i G+h Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i C+h Gi F#i Gi C+q Ai C+q Ai C+i Ai Gi C+i E+i G+q E+i C+i Gi Aq C+q E+i D+q C+h. Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i Gi E+i F+i G+i A+i G+i E+i F+i G+q A+i G+q E+i F+i F#+i G+q A+i G+q. A+i A#+i B+h A+i F#+i D+i G+h Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i C+h Gi F#i Gi C+q Ai C+q Ai C+i Ai Gi C+i E+i G+q E+i C+i Gi Aq C+q E+i D+q C+h. Di D#q C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. Di D#i Ei C+q Ei C+q Ei C+h. Ai Gi F#i Ai C+i E+q D+i C+i Ai D+h. Di D#i Ei C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. D+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q Bi D+q C+h, "
				//+ " Ci C#i Ei C+q Ei C+q Ei t(C+i;C+q) Rq Ri C+i D+i D#+i E+i C+i D+i t(E+i;E+i) Bi D+q C+q,"
				+ " Rq Ch C7h Fw Ch Gmaj7h Cw");
		Improviser joplin = new Improviser(entertainer);
		joplin.makeMusic("EntertainingSong.mid");
	
	}
	
	public double getInterest(NoteEvent[] melody, int index) {
		int currentVelocity = melody[index].getVelocity();
		double final_interest = 0;
		double low_interest = .02;
		double high_interest = .05;
		
		// Get the general direction.
		int generalDir = 0;
		for(int l = melody.length - 1; l > 1 && l > melody.length - 5; l--) {
			generalDir += melody[l - 1].getNote().getDist(melody[l].getNote());
		}
		
		// Add weights based on general direction.
		if (generalDir < 0) final_interest -= low_interest;
		if (generalDir > 0)	final_interest += high_interest;
		if (melody[index].getNote().getNumber() > melody[index - 1].getNote().getNumber())
			final_interest += high_interest;
		
		// Add weights based on repetitions in the last 10 notes
		boolean isRepeated = false;
		for (int i = 0; i < 10; i--) {
			if (i < 0) break;
			if (melody[index] == melody[index - i - 1])	isRepeated = true;
		}
		if (isRepeated) final_interest += high_interest;
		else final_interest -= low_interest;
		
		// Add weights based on whether it's before or after a rest.
		if (melody[index - 1].getNote().isRest()) final_interest -= low_interest * 2.5;
		if (melody[index + 1].getNote().isRest()) final_interest += high_interest;
		
		// Add weights based on whether the note's at the end of the song.
		if (index == melody.length - 1) final_interest += high_interest;
		
		return final_interest;	
	}
}
