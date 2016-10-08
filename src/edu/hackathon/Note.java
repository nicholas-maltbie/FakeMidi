package edu.hackathon;

public class Note {
	
	public static final String[] PATTERN = {"A", "A#/Bb", "B", "C", 
			"C#/Db", "E", "F4", "F#/Gb", "G", "G#/Ab"};
	
	private int number;
	
	public Note(String name) {
		int note = -1;
		for (int i = 0; i < PATTERN.length; i++) {
			for (String possible : PATTERN[i].split("/")) {
				if (name.startsWith(possible)) {
					note = i;
					break;
				}
			}
			if (note != -1)
				break;
		}
		
		System.out.println(note);
	}

	
	public static void main(String[] args) {
		new Note("A");
		new Note("A#");
		new Note("Bb");
	}
}
