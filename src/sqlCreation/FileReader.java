package sqlCreation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class FileReader {
	private static String[] continentData = null;
	private static String[] countryData = null;
	
	static void readFile(String path) throws IOException {
		BufferedReader br = new BufferedReader(new java.io.FileReader(new File(path)));
		try {  
		    String line;
		    String currentBlock = "";
		    String currentData = ""; 
		    // Zeile f�r Zeile lesen
		    while ((line = br.readLine()) != null) {
	    		// Kommentare (//) werden ignoriert
		    	if(!line.startsWith("//")) {
					// START und END befehle werden nicht bearbeitet
					if(!line.startsWith("#")) {
						//System.out.println(line);
						currentData += line + "\n";
					}
					   
					// Start befehl
					if(line.startsWith("#START")) {
						currentBlock = line.substring(7);
						currentData = "";
					}
					
					// End befehl
					if(line.startsWith("#END")) {
						// Zuweisung der Aktuellen Daten in den richtigen Block
						switch(currentBlock) {
						case "KONTINENT":				
							continentData = currentData.split("\n");						
							break;
						case "LAND":
							countryData = currentData.split("\n");
							break;
						}
						
						// Bl�cke und Daten zur�cksetzen
						currentBlock = "";
						currentData = "";
					}  	   
		     	}
		    }
		} finally {
		    br.close();
		}

	}
	// getter f�r Kontinent Daten
	// Werden in der SqlQuery fill methode weiter aufgeteilt
	static String[] getContinent() {
		return continentData;	
	}
	
	// getter f�r L�nder Daten
	// Werden in der SqlQuery fill methode weiter aufgeteilt
	static String[] getCountry() {
		return countryData;
	}
	
}
