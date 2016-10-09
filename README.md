# FakeMidi
Using midi file format to improvise music from lead sheets.

Nicholas Maltbie, Vuong Khuat, Wyatt French

## Objective 

This is part of Revolution UC Hackathon, Oct. 8-9, 2016. The objective is to 
create a program in Java that can read a lead sheet then export a midi file 
that adds improvisation to the song based on the chord progression and music 
theory. 

The basics of how to play a lead sheet can be found [here](https://greghowlett.com/blog/free-lessons/061111012.aspx) 
from Greg Howlett.

## How

Reading lead sheets and improvising is hard, now we will split it up into 
smaller steps.

### First Objective
We will develop some format to read lead sheets.

Lead sheets contain
* Time Signature
* Key
* Chord progression
* Melody
* Possibly Lyrics

### Second Objective
Once the lead sheets can be read into the computer, then the program will export 
the melody along with chords to a midi file to create a proof of concept. 

### Third Objective
Add improvisation to the midi file (more work on this later, first two should be
completed first.

## Output

These exported music files can then be played using a [Music Animaiton Machine 
MIDI Player](http://www.musanim.com/player/) from Stephen Musanim to generate a 
colored, animated score from the midi file. More information about the graphical
 score is created can be found [here](http://www.musanim.com/mam/pfifth.htm).  
 
# Finished Product
 
 Now the program can read in either specially written text files that represent a music
 sheet or from midi files with different tracks for chords and the melody. Then,
 the program will take the results from this data and use the melody and 
 chords to compose a song. 
 
 In order to use the application, you can use the GUI launched when the 
 FakeMidi.jar is executed (found in builds). The GUI will present the user with 
 two text boxes for input and output. The path of the input can be 
 directly typed into the load box or it can be selected using the dialog created
 when the select file button is pressed. Once a file is selected, it must be 
 loaded by pressing the load file button. After the file is loaded, it can be exported
 to the file specified in export file text box. A export location can be 
 specified using the select export button.
 
## Final Output
 
 The project can read in a midi file and output a midi file of an improvised 
 reading of the file's chords and melody. The chords and melody must be on 
 different tracks within the midi file. Examples of these txt files are in the project
 as AutumnLeaves.txt, Entertainer.txt and JingleBellRock.txt
 
 If a midi file is not available, a text file to store the iformation can be created.
 The text file must be as follows (without the line1, line2, line3...):
 
 line 1: Title of Song
 
 line 2: Composer
  
 line 3: Time Signature
 
 line 4: key
 
 line 5: melody
 
 line 6: chord progression.

 the melody is a single line of notes such as: 
 	Ci C#i Ei C+q Ei C+q Ei t(C+i;C+q) Rq Ri C+i D+i D#+i E+i C+i D+i t(E+i;E+i) Bi D+q C+q Rq Rq Di D#i Ei C+q Ei t(C+i;Cq) Rq Rq Ai Gi 
 	
 a t(note;note) means note and note are tied
 notes are saved as follows: note then length note or rest, for example:
 	example: C+1#w is a C# whole note one octave up from middle C.
 		C (middle C)
 		C+ (C one octave up)
 		C+3 (C three octaves up)
 		C- (C one octave down)
 		Bf (B flat)
 		R (rest)
 		E# (E sharp)
 	length is the type of note (A . after the letter means a dotted note):
 		whole is w	(4 beats)
 		half h		(2 beats)
 		quarter q	(1 beat)
 		eighth i	(1/2 beat)
 		sixteenth s	(1/4 beat)
 		thirty-second t
 		sixty-fourth x
 		one-twenty-eighth o

 the chords is a single line of the song's chord progression such as:
 	Rq Ch C7h Fw Ch Gmaj7h Cw Rh C7h Fw 
 	
 	chord_name is the name of the chord, for example:
  		C (C major)
  		Bb (B flat major)
   		Gm (G minor)
   		G#m	(G# minor)
   		Bbmaj7 (B flat mojor 7)
	  	F#min (F sharp minor)
 		R (A break, no chord)
   	length is the same as before
 	length is the length in beats until the next chord occurs	

## Challenges While Working on the Project

Attempting to understand Midi in java. None of the group had experience using Midi and it 
was very difficult to find a Library to use with the project and we ended up writing all 
of the code to implement music theory and midi file io ourselves using code on the 
internet and the default java API.

Lack of sleep. The project is 24 hours and None of us on the team slept while we 
were working so we all could use a nap now that it is almost over.

## Future Ideas

More music improv standards and music theory ideas added to options for Improviser.java

Image Recognition using OCR to read from sheet music pictures.

Export to Phone for image recognition (written in Java which exports to Android, too much time for this hackathon)

Export to Phone to listen and accompany musicians automatically (we know how to recognize pitches for notes).

More instruments than just piano.
