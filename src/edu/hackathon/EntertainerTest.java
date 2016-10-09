package edu.hackathon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class EntertainerTest {
	public static void main(String[] args) {
		System.out.println("Give me a file");
		Scanner scan = new Scanner(System.in);
		
		// The name of the file to open.
        String fileName = scan.nextLine();

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String title = bufferedReader.readLine();
            String composer = bufferedReader.readLine();
            String time = bufferedReader.readLine();
            String key = bufferedReader.readLine();
            String melody = bufferedReader.readLine();
            String chords = bufferedReader.readLine();
            
            LeadSheet sheet = new LeadSheet(title + ", " + composer + ", " + time + ", " + key + ", " + melody + ", " + chords);
    		Improviser joplin = new Improviser(sheet);
    		System.out.println("Give me an output file name (include .mid)!" );
    		joplin.makeMusic(scan.nextLine());
            
            
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
	}
}
