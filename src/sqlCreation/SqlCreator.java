package sqlCreation;

/**
 * @author pbs2h17awb
 * Setzt die Datenbank zurück
 * Löscht alle Laufdaten
 * Stellt Stammdaten aus der Resourcedatei wieder her
 */
public class SqlCreator {
	
	public static void main(String[] args) {
		// Datei einlesen
		SqlReader.readFile();
		// Daten für einzelne Bereiche über getter erhalten
		String[] continentData = SqlReader.getContinent();
		String[] countryData = SqlReader.getCountry();
		String[] missionData = SqlReader.getMission();
		String[] cardData = SqlReader.getCard();
		String[] colorData = SqlReader.getColor();

		// Constraint werden deaktiviert
		// damit alle Tabellen erstellt werden können
		// ohne die Reihenfolge einzuhalten
		SqlQuery.disableForeignKeyConstraints();

		// Alle Drop statements werden aufgeführt
		// damit die Datenbank vollständig zurückgesetzt wird
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
			
		// Tabllen werden neu erstellt
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
	
		// Tabellen werden mit Inhalt der Stammdaten Datei gefüllt
		SqlQuery.fillContinent(continentData);
		SqlQuery.fillCountry(countryData);
		SqlQuery.fillNeighbor(countryData);
		SqlQuery.fillCard(cardData);
		SqlQuery.fillMissions(missionData);
		SqlQuery.fillColor(colorData);
		
		// Constraint werden wieder aktiviert
		SqlQuery.enableForeignKeyConstraints();

		System.out.println("Tabellen erfolgreich zurückgesetzt");

	}

}
