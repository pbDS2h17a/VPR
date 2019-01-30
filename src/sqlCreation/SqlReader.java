package sqlCreation;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author pbs2h17awb
 * Ließt die Daten aus der Stammdaten.csv datei aus und trennt sie an den marktieren Bereichen in
 * Datenbklöcke auf.
 * Die Datenblöcke werden an die SqlQeury fillStatements übergeben
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
		    // Zeile für Zeile lesen
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
						// 7 = #START länge + 1 Leerzeichen
						currentBlock = line.substring(7);
						// Datenblock wird auf leer gesetzt
						currentData = "";
					}
					
					// Endbefehl
					if(line.startsWith("#END")) {
						// Zuweisung der Aktuellen Daten in den richtigen Block
						// Name hier muss übereinstimmen mit Name in Stammdaten datei!
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
						
						// Blöcke und Daten zurücksetzen
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
			// Versuche den Reader zu schließen
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * getter für Kontinent Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 * @return continentData mit Trennzeichen geteilt
	 * @see SqlQuery#fillContinent(String[])
	 */
	static String[] getContinent() {
		return continentData;	
	}
	
	/**
	 * getter für Länder Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 * @return countryData mit Trennzeichen geteilt
	 * @see SqlQuery#fillCountry(String[])
	 */
	static String[] getCountry() {
		return countryData;
	}
	/**
	 * getter für Mission Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 * @return missionData mit Trennzeichen geteilt
	 * @see SqlQuery#fillMissions(String[])
	 */
	static String[] getMission() {
		return missionData;
	}
	/**
	 * getter für Karten Daten
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 * @return cardData mit Trennzeichen geteilt
	 * @see SqlQuery#fillCard(String[])
	 */
	static String[] getCard() {
		return cardData;
	}

	/**
	 * @author Lea Mönikes
	 * getter für Farbe Datem
	 * Werden in der SqlQuery fill methode weiter aufgeteilt
	 * @return colorData mit Trennzeichen geteilt
	 * @see SqlQuery#fillColor(String[])
	 */
	static String[] getColor(){
		return colorData;
	}
	
}
