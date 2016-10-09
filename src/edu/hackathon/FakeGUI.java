package edu.hackathon;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.*;
import javax.swing.plaf.FileChooserUI;

public class FakeGUI extends JFrame implements ActionListener {

	private JButton loadButton, export, selectLoad, selectExport;
	
	private JTextField exportText, loadText;
	
	public FakeGUI() {
		super ("Fake Midi Maker");
		this.setBounds(100, 100, 700, 200);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		loadButton = new JButton("Load File");
		loadButton.addActionListener(this);
		loadButton.setActionCommand("Load");
		
		export = new JButton("Export File");
		export.addActionListener(this);
		export.setActionCommand("ExportFile");
		
		selectLoad = new JButton("Select File");
		selectLoad.addActionListener(this);
		selectLoad.setActionCommand("SelectFile");
		
		selectExport = new JButton("Select Export");
		selectExport.addActionListener(this);
		selectExport.setActionCommand("SelectExport");
		
		exportText = new JTextField("Export location");
		loadText = new JTextField("Load file");
		
		ImageIcon fakemidi = new ImageIcon("fakemidi.png");
		JLabel label = new JLabel("", fakemidi, JLabel.CENTER);
		JPanel background = new JPanel();
		background.add(label);
		getContentPane().add(background);

		JPanel loading = new JPanel();
		loading.setLayout(new BoxLayout(loading, BoxLayout.LINE_AXIS));
		loading.add(loadButton);
		loading.add(Box.createHorizontalStrut(10));
		loading.add(loadText);
		loading.add(Box.createHorizontalStrut(10));
		loading.add(selectLoad);
		
		getContentPane().add(loading);
		
		JPanel exporting = new JPanel();
		exporting.setLayout(new BoxLayout(exporting,BoxLayout.LINE_AXIS));
		exporting.add(export);
		exporting.add(Box.createHorizontalStrut(10));
		exporting.add(exportText);
		exporting.add(Box.createHorizontalStrut(10));
		exporting.add(selectExport);
		
		getContentPane().add(Box.createVerticalStrut(20));
		
		getContentPane().add(exporting);
		
		this.validate();
	}

	private Improviser joplin;
	private LeadSheet sheet;
	
	final JFileChooser fc = new JFileChooser();

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == selectExport) {
			int returnVal = fc.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            exportText.setText(file.getPath());
	        }
		}
		else if(event.getSource() == selectLoad) {
			int returnVal = fc.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            loadText.setText(file.getPath());
	        }
		}
		else if (event.getSource() == export) {
			if (joplin == null) {
				JOptionPane.showMessageDialog(this,
					    "You must first load a file before you can export it.",
					    "File not loaded",
					    JOptionPane.ERROR_MESSAGE); 
			}
			else {
				File file = new File(exportText.getText());
				joplin.makeMusic(file.getPath());
				JOptionPane.showMessageDialog(this,
					    "Succesfully wrote to " + file.getName() + "\n Enjoy!",
					    "Completed Action",
					    JOptionPane.INFORMATION_MESSAGE); 
			}
		}
		else if(event.getSource() == loadButton) {
			File file = new File(loadText.getText());
			System.out.println(file.getName());
			if (file.getName().substring(file.getName().indexOf('.')).equalsIgnoreCase(".mid")) {
				//load it as a midi file.
				readMidi(file);
				JOptionPane.showMessageDialog(this,
					    "Succesfully read file from " + file.getName() + "\n Press Export the improv!",
					    "Read File",
					    JOptionPane.INFORMATION_MESSAGE);
			}
			else if (file.getName().substring(file.getName().indexOf('.')).equalsIgnoreCase(".txt")) {
				//load it as a midi file.
				readText(file);
				JOptionPane.showMessageDialog(this,
					    "Succesfully read file from " + file.getName() + "\n Press Export the improv!",
					    "Read File",
					    JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				//inform user that it is not a valid option.
				JOptionPane.showMessageDialog(this,
			    "You must select a .mid or .txt file.",
			    "File Error",
			    JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	public static final int NOTE_ON = ShortMessage.NOTE_ON;
    public static final int NOTE_OFF = ShortMessage.NOTE_OFF;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	
	public void readMidi(File file) {
		
		Sequence sequence = null;
		try {
		 	 sequence = MidiSystem.getSequence(file);
		}
        catch(FileNotFoundException ex) {
			JOptionPane.showMessageDialog(this,
				    "Unable to find file.",
				    "File Not Found",
				    JOptionPane.ERROR_MESSAGE);    
			return;
        }
        catch(IOException ex) {
			JOptionPane.showMessageDialog(this,
                "Error reading file ",
                "Could Not Read File",
			    JOptionPane.ERROR_MESSAGE);        
            ex.printStackTrace();
            return;
        } catch (InvalidMidiDataException e) {
			JOptionPane.showMessageDialog(this,
	                "Invalid Midi Data ",
	                "Bad Midi",
				    JOptionPane.ERROR_MESSAGE);   
			e.printStackTrace();
			return;
		}
	        int trackNumber = 0;
	        
	        System.out.println();
	        Object[] possibilities = new Object[sequence.getTracks().length];
	        for(int i = 0; i < possibilities.length; i++)
	        	possibilities[i] = new Integer(i);
	        Integer opt = (Integer) JOptionPane.showInputDialog(
	                            this,
	                            "Which track number has the melody? (0 - " + (sequence.getTracks().length - 1) + ") ",
	                            "Track Number",
	                            JOptionPane.QUESTION_MESSAGE,
	                            null,
	                            possibilities,
	                            "0");

	        Track track = sequence.getTracks()[opt];
	        int melodyNote = -1;
	        long lastTick = -1;
	        int chordBeat = 0;
	        int beats = 0;
	        
	        String chords = "";
	        String queue = "";
	        String melody = "";
	        
	        long start_tick = 0;
	        long ticks = 0;
	        for (int i=0; i < track.size(); i++) { 
	            MidiEvent event = track.get(i);
	            //System.out.print("@" + event.getTick() + " ");
	            ticks = event.getTick();
	            if (ticks <= lastTick)
	            	continue;
	            MidiMessage message = event.getMessage();
	            if (message instanceof ShortMessage) {
	                ShortMessage sm = (ShortMessage) message;
	                //System.out.print("Channel: " + sm.getChannel() + " ");
	                if (sm.getCommand() == NOTE_OFF) {
	                    int key = sm.getData1();
	                    int octave = (key / 12)-1;
	                    int note = key % 12;
	                    String noteName = NOTE_NAMES[note];
	                    int velocity = sm.getData2();
	                    int offNote = key;
	                    if (melodyNote == offNote) {
	                    	String noteNameThing =  NOTE_NAMES[melodyNote % 12] + Interface.getNoteEquivOctave(melodyNote/12 - 1) + 
	                    			Interface.getNoteType((double)(ticks - start_tick) / sequence.getResolution()) + " ";
	                    	melody += noteNameThing;
	                    	melodyNote = -1;
	                    }
	                    //System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
	                } else if (sm.getCommand() == NOTE_ON) {
	                    int key = sm.getData1();
	                    int octave = (key / 12)-1;
	                    int note = key % 12;
	                    String noteName = NOTE_NAMES[note];
	                    int velocity = sm.getData2();
	                    if(key != melodyNote) {
		                    if(melodyNote != -1)
		                    {
		                    	String noteNameThing =  NOTE_NAMES[melodyNote % 12] + Interface.getNoteEquivOctave(melodyNote/12 - 1) + 
		                    			Interface.getNoteType((double)(ticks - start_tick) / sequence.getResolution()) + " ";
		                    	melody += noteNameThing;
		                    	/*queue += noteNameThing;
	                			beats += (int)((double)(ticks - start_tick) / sequence.getResolution());
	                			if (beats > chordBeat) {
	                				System.out.println("After \'" + queue + "\', what chord is played?");
	                				String chordName = scanner.next();
	                				System.out.println("How many beats does " + chordName + " play for?");
	                				int count = scanner.nextInt();
	                				beats += count * sequence.getResolution();
	                				chords += chordName + getNoteType(count) + " ";
	                				queue = "";
	                			}*/
		                    }
		                    
		                    start_tick = ticks;
		                    melodyNote = key;
	                    }
	                }
	            } else {
	                //System.out.println("Other message: " + message.getClass());
	            }
	            lastTick = ticks;
	        }

	        System.out.println("I have read the melody, which track has the chords? (0 - " + (sequence.getTracks().length - 1) + ") ");

	        possibilities = new Object[sequence.getTracks().length];
	        for(int i = 0; i < possibilities.length; i++)
	        	possibilities[i] = new Integer(i);
	        opt = (Integer) JOptionPane.showInputDialog(
                    this,
                    "Which track number has the chords? (0 - " + (sequence.getTracks().length - 1) + ") ",
                    "Track Number",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    possibilities,
                    "0");

    		track = sequence.getTracks()[opt];
	        int chordNote = -1;
	        
	        chords = "";
	        lastTick = -1;
	        start_tick = 0;
	        ticks = 0;
	        for (int i=0; i < track.size(); i++) { 
	            MidiEvent event = track.get(i);
	            //System.out.print("@" + event.getTick() + " ");
	            ticks = event.getTick();
	            if (ticks <= lastTick)
	            	continue;
	            
	            MidiMessage message = event.getMessage();
	            if (message instanceof ShortMessage) {
	                ShortMessage sm = (ShortMessage) message;
	                //System.out.print("Channel: " + sm.getChannel() + " ");
	                if (sm.getCommand() == NOTE_OFF) {
	                    int key = sm.getData1();
	                    int octave = (key / 12)-1;
	                    int note = key % 12;
	                    String noteName = NOTE_NAMES[note];
	                    int velocity = sm.getData2();
	                    int offNote = key;
	                    if (chordNote == offNote) {
	                    	String noteNameThing =  NOTE_NAMES[chordNote % 12] + Interface.getNoteType((double)(ticks - start_tick) / sequence.getResolution()) + " ";
	                    	chords += noteNameThing;
	                    	chordNote = -1;
	                    }
	                    //System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
	                } else if (sm.getCommand() == NOTE_ON) {
	                    int key = sm.getData1();
	                    int octave = (key / 12)-1;
	                    int note = key % 12;
	                    String noteName = NOTE_NAMES[note];
	                    int velocity = sm.getData2();
	                    if(key != chordNote) {
		                    if(chordNote != -1)
		                    {
		                    	String noteNameThing =  NOTE_NAMES[chordNote % 12] + Interface.getNoteType((double)(ticks - start_tick) / sequence.getResolution()) + " ";
		                    	chords += noteNameThing;
		                    	chordNote = -1;
		                    }
		                    
		                    start_tick = ticks;
		                    chordNote = key;
	                    }
	                }
	            }
	            lastTick = ticks;
	        }
	        
	        String title = (String)JOptionPane.showInputDialog(
                    this,
                    "Give me the title! ",
                    "Title Please",
                    JOptionPane.QUESTION_MESSAGE);
	        
	        String composer = (String)JOptionPane.showInputDialog(
                    this,
                    "Tell me the composer! ",
                    "Composer",
                    JOptionPane.QUESTION_MESSAGE);
	        
	        String time = (String)JOptionPane.showInputDialog(
                    this,
                    "Tell me the time signature (top/bottom)! ",
                    "Time Signature",
                    JOptionPane.QUESTION_MESSAGE);
	        
	        String key = (String)JOptionPane.showInputDialog(
                    this,
                    "Give me the key signature! ",
                    "key",
                    JOptionPane.QUESTION_MESSAGE);
	        
	        sheet = new LeadSheet(title + ", " + composer + ", " + time + ", " + key + ", " + melody + ", " + chords);
			// + "Di Eq C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. Di D#i Ei C+q Ei C+q Ei C+h. Ai Gi F#i Ai C+i E+q D+i C+i Ai D+h. Di D#i Ei C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. D+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q Bi D+q C+h. Di D#q C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. Di D#i Ei C+q Ei C+q Ei C+h. Ai Gi F#i Ai C+i E+q D+i C+i Ai D+h. Di D#i Ei C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. D+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q Bi D+q C+h Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i Gi E+i F+i G+i A+i G+i E+i F+i G+q A+i G+q E+i F+i F#+i G+q A+i G+q. A+i A#+i B+h A+i F#+i D+i G+h Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i C+h Gi F#i Gi C+q Ai C+q Ai C+i Ai Gi C+i E+i G+q E+i C+i Gi Aq C+q E+i D+q C+h. Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i Gi E+i F+i G+i A+i G+i E+i F+i G+q A+i G+q E+i F+i F#+i G+q A+i G+q. A+i A#+i B+h A+i F#+i D+i G+h Ei Fi F#i Gq Ai Gq Ei Fi F#i Gq Ai Gq E+i C+i Gi Ai Bi C+i D+i E+i D+i C+i D+i C+h Gi F#i Gi C+q Ai C+q Ai C+i Ai Gi C+i E+i G+q E+i C+i Gi Aq C+q E+i D+q C+h. Di D#q C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. Di D#i Ei C+q Ei C+q Ei C+h. Ai Gi F#i Ai C+i E+q D+i C+i Ai D+h. Di D#i Ei C+q Ei C+q Ei C+h. D+i D#+i E+i C+i D+i E+q Bi D+q C+h. D+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q C+i D+i C+i E+i C+i D+i E+q Bi D+q C+h, "
			//+ " Ci C#i Ei C+q Ei C+q Ei t(C+i;C+q) Rq Ri C+i D+i D#+i E+i C+i D+i t(E+i;E+i) Bi D+q C+q,"
			//+ " Rq Ch C7h Fw Ch Gmaj7h Cw");
	        joplin = new Improviser(sheet);
	}
	
	public void readText(File file) {
		String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(file);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String title = bufferedReader.readLine();
            String composer = bufferedReader.readLine();
            String time = bufferedReader.readLine();
            String key = bufferedReader.readLine();
            String melody = bufferedReader.readLine();
            String chords = bufferedReader.readLine();
            
            sheet = new LeadSheet(title + ", " + composer + ", " + time + ", " + key + ", " + melody + ", " + chords);
    		joplin = new Improviser(sheet);
            
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
			JOptionPane.showMessageDialog(this,
				    "Unable to find file.",
				    "File Not Found",
				    JOptionPane.ERROR_MESSAGE);             
        }
        catch(IOException ex) {
			JOptionPane.showMessageDialog(this,
                "Error reading file ",
                "Could Not Read File",
			    JOptionPane.ERROR_MESSAGE);        
            // Or we could just do this: 
            // ex.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		FakeGUI fakeGUI = new FakeGUI();
	}
}
