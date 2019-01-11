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

		// Constraint werden deaktiviert
		// damit alle Tabellen erstellt werden können
		SqlQuery.disableForeignKeyConstraints();

		// Alle Drop statements werden aufgeführt
		// um Fehler vorzubeugen
		SqlQuery.dropContinent();
		SqlQuery.dropCountry();
		SqlQuery.dropNeighbor();
		SqlQuery.dropPlayer();
		SqlQuery.dropLobby();
		SqlQuery.dropPlayerCountry();
		SqlQuery.dropCard();
		SqlQuery.dropMission();
		SqlQuery.dropColor();
		SqlQuery.dropCardsPlayer();
		SqlQuery.dropMissionPlayer();

		
		// Tabllen werden erstellt
		SqlQuery.createContinent();
		SqlQuery.createCountry();
		SqlQuery.createNeighbor();
		SqlQuery.createColor();
		SqlQuery.createPlayer();
		SqlQuery.createLobby();
		SqlQuery.createPlayerCountry();
		SqlQuery.createCard();
		SqlQuery.createMission();
		SqlQuery.createMissionPlayer();
		SqlQuery.createCardsPlayer();

		// Constraint werden wieder aktiviert
		SqlQuery.enableForeignKeyConstraints();

		
		// Tabellen werden gefüllt
		SqlQuery.fillContinent(continentData);
		SqlQuery.fillCountry(countryData);
		SqlQuery.fillNeighbor(countryData);
		SqlQuery.fillCard(cardData);
		SqlQuery.fillMissions(missionData);
		SqlQuery.fillColor(colorData);

	}

}
