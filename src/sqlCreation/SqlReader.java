package sqlCreation;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author pbs2h17awb
 * Lie�t die Daten aus der Stammdaten.csv datei aus und trennt sie an den marktieren Bereichen in
 * Datenbkl�cke auf.
 * Die Datenbl�cke werden an die SqlQeury fillStatements �bergeben
 */
class SqlReader {
	private static String[] continentData = null;
	private static String[] countryData = null;
	private static String[] missionData = null;
	private static String[] cardData = null;
	private static String[] colorData = null;

	static void readFile() {
		String path = "src\\resources\\stammdaten4.csv";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {  
		    String line;
		    String currentBlock = "";
		    String currentData = ""; 
		    // Zeile f�r Zeile lesen
		    while ((line = br.readLine()) != null) {
	    		// Kommentare (//) werden ignoriert
		    	if(!line.startsWith("//")) {
					// #START und #END befehle werden nicht bearbeitet
					if(!line.startsWith("#")) {
						currentData += line + "\n";
					}
					   
					// Startbefehl
					if(line.startsWith("#START")) {
						// Aktueller Name des Blockes steht hinter #START
						// 7 = #START l�nge + 1 Leerzeichen
						currentBlock = line.substring(7);
						// Datenblock wird auf leer gesetzt
						currentData = "";
					}
					
					// Endbefehl
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
						case "FARBE":
							colorData = currentData.split("\n");
							break;
						default:
							System.out.println("Fehler beim einlesen der Stammdaten! Ein Blockname konnte nicht zugewiesen werden");
							return;
						}
						
						// Bl�cke und Daten zur�cksetzen
						currentBlock = "";
						currentData = "";
					}  	   
		     	}
		    }
		} catch (IOException e) {
			// Fehler abfangen
			System.out.println("Fehler beim Lesen der Datei:"+path);
			e.printStackTrace();
		} finally {
			// Versuche den Reader zu schlie�en
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * getter f�r Kontinent Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 * @return continentData mit Trennzeichen geteilt
	 * @see SqlQuery#fillContinent(String[])
	 */
	static String[] getContinent() {
		return continentData;	
	}
	
	/**
	 * getter f�r L�nder Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 * @return countryData mit Trennzeichen geteilt
	 * @see SqlQuery#fillCountry(String[])
	 */
	static String[] getCountry() {
		return countryData;
	}
	/**
	 * getter f�r Mission Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 * @return missionData mit Trennzeichen geteilt
	 * @see SqlQuery#fillMissions(String[])
	 */
	static String[] getMission() {
		return missionData;
	}
	/**
	 * getter f�r Karten Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 * @return cardData mit Trennzeichen geteilt
	 * @see SqlQuery#fillCard(String[])
	 */
	static String[] getCard() {
		return cardData;
	}

	/**
	 * @author Lea M�nikes
	 * getter f�r Farbe Datem
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 * @return colorData mit Trennzeichen geteilt
	 * @see SqlQuery#fillColor(String[])
	 */
	static String[] getColor(){
		return colorData;
	}
	
}
