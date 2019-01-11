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

	public static Statement stmt = SqlHelper.createStatement();

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

	static void disableForeignKeyConstraints() {
		try {
			stmt.executeUpdate("SET foreign_key_checks = 0");

		} catch (SQLException e) {
			System.out.println("disableForeignKeyRestraints");
			e.printStackTrace();
		}
	}

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
	static void dropCountry() { 
		try {
			stmt.executeUpdate("SET foreign_key_checks = 0");
			stmt.executeUpdate("DROP TABLE IF EXISTS country");
		} catch (SQLException e) {
			System.out.println("dropCountry");
			e.printStackTrace();
		}	
	}
	
	static void dropNeighbor(){
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS neighbor");
		} catch (SQLException e) {
			System.out.println("dropNeighbor");
			e.printStackTrace();
		}
	}
	
	static void dropContinent() {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS continent");
		} catch (Exception e) {
			System.out.println("dropContinent");
			e.printStackTrace();
		}
	}
	
	static void dropPlayer() {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS player");
		} catch (Exception e) {
			System.out.println("dropPlayer");
			e.printStackTrace();
		}
	}
	
	static void dropPlayerCountry() {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS player_country");
		} catch (Exception e) {
			System.out.println("dropPlayerCountry");
			e.printStackTrace();
		}
	}
	
	static void dropLobby() {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS lobby");
		} catch (Exception e) {
			System.out.println("dropLobby");
			e.printStackTrace();
		}
	}
	
	static void dropCard() {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS card");
		} catch (Exception e) {
			System.out.println("dropCard");
			e.printStackTrace();
		}
	}
	
	static void dropMission() {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS mission");
		} catch (Exception e) {
			System.out.println("dropMission");
			e.printStackTrace();
		}
	}
	
	static void dropMissionPlayer() {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS mission_player");
		} catch (Exception e) {
			System.out.println("dropMissionPlayer");
			e.printStackTrace();
		}
	}
	
	static void dropCardsPlayer() {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS cards_player");
		} catch (Exception e) {
			System.out.println("dropCardPlayer");
			e.printStackTrace();
		}
	}
	
	static void dropColor() {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS color");
		} catch (Exception e) {
			System.out.println("dropColor");
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
				" address INT(20)," +
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
				" country_id INT, " +
				" neighbor_id INT, " +
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
				" date DATETIME, " +
				" passwort VARCHAR(255), " +
				" last_change DATETIME, " +
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
	
	static void createPlayerCountry() {
		String sqlPlayerCountry = "CREATE TABLE IF NOT EXISTS player_country (" +
				" player_id INT, "+
				" country_id INT, "+
				" lobby_id INT, "+
				" army_count INT, " +
				" FOREIGN KEY(player_id) REFERENCES player(player_id), " +
				" FOREIGN KEY(country_id) REFERENCES country(country_id), " +
				" FOREIGN KEY(lobby_id) REFERENCES lobby(lobby_id), " +
				" PRIMARY KEY(country_id, lobby_id) " +
				");";
		try {
			stmt.executeUpdate(sqlPlayerCountry);
		} catch (SQLException e) {
			System.out.println("createPlayerCountry");
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
	
}
