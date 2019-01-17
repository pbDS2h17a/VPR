package sqlCreation;

import sqlConnection.Player;
import sqlConnection.SqlHelper;

import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author basti
 * SQL Queries zum erstellen der Stammdatenbank
 */
public class SqlQuery {
	//TODO prepared statements https://docs.oraclecom/javase/tutorial/jdbc/basics/prepared.html
	//TODO Tabellen Namen als Variablen auslagern
	//TODO Validieren der Fill Statements mit aktueller modelierung!  Andere Teams Fragen!!!!
	//TODO createChat hier implemntierung
	//TODO statt alle Drop statments einzeln auszuführen komplette Datenbank droppen (performance effizentier)
	public static String splitter = ";";

	public static Statement stmt = SqlHelper.getStatement();
	
	public static void fillTestData() {
		try {
			stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser1','127.0.0.1', 1 ,1)");
			stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser2','127.0.0.1', 1 ,2)");
			stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser3','127.0.0.1', 1 ,3)");
			stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser4','127.0.0.1', 1 ,4)");
			stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser5','127.0.0.1', 1 ,5)");
			stmt.executeUpdate("INSERT INTO player VALUES(NULL,'Testuser6','127.0.0.1', 1 ,6)");
			stmt.executeUpdate("INSERT INTO lobby VALUES(NULL,DEFAULT,NULL,1,1,1)");
		} catch (SQLException e) {
			System.out.println("fillTestData");
			e.printStackTrace();
		}
		System.out.println("Testdaten einfügen");
	}
	
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
			
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("fillContinent");
				e.printStackTrace();
			}
		}
	}	

	//TODO implement lobby und address
	
	public static void fillPlayer(Player player) {
		String sql =
				"INSERT INTO player (name, color, lobby_id, address)" +
				"VALUES ('"+player.getName()+"', '"+player.getColor()+"', NULL, NULL);";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("fillPlayer");
			e.printStackTrace();
		}
	}

	/**
	 *
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
			
			// SQL ausführen
			try {
				stmt.executeUpdate(sqlCountry);
			} catch (SQLException e) {
				System.out.println("fillCountry");
				e.printStackTrace();
			}		
		}	
	}

	/**
	 *
	 * @param data StringArray mit LandID und variablen vielen NachbarIDs
	 */
	static void fillNeighbor(String[] data) {
		for (String string : data) {
			String[] dataArray = string.split(splitter);
			String id = dataArray[0].trim();
			String sqlNeighbor = "";
			
			// Nachbarn
			for(int i = 3; i < (dataArray.length-1); i++) {
				sqlNeighbor = 
					"INSERT INTO neighbor (country_id, neighbor_id)" +
					"VALUES('"+id+"', '"+dataArray[i].trim()+"');";
				try {
					stmt.executeUpdate(sqlNeighbor);
				} catch (SQLException e) {
					System.out.println("fillNeighbor");
					e.printStackTrace();
				}
			}
		}
		
		
		
	}

	//TODO in SqlHelper auslagern
//	static void fillPlayerCountry(Player p) {
//		List<Country> countryList = p.getCountryList();
//		
//		for (Country country : countryList) {
//			String sql =
//					"INSERT INTO player_country (player_id, country_id)" +
//					"VALUES ('"+p.getId()+"', '"+country.getId()+"');";
//			try {
//				stmt.executeUpdate(sql);
//			} catch (SQLException e) {
//				System.out.println("fillPlayer");
//				e.printStackTrace();
//			}
//		}
//	}

	/**
	 *
	 * @param data StringArray mit MissionID und Beschreibung
	 */
	static void fillMissions(String[] data) {	
		for (String string : data) {
			String[] dataArray = string.split(splitter);
			String id = dataArray[0];
			String description = dataArray[1];
			String sql = "INSERT INTO mission(mission_id, description)"
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
			String sql = "INSERT INTO card(card_id, value, country_id)"
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
	 *
	 * @param data stringArray mit FarbID, Namen und wert
	 *             Wert ist ein 6-Stelliger HEX code
	 */
	static void fillColor(String[] data){
		for (String string : data) {
			
			String[] dataArray = string.split(splitter);
			String colorId = dataArray[0].trim();
			String colorName = dataArray[1].trim();
			String value = dataArray[2].trim();
			
			String sql = "INSERT INTO color(color_id, name, value)"
					+"VALUES('"+colorId+"','"+colorName+"','"+value+"');";
			
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("fillColor");
				e.printStackTrace();
			}
		}
		
	}

	public static void disableForeignKeyConstraints() {
		try {
			stmt.executeUpdate("SET foreign_key_checks = 0");

		} catch (SQLException e) {
			System.out.println("disableForeignKeyRestraints");
			e.printStackTrace();
		}
	}

	public static void enableForeignKeyConstraints() {
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
	public static void dropTable(String tableName) {
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
	static void createContinent() {
		//Kontinente
		String sqlContinent = "CREATE TABLE IF NOT EXISTS continent (" +
				" continent_id INT, " +
                " name VARCHAR(255), " +
				" bonus INT," +
				" PRIMARY KEY(continent_id)" +
				");";
		
		try {
			stmt.executeUpdate(sqlContinent);
		} catch (Exception e) {
			System.out.println("createContinent");
			e.printStackTrace();
		}	
	}
	
	static void createCountry() {
		//Länder
		String sqlCountry = "CREATE TABLE IF NOT EXISTS country (" +
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
			System.out.println("createCountry");
			e.printStackTrace();
		}	
	}
	
	static void createPlayer() {
		//Länder
		String sqlPlayer = "CREATE TABLE IF NOT EXISTS player (" +
				" player_id INT NOT NULL AUTO_INCREMENT, " +/**/
	            " name VARCHAR(255) NOT NULL, " + 
				" address CHAR(15)," +
				" lobby_id INT, " +
				" color_id INT, " +
				" FOREIGN KEY(lobby_id) REFERENCES lobby(lobby_id)," +
				" FOREIGN KEY(color_id) REFERENCES color(color_id)," +
	            " PRIMARY KEY(player_id)" +
	            ");";
		
		try {
			stmt.executeUpdate(sqlPlayer);
		} catch (SQLException e) {
			System.out.println(" createPlayer");
			e.printStackTrace();
		}
	}
	
	static void createNeighbor() {
		String sqlNeighbor = "CREATE TABLE IF NOT EXISTS neighbor (" +
				" country_id INT NOT NULL, " +
				" neighbor_id INT NOT NULL, " +
				" FOREIGN KEY(country_id) REFERENCES country(country_id), " +
				" FOREIGN KEY(neighbor_id) REFERENCES country(country_id), " +
				" PRIMARY KEY(country_id, neighbor_id) " +
				");";
		try {
			stmt.executeUpdate(sqlNeighbor);
		} catch (SQLException e) {
			System.out.println("createNeighbor");
			e.printStackTrace();
		}	
	}
	
	static void createLobby() {
		String sqlLobby = "CREATE TABLE IF NOT EXISTS lobby (" +
				" lobby_id INT NOT NULL AUTO_INCREMENT, " +
				" date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
				" last_change DATETIME, " +
				" player_order VARCHAR(255), " +
				" leader_id INT NOT NULL, " +
				" player_turn_id INT, " +
				" FOREIGN KEY(leader_id) REFERENCES player(player_id)," +
				" FOREIGN KEY(player_turn_id) REFERENCES player(player_id)," +
				" PRIMARY KEY(lobby_id) " +
				");";
		try {
			stmt.executeUpdate(sqlLobby);
		} catch (SQLException e) {
			System.out.println("createLobby");
			e.printStackTrace();
		}	
	}

	static void createCard() {
		String sqlCard = "CREATE TABLE IF NOT EXISTS card (" +
				" card_id INT NOT NULL, " +
				" value INT, " +
				" country_id INT, " +
				" FOREIGN KEY(country_id) REFERENCES country(country_id), "+
				" PRIMARY KEY(card_id) " +
				");";
		try {
			stmt.executeUpdate(sqlCard);
		} catch (SQLException e) {
			System.out.println("createCard");
			e.printStackTrace();
		}	
	}
	
	static void createMission() {
		String sqlMission = "CREATE TABLE IF NOT EXISTS mission (" +
				" mission_id INT NOT NULL, " +
				" description VARCHAR(500), " +
				" PRIMARY KEY(mission_id)" +
				");";
		try {
			stmt.executeUpdate(sqlMission);
		} catch (SQLException e) {
			System.out.println("createMission");
			e.printStackTrace();
		}	
	}
	
	static void createCardsPlayer() {
		String sqlCardsPlayer = "CREATE TABLE IF NOT EXISTS cards_player (" +
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
			System.out.println("createCardsPlayer");
			e.printStackTrace();
		}	
	}
	
	static void createMissionPlayer() {
		String sqlMissionPlayer = "CREATE TABLE IF NOT EXISTS mission_player (" +
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
			System.out.println("createMissionPlayer");
			e.printStackTrace();
		}	
	}
	
	static void createCountryPlayer() {
		String sqlPlayerCountry = "CREATE TABLE IF NOT EXISTS country_player (" +
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
			System.out.println("createCountryPlayer");
			e.printStackTrace();
		}
	}

	static void createColor() {
		String sqlPlayerCountry = "CREATE TABLE IF NOT EXISTS color (" +
				" color_id INT, " +
				" name VARCHAR(7), " +
				" value VARCHAR(6), " +
				" PRIMARY KEY(color_id) " +
				");";
		try {
			stmt.executeUpdate(sqlPlayerCountry);
		} catch (SQLException e) {
			System.out.println("createColor");
			e.printStackTrace();
		}
	}
	
	static void createChat() {
		String sqlChat = "CREATE TABLE IF NOT EXISTS chat (" +
				"message_id INT PRIMARY KEY AUTO_INCREMENT, " +
				"pid INT REFERENCES player(pid), " +
				"lid INT REFERENCES lobby(lid), " +
				"timestamp LONG NOT NULL, " +
				"message VARCHAR(255));";
		try {
			stmt.executeUpdate(sqlChat);
		} catch (SQLException e) {
			System.out.println("createChat");
			e.printStackTrace();
		}
	}

}
