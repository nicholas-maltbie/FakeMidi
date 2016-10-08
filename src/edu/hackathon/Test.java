package edu.hackathon;

import java.io.File;

import org.jfugue.pattern.*;
import org.jfugue.player.Player;

public class Test {
    
    public static void main(String[] args) {
    	Player player = new Player();
	    Pattern p1 = new Pattern("V0 I[Piano] Fw | Ebi. Ebs Ebi. Ebs Ebq Gq | Fw | Gi. Gs Gi. Gs G Bb | Aw | ");
	    Pattern p2 = new Pattern("V1 I[Piano] Fmajw | Ebmajw | Fmajw | Ebmajw | Fmajw |");
	    player.play(p1, p2);
	    
	    /*Player player = new Player();
	    File file = new File("out.mid");
	    String s = "A B C D E F G";
	    player.saveMidi(s, file);
	    player.play(s);
	    
	    System.exit(0); // If using Java 1.4 or lower
    	*/
    }

}