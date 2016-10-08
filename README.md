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

