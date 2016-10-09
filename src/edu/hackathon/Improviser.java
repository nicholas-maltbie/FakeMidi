package edu.hackathon;

public class Improviser {

	
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
		for(NoteEvent event : inspiration.getMelody()) {
			helper.addNote(event.getNote(), event.getTicks(), tick, event.getVelocity());
			while(chordTick <= tick) {
				ChordEvent c = progression[chordIndex];
				c.addSelfToMidi(helper, chordTick);
				chordIndex++;
				chordTick += c.getTicks();
			}
			tick += event.getTicks();
			
		}
		helper.writeToFile(title);
		
	}
	
	
	public static void main(String[] args) {
		LeadSheet entertainer = new LeadSheet("The Entertainer, Scott Joplin, 4/4, C, Ei C+q Ei C+q Ei t(C+i;C+q) Rq Ri C+i D+i D#+i E+i C+i D+i t(E+i;E+i) B+i D+q C+q, Ch Cmaj7h Fw Ch Gmaj7h C");
		Improviser joplin = new Improviser(entertainer);
		joplin.makeMusic("EntertainingSong");
	
	}
}
