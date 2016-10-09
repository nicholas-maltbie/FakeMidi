package edu.hackathon;

public class ChordEvent {

	private int ticks, velocity;
	private Chord chord;
	
	public ChordEvent(int ticks, int velocity, Chord chord) {
		this.ticks = ticks;
		this.velocity = velocity;
		this.chord = chord;
	}
	
	public int getTicks() {
		return ticks;
	}
	
	public int getVelocity() {
		return velocity;
	}
	
	public Chord getChord() {
		return chord;
	}
}
