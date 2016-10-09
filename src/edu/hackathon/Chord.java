package edu.hackathon;

import java.util.*;

public class Chord {
	
	private List<Note> notes;
	private Note root;
	private boolean isBreak;
	
	/**
	 * root:type
	 * Root:type:7
	 * 
	 * maj = MAJOR
	 * min = NATURAL_MINOR
	 * 
	 * @param notes
	 */
	public Chord(String name) {
		if (name.startsWith("R")) {
			isBreak = true;
			notes = new ArrayList<>();
			return;
		}
		String[] parts = name.split(":");	
		Note note = new Note(parts[0]);	
		root = note;
		Scale.Type type = Scale.Type.MAJOR;
		if (parts[1].equals("min"))
			type = Scale.Type.NATURAL_MINOR;
		Scale scale = new Scale(note, type);
		notes = new ArrayList<>();	
		notes.add(scale.getNote(1));
		notes.add(scale.getNote(3));
		notes.add(scale.getNote(5));
		if (parts.length > 2) 
			notes.add(scale.getNote(7));

	}
	
	public Note getRoot() {
		return root;
	}
	
	public boolean isBreak() {
		return isBreak;
	}
	
	public Note getHighestNote() {
		return notes.get(notes.size() - 1);
	}
	
	public Chord(List<Note> notes) {
		this.notes = notes;
	}
	
	public Note[] getNotes() {
		Note[] array = new Note[notes.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = notes.get(i);
		}
		return array;
	}
	
	public Chord getInversionUp(int number) {
		ArrayList<Note> copy = new ArrayList<Note>(notes);
		for (int i = 0; i < number; i++) {
			Note firstnote = copy.get(0);
			firstnote = firstnote.getOctaveUp();
			copy.remove(0);
			copy.add(firstnote);
		}	
		return new Chord(copy);	
	}
	
	public Chord getInversionDown(int number) {
		ArrayList<Note> copy = new ArrayList<Note>(notes);
		for (int i = 0; i < number; i++) {
			Note lastnote = copy.get(copy.size() - 1);
			lastnote = lastnote.getOctaveDown();
			copy.remove(copy.size() - 1);
			copy.add(0, lastnote);
		}	
		return new Chord(copy);	
	}
}
