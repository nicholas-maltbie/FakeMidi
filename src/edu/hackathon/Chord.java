package edu.hackathon;

import java.util.*;

public class Chord {
	
	private List<Note> notes;
	
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
		String[] parts = name.split(":");	
		Note note = new Note(parts[0]);	
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
	
	public Note[] getNotes() {
		Note[] array = new Note[notes.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = notes.get(i);
		}
		return array;
	}
}
