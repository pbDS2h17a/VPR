package sqlCreation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader {
	private static String[] continentData = null;
	private static String[] countryData = null;
	private static String[] missionData = null;
	private static String[] cardData = null;
	
	static void readFile(String path) throws IOException {
		//BufferedReader br = new BufferedReader(new FileInputStream(path));
		BufferedReader br = new BufferedReader(new InputStreamReader(
			    new FileInputStream(path), "UTF-8"));
		try {  
		    String line;
		    String currentBlock = "";
		    String currentData = ""; 
		    // Zeile für Zeile lesen
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
						case "KARTE":
							cardData = currentData.split("\n");
							break;
						case "MISSION":
							missionData = currentData.split("\n");
							break;
						}
						
						// Blöcke und Daten zurücksetzen
						currentBlock = "";
						currentData = "";
					}  	   
		     	}
		    }
		} finally {
		    br.close();
		}

	}
	// getter für Kontinent Daten
	// Werden in der SqlQuery fill methode weiter aufgeteilt
	static String[] getContinent() {
		return continentData;	
	}
	
	// getter für Länder Daten
	// Werden in der SqlQuery fill methode weiter aufgeteilt
	static String[] getCountry() {
		return countryData;
	}
	
	static String[] getMission() {
		return missionData;
	}
	
	static String[] getCard() {
		return cardData;
	}
	
}
