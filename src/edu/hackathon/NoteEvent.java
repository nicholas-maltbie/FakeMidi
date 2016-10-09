package edu.hackathon;

public class NoteEvent {
	
	private int ticks, velocity;
	private Note note;
	
	public NoteEvent(int ticks, int velocity, Note note) {
		this.ticks = ticks;
		this.velocity = velocity;
		this.note = note;
	}
	
	public int getTicks() {
		return ticks;
	}
	
	public int getVelocity() {
		return velocity;
	}
	
	public Note getNote() {
		return note;
	}
}
