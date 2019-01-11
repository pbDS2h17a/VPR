package sqlCreation;

import sqlConnection.SqlHelper;

import java.io.IOException;
import java.sql.SQLException;

public class MySQLConnection {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// Datei einlesen
		FileReader.readFile("src\\resources\\stammdaten4.csv");
		// Daten über getter erhalten
		String[] continentData = FileReader.getContinent();
		String[] countryData = FileReader.getCountry();
		String[] missionData = FileReader.getMission();
		String[] cardData = FileReader.getCard();
		String[] colorData = FileReader.getColor();
		
		// Statement hilfsfunktion
		SqlQuery.stmt = SqlHelper.createStatement();
		
		// Alle Drop statements werden aufgeführt
		// um Fehler vorzubeugen
		SqlQuery.dropCountry();
		SqlQuery.dropContinent();
		SqlQuery.dropNeighbor();
		SqlQuery.dropPlayer();
		SqlQuery.dropPlayerCountry();
		SqlQuery.dropCard();
		SqlQuery.dropMission();
		SqlQuery.dropColor();
		
		// Tabllen werden erstellt
		SqlQuery.createContinent();
		SqlQuery.createCountry();
		SqlQuery.createNeighbor();
		SqlQuery.createPlayer();
		SqlQuery.createPlayerCountry();
		SqlQuery.createCard();
		SqlQuery.createMission();
		SqlQuery.createColor();
		SqlQuery.createLobby();

		
		// Tabellen werden gefüllt
		SqlQuery.fillContinent(continentData);
		SqlQuery.fillCountry(countryData);
		SqlQuery.fillNeighbor(countryData);
		SqlQuery.fillCard(cardData);
		SqlQuery.fillMissions(missionData);
		SqlQuery.fillColor(colorData);

	}

}
