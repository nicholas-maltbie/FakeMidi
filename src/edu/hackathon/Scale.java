package edu.hackathon;

public class Scale {
	public enum Type {
		MAJOR, NATURAL_MINOR, HARMONIC_MINOR, MELODIC_MINOR
	}
	private Note[] notes;
	private Type type;
	
	public Scale(Note note, Type type) {
		this.type = type;
		notes = new Note[8];
		notes[0] = note;
		if (type == Type.MAJOR) {
			notes[1] = notes[0].getWholeStepUp();
			notes[2] = notes[1].getWholeStepUp();
			notes[3] = notes[2].getHalfStepUp();
			notes[4] = notes[3].getWholeStepUp();
			notes[5] = notes[4].getWholeStepUp();
			notes[6] = notes[5].getWholeStepUp();
			notes[7] = notes[6].getHalfStepUp();
		}
		if (type == Type.NATURAL_MINOR) {
			notes[1] = notes[0].getWholeStepUp();
			notes[2] = notes[1].getHalfStepUp();
			notes[3] = notes[2].getWholeStepUp();
			notes[4] = notes[3].getWholeStepUp();
			notes[5] = notes[4].getHalfStepUp();
			notes[6] = notes[5].getWholeStepUp();
			notes[7] = notes[6].getWholeStepUp();
		}
		if (type == Type.HARMONIC_MINOR) {
			notes[1] = notes[0].getWholeStepUp();
			notes[2] = notes[1].getHalfStepUp();
			notes[3] = notes[2].getWholeStepUp();
			notes[4] = notes[3].getWholeStepUp();
			notes[5] = notes[4].getHalfStepUp();
			Note temp = notes[5].getWholeStepUp();
			notes[6] = temp.getHalfStepUp();
			notes[7] = notes[6].getHalfStepUp();
		}
		if (type == Type.MELODIC_MINOR) {
			notes[1] = notes[0].getWholeStepUp();
			notes[2] = notes[1].getHalfStepUp();
			notes[3] = notes[2].getWholeStepUp();
			notes[4] = notes[3].getWholeStepUp();
			notes[5] = notes[4].getWholeStepUp();
			notes[6] = notes[5].getWholeStepUp();
			notes[7] = notes[6].getHalfStepUp();
		}
	}
	
	public Note getNote(int offset) {
		if (offset >= 9) {
				return new Scale(notes[0].getOctaveUp(), type).getNote(offset - 7);
		}
		return notes[offset - 1];
	}
	
}