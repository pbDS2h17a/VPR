package sqlCreation;

import sqlConnection.SqlHelper;

import java.io.IOException;
import java.sql.SQLException;

public class MySQLConnection {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// Datei einlesen
		FileReader.readFile("src\\resources\\stammdaten4.csv");
		// Daten �ber getter erhalten
		String[] continentData = FileReader.getContinent();
		String[] countryData = FileReader.getCountry();
		String[] missionData = FileReader.getMission();
		String[] cardData = FileReader.getCard();
		
		// Statement hilfsfunktion
		
		SqlQuery.stmt = SqlHelper.getStatement();
		
		// Alle Drop statements werden aufgef�hrt
		// um Fehler vorzubeugen
		SqlQuery.dropCountry();
		SqlQuery.dropContinent();
		SqlQuery.dropNeighbor();
		SqlQuery.dropPlayer();
		SqlQuery.dropPlayerCountry();
		SqlQuery.dropCard();
		SqlQuery.dropMission();
		
		// Tabllen werden erstellt
		SqlQuery.createContinent();
		SqlQuery.createCountry();
		SqlQuery.createNeighbor();
		SqlQuery.createPlayer();
		SqlQuery.createPlayerCountry();
		SqlQuery.createCard();
		SqlQuery.createMission();
		SqlQuery.createLobby();
		
		// Tabellen werden gef�llt
		SqlQuery.fillContinent(continentData);
		SqlQuery.fillCountry(countryData);
		SqlQuery.fillNeighbor(countryData);
		SqlQuery.fillCard(cardData);
		SqlQuery.fillMissions(missionData);

	}

}
