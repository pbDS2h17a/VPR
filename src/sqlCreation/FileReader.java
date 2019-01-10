package sqlCreation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * @author pbs2h17awb
 */
public class FileReader {
	private static String[] continentData = null;
	private static String[] countryData = null;
	private static String[] missionData = null;
	private static String[] cardData = null;
	/**
	 * 
	 * @param path Pfad your Stammdaten datei
	 * @throws IOException 
	 */
	static void readFile(String path) throws IOException {
		//BufferedReader br = new BufferedReader(new FileInputStream(path));
		BufferedReader br = new BufferedReader(new InputStreamReader(
			    new FileInputStream(path), "UTF-8"));
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
						// Name hier muss �bereinstimmen mit Name in Stammdaten datei! 
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
	
	/**
	 * @return getter f�r Kontinent Daten
	 * 		   Werden in der SqlQuery fill methode weiter aufgeteilt
	 */
	static String[] getContinent() {
		return continentData;	
	}
	
	/**
	 *
	 * @return getter f�r L�nder Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 */
	static String[] getCountry() {
		return countryData;
	}
	/**
	 * 
	 * @return getter f�r Mission Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 */
	static String[] getMission() {
		return missionData;
	}
	/**
	 *
	 * @return getter f�r Karten Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 */
	static String[] getCard() {
		return cardData;
	}
	
}
