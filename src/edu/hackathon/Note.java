package edu.hackathon;

import java.math.*;

public class Note {
	
	public static final String[] PATTERN = {"C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", "B"};
	
	private int number;
	
	public Note(String name) {
		int note = -1;
		String start = name.substring(0, 1);
		if(name.indexOf('#') > -1 || name.indexOf('b') > -1)
			start = name.substring(0, 2);
		for (int i = 0; i < PATTERN.length; i++) {
			for (String possible : PATTERN[i].split("/")) {
				if (start.equals(possible)) {
					note = i;
					break;
				}
			}
			if (note != -1)
				break;
		}
		
		int octave = 0;
		if(name.indexOf('-') > -1) {
			String rest = name.substring(name.indexOf('-')+1);
			if (rest.isEmpty())
				octave = -1;
			else
				octave = -Integer.parseInt(rest);
		}
		if(name.indexOf('+') > -1) {
			String rest = name.substring(name.indexOf('+')+1);
			if (rest.isEmpty())
				octave = 1;
			else
				octave = Integer.parseInt(rest);
		}
		
		number = 40 + octave * 12 + note;
	}
	
	public Note(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
	
	public double getFrequency() {
		return Math.pow(2, (number - 49.0) / 12) * 440;
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

	public Note getOctaveUp() {
		return new Note(number + 12);
	}
	
	public Note getOctaveDown() {
		return new Note(number - 12);
	}
	
	
	public static void main(String[] args) {
		System.out.println(new Note("Eb+3").getFrequency());
		System.out.println(new Note("C#").getFrequency());
		System.out.println(new Note("Db").getFrequency());
		System.out.println(new Note("D").getFrequency());
		System.out.println(new Note("D#").getFrequency());
	}
}
