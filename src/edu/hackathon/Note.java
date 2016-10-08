package edu.hackathon;

import java.math.*;

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
	
	public Note(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
	
	public double getFrequency() {
		return Math.pow(2, (number - 49) / 12) * 440;
	}
	
	public Note getWholeStepUp() {
		return new Note(number + 2);
	}
	
	public Note getWholeStepDown() {
		return new Note(number - 2);
	}
	
	public Note getHalfStepUp() {
		return new Note(number + 1);
	}
	
	public Note getHalfStepDown() {
		return new Note(number - 1);
	}

	
	public static void main(String[] args) {
		new Note("A");
		new Note("A#");
		new Note("Bb");
	}
}
