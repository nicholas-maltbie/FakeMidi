package edu.hackathon;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.*;

public class AidedMidi {
	
	private Sequence s;
	private Track t;
	private long end;
	
	public AidedMidi() {
		try {
			 s = new Sequence(javax.sound.midi.Sequence.PPQ,24);
	
	//****  Create a new MIDI sequence with 24 ticks per beat  ****
				
	//****  Obtain a MIDI track from the sequence  ****
			t = s.createTrack();
	
	//****  General MIDI sysex -- turn on General MIDI sound set  ****
			byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
			SysexMessage sm = new SysexMessage();
			sm.setMessage(b, 6);
			MidiEvent me = new MidiEvent(sm,(long)0);
			t.add(me);
	
	//****  set tempo (meta event)  ****
			MetaMessage mt = new MetaMessage();
	        byte[] bt = {0x02, (byte)0x00, 0x00};
			mt.setMessage(0x51 ,bt, 3);
			me = new MidiEvent(mt,(long)0);
			t.add(me);
	
	//****  set track name (meta event)  ****
			mt = new MetaMessage();
			String TrackName = new String("midifile track");
			mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
			me = new MidiEvent(mt,(long)0);
			t.add(me);
	
	//****  set omni on  ****
			ShortMessage mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7D,0x00);
			me = new MidiEvent(mm,(long)0);
			t.add(me);
	
	//****  set poly on  ****
			mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7F,0x00);
			me = new MidiEvent(mm,(long)0);
			t.add(me);
	
	//****  set instrument to Piano  ****
			mm = new ShortMessage();
			mm.setMessage(0xC0, 0x00, 0x00);
			me = new MidiEvent(mm,(long)0);
			t.add(me);
		}
		catch (Exception e) {
			System.out.println("Exception caught " + e.toString());
		}
	}
	
	public void addNote(Note note, long length, long start, int velocity) {

		start++;
		if (start + length + 24 > end) 
		{
			end = start + length + 24;
		}
//****  note on - middle C  ****
		try {
			ShortMessage mm = new ShortMessage();
			mm.setMessage(ShortMessage.NOTE_ON,(note.getNumber() + 20),velocity);
			MidiEvent me = new MidiEvent(mm,(long)start);
			t.add(me);
			mm = new ShortMessage();
			mm.setMessage(ShortMessage.NOTE_OFF,(note.getNumber() + 20),velocity);
			me = new MidiEvent(mm,(long)(start + length));
			t.add(me);
			System.out.println(start);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeToFile(String file) {
		File f = new File(file);
		try {
			MetaMessage mt = new MetaMessage();
	        byte[] bet = {}; // empty array
			mt.setMessage(0x2F,bet,0);
			MidiEvent me = new MidiEvent(mt, end);
			t.add(me);
			
			MidiSystem.write(s,1,f);
			
			t.remove(me);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		AidedMidi help = new AidedMidi();
		//help.addNote(new Note("C"), 24 * 4, 0, 60);
		Chord chord = new Chord("C:maj");
		for (Note note : chord.getNotes()) {
			help.addNote(note, 24 * 2, 0, 80);
		}
		chord = new Chord("C:min");
		for (Note note : chord.getNotes()) {
			help.addNote(note, 24 * 2, 100, 80);
		}
		help.writeToFile("test.mid");
	}
}
