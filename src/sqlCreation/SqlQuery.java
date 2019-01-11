package sqlCreation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import sqlConnection.Country;
import sqlConnection.Player;
import sqlConnection.SqlHelper;

/**
 * @author basti
 * SQL Queries zum erstellen der Stammdatenbank
 */
public class SqlQuery {
	//TODO implement Fillstatements neighbor(SRD), card
	//TODO prepared statements https://docs.oraclecom/javase/tutorial/jdbc/basics/prepared.html
	//TODO Tabellen Namen als Variablen auslagern
	//TODO Validieren der Create & Fill Statements mit aktueller modelierung!  Andere Teams Fragen!!!!
	public static String splitter = ";";

	
	public static Statement stmt = SqlHelper.stmt;
	/*
	 * _____________________________________________________________________________________________________________________________________________________
	 * FILL STATEMENTS
	 */

	
	//#######################################################################
	//FILL STATEMENTS
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
//			if(!player.getCountryList().isEmpty()) {
//				fillPlayerCountry(player);
//			}
		} catch (SQLException e) {
			System.out.println("fillPlayer");
			e.printStackTrace();
		}
	}
	// Hier auch Neighbors befüllt!
	//TODO in eigene Methode auslagern?
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
	
	static void fillCard(String[] data) {	
		for (String string : data) {
			String[] dataArray = string.split(splitter);
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
	
	static void fillColor(String[] data){
		for (String string : data) {
			
			String[] dataArray = string.split(splitter);
			String colorId = dataArray[0].trim();
			String colorName = dataArray[1].trim();
			String value = dataArray[2].trim();
			
			String sql = "INSERT INTO color(color_id, color_name, color_value)"
					+"VALUES('"+colorId+"','"+colorName+"','"+value+"');";
			
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("fillColor");
				e.printStackTrace();
			}
		}
		
	}
	
	
	//#############################################################################
	//#DROP STATEMENTS
	static void dropCountry() { 
		try {
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
	
	//###########################################################################
	// CREATE STATEMENTS
	static void createContinent() {
		//Kontinente
		String sqlContinent = "CREATE TABLE IF NOT EXISTS continent (" +
				" continent_id INTEGER, " +
                " name VARCHAR(255), " +
				" bonus INTEGER," +
				" PRIMARY KEY (continent_id)" +
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
				" country_id INTEGER, " +
	            " name VARCHAR(255) NOT NULL, " + 
				" continent_id INTEGER REFERENCES continent(countinent_id), " +
				" svg VARCHAR(15000), " +
				" PRIMARY KEY (country_id)" +
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
				" player_id INTEGER NOT NULL AUTO_INCREMENT, " +
	            " name VARCHAR(255) NOT NULL, " + 
				" color VARCHAR(255) NOT NULL," +
				" lobby_id INTEGER REFERENCES lobby(lobby_id)," +
				" address INTEGER(20)," +
	            " PRIMARY KEY (player_id)" +
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
				"country_id INTEGER REFERENCES country(country_id), " +
				"neighbor_id INTEGER REFERENCES country(country_id), " +
				"PRIMARY KEY(country_id, neighbor_id) " +
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
				"lobby_id INTEGER NOT NULL AUTO_INCREMENT, " +
				"date DATETIME," +
				"passwort VARCHAR(255)," +
				"last_change DATETIME," +
				"player_order VARCHAR(255)," +
				"leader_id INTEGER REFERENCES player(player_id)," +
				"players_turn_id INTEGER REFERENCES player(player_id)," +
				"PRIMARY KEY(lobby_id) " +
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
				"card_id INTEGER NOT NULL, " +
				"value INTEGER, " +
				"country_id INTEGER REFERENCES country(country_id), "+
				"PRIMARY KEY(card_id) " +
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
				"mission_id INTEGER NOT NULL, " +
				"description VARCHAR(500) " +
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
				"player_id INTEGER REFERENCES player(player_id), " +
				"card_id INTEGER REFERENCES card(card_id), " +
				"lobby_id INTEGER REFERENCES lobby(lobby_id), " +
				"PRIMARY KEY(card_id, lobby_id) " +
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
				"mission_id INTEGER REFERENCES mission(mission_id), " +
				"player_id INTEGER REFERENCES player(player_id), " +
				"lobby_id INTEGER REFERENCES lobby(lobby_id), " +
				"PRIMARY KEY(lobby_id, mission_id) " +
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
				"player_id INTEGER REFERENCES player(player_id), " +
				"country_id INTEGER REFERENCES country(country_id), " +
				"lobby_id INTEGER REFERENCES lobby(lobby_id), " +
				"army_count INTEGER , " +
				"PRIMARY KEY(country_id, lobby_id) " +
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
				"color_id INTEGER, " +
				"color_name VARCHAR(7), " +
				"color_value VARCHAR(6), " +
				"PRIMARY KEY(color_id) " +
				");";
		try {
			stmt.executeUpdate(sqlPlayerCountry);
		} catch (SQLException e) {
			System.out.println("createColor");
			e.printStackTrace();
		}	
	}
	
}
