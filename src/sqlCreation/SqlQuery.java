package sqlCreation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import sqlConnection.Country;
import sqlConnection.Player;

/**
 * @author basti
 * SQL Queries zum erstellen der Stammdatenbank
 */
public class SqlQuery {
	
	public static Statement stmt;
	
	static void fillContinent(String[] data) {
		for (String string : data) {
			String[] dataArray = string.split(",");
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
	static void fillPlayer(Player player) {			
		String sql =
				"INSERT INTO player (name, color, lobby_id, address)" +
				"VALUES ('"+player.getName()+"', '"+player.getColor()+"');";
		try {
			stmt.executeUpdate(sql);
			if(!player.getCountryList().isEmpty()) {
				fillPlayerCountry(player);
			}
		} catch (SQLException e) {
			System.out.println("fillPlayer");
			e.printStackTrace();
		}
	}
	
	static void fillCountry(String[] data) {
		for (String string : data) {
			String[] dataArray = string.split(",");
			String id = dataArray[0].trim();
			String name = dataArray[1].trim();
			String continent = dataArray[2].trim();
			String sqlNeighbor = "";
			String sqlCountry =
					"INSERT INTO country (country_id, name, continent_id)" +
					"VALUES ('"+id+"', '"+name+"', '"+continent+"');";
			
			for(int i = 3; i < dataArray.length; i++) {
				sqlNeighbor = 
					"INSERT INTO neighbor (country_id, neighbor_id)" +
					"VALUES('"+id+"', '"+dataArray[i].trim()+"');";
			}
			
			try {
				stmt.executeUpdate(sqlNeighbor);
				stmt.executeUpdate(sqlCountry);
			} catch (SQLException e) {
				System.out.println("fillCountry");
				e.printStackTrace();
			}
		}
		
		
	}
	
	static void dropCountry() { 
		try {
			stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
			stmt.executeUpdate("DROP TABLE IF EXISTS country");
			stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
		} catch (SQLException e) {
			System.out.println("dropCountry");
			e.printStackTrace();
		}	
	}
	
	static void dropNeighbor() throws SQLException {
		try {
			stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
			stmt.executeUpdate("DROP TABLE IF EXISTS neighbor");
			stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
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
				" lobby_id INTEGER," +
				" address INTEGER," +
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
	
	static void fillPlayerCountry(Player p) {
		List<Country> countryList = p.getCountryList();
		
		for (Country country : countryList) {
			String sql =
					"INSERT INTO player_country (player_id, country_id)" +
					"VALUES ('"+p.getId()+"', '"+country.getId()+"');";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("fillPlayer");
				e.printStackTrace();
			}
		}
		
	}
	
	static void createPlayerCountry() {
		String sqlNeighbor = "CREATE TABLE IF NOT EXISTS player_country (" +
				"player_id INTEGER REFERENCES player(player_id), " +
				"country_id INTEGER REFERENCES country(country_id), " +
				"PRIMARY KEY(player_id, country_id) " +
				");";
		try {
			stmt.executeUpdate(sqlNeighbor);
		} catch (SQLException e) {
			System.out.println("createPlayerCountry");
			e.printStackTrace();
		}	
	}
	
}
