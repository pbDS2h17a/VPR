package sqlCreation;

import java.io.IOException;
import java.sql.SQLException;

public class MySQLConnection {
	
	public static void main(String[] args) throws IOException {
		// Datei einlesen
		FileReader.readFile();
		// Daten f�r einzelne Bereiche �ber getter erhalten
		String[] continentData = FileReader.getContinent();
		String[] countryData = FileReader.getCountry();
		String[] missionData = FileReader.getMission();
		String[] cardData = FileReader.getCard();
		String[] colorData = FileReader.getColor();

		// Constraint werden deaktiviert
		// damit alle Tabellen erstellt werden k�nnen
		// ohne die Reihenfolge einzuhalten
		SqlQuery.disableForeignKeyConstraints();

		// Alle Drop statements werden aufgef�hrt
		// damit die Datenbank vollst�ndig zur�ckgesetzt wird
		SqlQuery.dropTable("card");
		SqlQuery.dropTable("card_player");
		SqlQuery.dropTable("chat");
		SqlQuery.dropTable("color");
		SqlQuery.dropTable("continent");
		SqlQuery.dropTable("country");
		SqlQuery.dropTable("country_player");
		SqlQuery.dropTable("lobby");
		SqlQuery.dropTable("mission");
		SqlQuery.dropTable("mission_player");
		SqlQuery.dropTable("neighbor");
		SqlQuery.dropTable("player");
		SqlQuery.dropTable("player_country");
		SqlQuery.dropTable("color_player");
			
		// Tabllen werden erstellt
		SqlQuery.createTableContinent();
		SqlQuery.createTableCountry();
		SqlQuery.createTableNeighbor();
		SqlQuery.createTableColor();
		SqlQuery.createTablePlayer();
		SqlQuery.createTableLobby();
		SqlQuery.createTableCountryPlayer();
		SqlQuery.createTableCard();
		SqlQuery.createTableMission();
		SqlQuery.createTableMissionPlayer();
		SqlQuery.createTableCardsPlayer();
		SqlQuery.createTableColorPlayer();
		SqlQuery.createTableChat();
	
		// Tabellen werden gef�llt
		SqlQuery.fillContinent(continentData);
		SqlQuery.fillCountry(countryData);
		SqlQuery.fillNeighbor(countryData);
		SqlQuery.fillCard(cardData);
		SqlQuery.fillMissions(missionData);
		SqlQuery.fillColor(colorData);
		
		// Constraint werden wieder aktiviert
		SqlQuery.enableForeignKeyConstraints();

		System.out.println("Tabellen erfolgreich zur�ckgesetzt");

	}

}
