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
			if (!event.getNote().isRest())
				helper.addNote(event.getNote(), event.getTicks(), tick, event.getVelocity());
			while(chordTick <= tick) {
				ChordEvent c = progression[chordIndex];
				if (!c.getChord().isBreak()) {
					if (next != null && next.getNote().isRest()) {
						int added = playBrokenChord(c, tick, helper, Direction.UP_DOWN, StyleTiming.MIDDLE);
						added = added * 8 / 7;
						chordTick += added;
						tick += added;
					}
					else {
						playChordRightHand(c, tick, nonRest, helper);
						addRootOctave(c, chordTick, nonRest, helper);
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
			
			//If next three notes are all ascending or descending, add a color note
			
			if (isAscending(melody, note, 3) || isDescending(melody, note, 3)) {
				Note colorful = getColorNote(melody, note);
				helper.addNote(colorful, COLORFUL_NOTE_LENGTH, tick - COLORFUL_NOTE_LENGTH / 3, melody[note].getVelocity() * 4 / 5);
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
		for (int i = index; i < index + length; i++) {
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
		if (noteBefore != currentNote && noteAfter != currentNote && ((noteBefore >= currentNote && currentNote >= noteAfter) || (noteBefore <= currentNote && currentNote <= noteAfter)))
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
			while (note.getNumber() > high.getNumber()) 
				note = note.getOctaveDown();
			helper.addNote(note, chord.getTicks(), tick, chord.getVelocity() * 3 / 4);
		}
	}
	
	public void addRootOctave(ChordEvent chord, int tick, Note high, AidedMidi helper) {
		Note root = chord.getChord().getRoot();
		while(root.getNumber() > high.getNumber()) {
			root.getOctaveDown();
		}
		root.getOctaveDown();
		helper.addNote(root, chord.getTicks(), tick, chord.getVelocity() * 3 / 4);
		helper.addNote(root.getOctaveDown(), chord.getTicks(), tick, chord.getVelocity() * 3 / 4);
	}
	
	
	public static void main(String[] args) {
		LeadSheet entertainer = new LeadSheet("The Entertainer, Scott Joplin, 4/4, C,"
				+ " Ci C#i Ei C+q Ei C+q Ei t(C+i;C+q) Rq Ri C+i D+i D#+i E+i C+i D+i t(E+i;E+i) Bi D+q C+q,"
				+ " Rq Ch C7h Fw Ch Gmaj7h Cw");
		Improviser joplin = new Improviser(entertainer);
		joplin.makeMusic("EntertainingSong.mid");
	
	}
}
