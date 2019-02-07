package sqlCreation;

import sqlConnection.SqlHelper;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author pbs2h17awb
 * SQL Queries zum Erstellen der Stammdatenbank
 */
class SqlQuery {
	private static String splitter = ";";
	private static Statement stmt = SqlHelper.getStatement();
	
	//#################################################################################################################
	// FILL STATEMENTS
	//#################################################################################################################
	static void fillContinent(String[] data) {
		for (String string : data) {
			String[] dataArray = string.split(splitter);
			String id = dataArray[0].trim();
			String name = dataArray[1].trim();
			String bonus = dataArray[2].trim();
			
			String sql =
					"INSERT INTO continent (continent_id, name, bonus)" +
					"VALUES ('"+id+"', '"+name+"', '"+bonus+"');";
			// SQL Ausführen
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("fillContinent");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Schreibt die Länder in die Datenbank
	 * @param data StringArray mit ID,Name,KontinentID und SVG
	 */
	static void fillCountry(String[] data) {
		for (String string : data) {
			String[] dataArray = string.split(splitter);
			String id = dataArray[0].trim();
			String name = dataArray[1].trim();
			String continent = dataArray[2].trim();
			String svg = dataArray[dataArray.length-1].trim();
			
			String sqlCountry =
					"INSERT INTO country (country_id, name, continent_id, svg)" +
					"VALUES ('"+id+"', '"+name+"', '"+continent+"','"+svg+"');";		
			
			// SQL Ausführen
			try {
				stmt.executeUpdate(sqlCountry);
			} catch (SQLException e) {
				System.out.println("fillCountry");
				e.printStackTrace();
			}		
		}	
	}

	/**
	 * Schreibt die Nachbarländer in die Datenbank
	 * @param data StringArray mit LandID und variablen vielen NachbarIDs
	 */
	static void fillNeighbor(String[] data) {
		for (String string : data) {
			String[] dataArray = string.split(splitter);
			String id = dataArray[0].trim();
			// Nachbarn fangen bei Element 3 an bis zum vorletzten Element
			for (int i = 3; i < (dataArray.length - 1); i++) {
				String sqlNeighbor =
						"INSERT INTO neighbor (country_id, neighbor_id)" +
					    "VALUES('" + id + "', '" + dataArray[i].trim() + "');";

				// SQL Ausführen
				try {
					stmt.executeUpdate(sqlNeighbor);
				} catch (SQLException e) {
					System.out.println("fillNeighbor");
					e.printStackTrace();
				}
			}
		}
		
	}


	/**
	 * Schreibt die Missionen in die Datenbank
	 * @param data StringArray mit MissionID und Beschreibung
	 */
	static void fillMissions(String[] data) {	
		for (String string : data) {
			String[] dataArray = string.split(splitter);
			String id = dataArray[0];
			String description = dataArray[1];
			String sql =
					"INSERT INTO mission(mission_id, description)"
					+"VALUES('"+id+"','"+description+"');";
			
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("fillMission");
				e.printStackTrace();
			}	
		}		
	}

	/**
	 *
	 * @param data StringArray mit KartenID, LandID, und Wert
	 *             der Wert ist eine Zahl zwischen 1 und 3
	 *             und entspricht der Einheit der Karte
	 *             1 = Infanterie (Fußsoldat)
	 *             2 = Kavallerie (Pferd)
	 *             3 = Artillerie (Kanone)
	 */
	static void fillCard(String[] data) {	
		for (String string : data) {
			String[] dataArray = string.split(splitter);
			// cardId und countryId sind identisch
			String cardId = dataArray[0];
			String countryId = dataArray[0];
			String value = dataArray[1];
			String sql =
					"INSERT INTO card(card_id, value, country_id)"
					+"VALUES('"+cardId+"','"+value+"','"+countryId+"');";
			
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("fillMission");
				e.printStackTrace();
			}
		}		
	}

	/**
	 * @author Lea Mönikes
	 * @param data stringArray mit FarbID, Namen und Wert
	 *             Der Wert ist ein 6-Stelliger HEX code
	 */
	static void fillColor(String[] data){
		for (String string : data) {
			
			String[] dataArray = string.split(splitter);
			String colorId = dataArray[0].trim();
			String colorName = dataArray[1].trim();
			String value = dataArray[2].trim();
			
			String sql =
					"INSERT INTO color(color_id, name, value)"
					+"VALUES('"+colorId+"','"+colorName+"','"+value+"');";
			
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("fillColor");
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * Hilfsmethode um die Constraints zu deaktivieren
	 */
	static void disableForeignKeyConstraints() {
		try {
			stmt.executeUpdate("SET foreign_key_checks = 0");
		} catch (SQLException e) {
			System.out.println("disableForeignKeyRestraints");
			e.printStackTrace();
		}
	}
	/**
	 * Hilfsmethode um die Constraints zu aktivieren
	 */
	static void enableForeignKeyConstraints() {
		try {
			stmt.executeUpdate("SET foreign_key_checks = 1");

		} catch (SQLException e) {
			System.out.println("enableForeignKeyRestraints");
			e.printStackTrace();
		}
	}

	//#################################################################################################################
	// DROP STATEMENTS
	//#################################################################################################################

	/**
	 * Löscht den Table aus der Tabelle wenn er vorhanden ist
	 * @param tableName Name der zu löschenden Tabelle
	 */
	static void dropTable(String tableName) {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS "+tableName);
		} catch (SQLException e) {
			System.out.println("Fehler beim Löschen der Tabelle: "+tableName);
			e.printStackTrace();
		}	
	}
	
	//#################################################################################################################
	// CREATE STATEMENTS 
	//#################################################################################################################
	/**
	 * Methode erstellt die Tabellen für Kontinente
	 */
	static void createTableContinent() {
		String sqlContinent =
				" CREATE TABLE IF NOT EXISTS continent (" +
				" continent_id INT, " +
                " name VARCHAR(255), " +
				" bonus INT," +
				" PRIMARY KEY(continent_id)" +
				");";	
		try {
			stmt.executeUpdate(sqlContinent);
		} catch (Exception e) {
			System.out.println("createTableContinent");
			e.printStackTrace();
		}	
	}
	/**
	 * Methode erstellt die Tabellen für Länder
	 */
	static void createTableCountry() {
		String sqlCountry =
				" CREATE TABLE IF NOT EXISTS country (" +
				" country_id INT, " +
	            " name VARCHAR(255) NOT NULL, " +
				" continent_id INT, "+
				" svg VARCHAR(15000), " +
				" FOREIGN KEY(continent_id) REFERENCES continent(continent_id), " +
				" PRIMARY KEY(country_id)" +
	            ");";	
		try {
			stmt.executeUpdate(sqlCountry);
		} catch (SQLException e) {
			System.out.println("createTableCountry");
			e.printStackTrace();
		}	
	}
	/**
	 * Methode erstellt die Tabellen für Spieler
	 */
	static void createTablePlayer() {
		String sqlPlayer =
				" CREATE TABLE IF NOT EXISTS player (" +
				" player_id INT NOT NULL AUTO_INCREMENT, " +
	            " name VARCHAR(255) NOT NULL, " + 
				" address CHAR(15)," +
				" lobby_id INT, " +
				" FOREIGN KEY(lobby_id) REFERENCES lobby(lobby_id)," +
	            " PRIMARY KEY(player_id)" +
	            ");";
		try {
			stmt.executeUpdate(sqlPlayer);
		} catch (SQLException e) {
			System.out.println(" createTablePlayer");
			e.printStackTrace();
		}
	}
	/**
	 * Methode erstellt die Tabellen für die Nachbarn
	 */
	static void createTableNeighbor() {
		String sqlNeighbor =
				" CREATE TABLE IF NOT EXISTS neighbor (" +
				" country_id INT NOT NULL, " +
				" neighbor_id INT NOT NULL, " +
				" FOREIGN KEY(country_id) REFERENCES country(country_id), " +
				" FOREIGN KEY(neighbor_id) REFERENCES country(country_id), " +
				" PRIMARY KEY(country_id, neighbor_id) " +
				");";
		try {
			stmt.executeUpdate(sqlNeighbor);
		} catch (SQLException e) {
			System.out.println("createTableNeighbor");
			e.printStackTrace();
		}	
	}
	/**
	 * Methode erstellt die Tabellen für die Lobby
	 */
	static void createTableLobby() {
		String sqlLobby =
				" CREATE TABLE IF NOT EXISTS lobby (" +
				" lobby_id INT NOT NULL AUTO_INCREMENT, " +
				" date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
				" last_change INT, " +
				" player_order VARCHAR(255), " +
				" leader_id INT, " +
				" player_turn_id INT, " +
				" FOREIGN KEY(leader_id) REFERENCES player(player_id)," +
				" FOREIGN KEY(player_turn_id) REFERENCES player(player_id)," +
				" PRIMARY KEY(lobby_id) " +
				");";
		try {
			stmt.executeUpdate(sqlLobby);
		} catch (SQLException e) {
			System.out.println("createTableLobby");
			e.printStackTrace();
		}	
	}
	/**
	 * Methode erstellt die Tabellen für Handkarten
	 */
	static void createTableCard() {
		String sqlCard =
				" CREATE TABLE IF NOT EXISTS card (" +
				" card_id INT NOT NULL, " +
				" value INT, " +
				" country_id INT, " +
				" FOREIGN KEY(country_id) REFERENCES country(country_id), "+
				" PRIMARY KEY(card_id) " +
				");";
		try {
			stmt.executeUpdate(sqlCard);
		} catch (SQLException e) {
			System.out.println("createTableCard");
			e.printStackTrace();
		}	
	}
	/**
	 * Methode erstellt die Tabellen für alle Missionen
	 */
	static void createTableMission() {
		String sqlMission =
				" CREATE TABLE IF NOT EXISTS mission (" +
				" mission_id INT NOT NULL, " +
				" description VARCHAR(500), " +
				" PRIMARY KEY(mission_id)" +
				");";
		try {
			stmt.executeUpdate(sqlMission);
		} catch (SQLException e) {
			System.out.println("createTableMission");
			e.printStackTrace();
		}	
	}
	/**
	 * Methode erstellt die Tabelle für Spieler und ihre Karten
	 */
	static void createTableCardsPlayer() {
		String sqlCardsPlayer =
				" CREATE TABLE IF NOT EXISTS cards_player (" +
				" lobby_id INT, " +
				" card_id INT, " +
				" player_id INT, " +
				" FOREIGN KEY(player_id) REFERENCES player(player_id), " +
				" FOREIGN KEY(card_id) REFERENCES card(card_id), " +
				" FOREIGN KEY(lobby_id) REFERENCES lobby(lobby_id), " +
				" PRIMARY KEY(card_id, lobby_id) " +
				");";
		try {
			stmt.executeUpdate(sqlCardsPlayer);
		} catch (SQLException e) {
			System.out.println("createTableCardsPlayer");
			e.printStackTrace();
		}	
	}
	/**
	 * Methode erstellt Tabelle für Missionen und Spieler
	 */
	static void createTableMissionPlayer() {
		String sqlMissionPlayer =
				" CREATE TABLE IF NOT EXISTS mission_player (" +
				" mission_id INT NOT NULL, " +
				" player_id INT, " +
				" lobby_id INT, " +
				" FOREIGN KEY(player_id)  REFERENCES player(player_id), " +
				" FOREIGN KEY(lobby_id)   REFERENCES lobby(lobby_id), " +
				" FOREIGN KEY(mission_id) REFERENCES mission(mission_id), " +
				" PRIMARY KEY(lobby_id, mission_id) " +
				");";
		try {
			stmt.executeUpdate(sqlMissionPlayer);
		} catch (SQLException e) {
			System.out.println("createTableMissionPlayer");
			e.printStackTrace();
		}	
	}
	/**
	 * @author Lea Mönikes
	 * Methode erstellt Tabelle für Spieler, Länder und Farben
	 */
	static void createTableCountryPlayer() {
		String sqlPlayerCountry =
				" CREATE TABLE IF NOT EXISTS country_player (" +
				" player_id INT, "+
				" country_id INT, "+
				" lobby_id INT, "+
				" unit_count INT, " +
				" FOREIGN KEY(player_id) REFERENCES player(player_id), " +
				" FOREIGN KEY(country_id) REFERENCES country(country_id), " +
				" FOREIGN KEY(lobby_id) REFERENCES lobby(lobby_id), " +
				" PRIMARY KEY(country_id, lobby_id) " +
				");";
		try {
			stmt.executeUpdate(sqlPlayerCountry);
		} catch (SQLException e) {
			System.out.println("createTableCountryPlayer");
			e.printStackTrace();
		}
	}
	/**
	 * @author Lea Mönikes
	 * Methode erstellt die Tabellen für alle auswählbaren Farben
	 */
	static void createTableColor() {
		String sqlPlayerCountry =
				" CREATE TABLE IF NOT EXISTS color (" +
				" color_id INT, " +
				" name VARCHAR(7), " +
				" value VARCHAR(6), " +
				" PRIMARY KEY(color_id) " +
				");";
		try {
			stmt.executeUpdate(sqlPlayerCountry);
		} catch (SQLException e) {
			System.out.println("createTableColor");
			e.printStackTrace();
		}
	}
	/**
	 * Methode erstellt die Tabellen für den Chat
	 */
	static void createTableChat() {
		String sqlChat =
				" CREATE TABLE IF NOT EXISTS chat (" +
				" message_id INT PRIMARY KEY AUTO_INCREMENT, " +
				" player_id INT REFERENCES player(player_id), " +
				" lobby_id INT REFERENCES lobby(lobby_id), " +
				" timestamp LONG NOT NULL, " +
				" message VARCHAR(255)"
				+");";
		try {
			stmt.executeUpdate(sqlChat);
		} catch (SQLException e) {
			System.out.println("createTableChat");
			e.printStackTrace();
		}
	}
	

	/**
	 * @author Lea Mönikes
	 * Methode erstellt die Tabellen für die Farbe eines Spielers in einer Lobby
	 */
	static void createTableColorPlayer(){
		String sqlColorPlayer =
				" CREATE TABLE IF NOT EXISTS color_player (" +
				" player_id INT, "+
				" color_id INT, "+
				" lobby_id INT, "+
				" FOREIGN KEY(player_id) REFERENCES player(player_id), " +
				" FOREIGN KEY(lobby_id) REFERENCES lobby(lobby_id), " +
				" FOREIGN KEY(color_id) REFERENCES color(color_id), " +
				" PRIMARY KEY(lobby_id, color_id) " +
				");";
		try {
			stmt.executeUpdate(sqlColorPlayer);
		} catch (SQLException e) {
			System.out.println("createTableColorPlayer");
			e.printStackTrace();
		}
	}

}
