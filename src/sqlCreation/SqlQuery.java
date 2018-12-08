package sqlCreation;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author basti
 * SQL Queries zum erstellen der Stammdatenbank
 */
public class SqlQuery {
	
	
	/**
	 * @param stmt SQL statement
	 * @param data String[] der Werte
	 */
	static void fillContinent(Statement stmt,String[] data) {
		for (String string : data) {
			String[] dataArray = string.split(",");
			String id = dataArray[0].trim();
			String name = dataArray[1].trim();
			String bonus = dataArray[2].trim();
			
			String sql =
					"INSERT INTO continent (continent_id, continent_name, bonus)" +
					"VALUES ('"+id+"', '"+name+"', '"+bonus+"');";
			
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("fillContinent");
				e.printStackTrace();
			}
		}
	}
	
	static void fillCountry(Statement stmt,String[] data) {
		for (String string : data) {
			String[] dataArray = string.split(",");
			String id = dataArray[0].trim();
			String name = dataArray[1].trim();
			String continent = dataArray[2].trim();
			String sqlNeighbor = "";
			String sqlCountry =
					"INSERT INTO country (country_id, country_name, country_continent_id)" +
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
	
	static void dropCountry(Statement stmt) { 
		try {
			stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
			stmt.executeUpdate("DROP TABLE IF EXISTS country");
			stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
		} catch (SQLException e) {
			System.out.println("dropCountry");
			e.printStackTrace();
		}	
	}
	
	static void dropNeighbor(Statement stmt) throws SQLException {
		try {
			stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
			stmt.executeUpdate("DROP TABLE IF EXISTS neighbor");
			stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
		} catch (SQLException e) {
			System.out.println("dropNeighbor");
			e.printStackTrace();
		}
	}
	
	static void dropContinent(Statement stmt) {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS continent");
		} catch (Exception e) {
			System.out.println("dropContinent");
			e.printStackTrace();
		}
	}
	
	static void dropPlayer(Statement stmt) {
		try {
			stmt.executeUpdate("DROP TABLE IF EXISTS player");
		} catch (Exception e) {
			System.out.println("dropPlayer");
			e.printStackTrace();
		}
	}
	
	static void createContinent(Statement stmt) {
		//Kontinente
		String sqlContinent = "CREATE TABLE IF NOT EXISTS continent (" +
				" continent_id INTEGER, " +
                " continent_name VARCHAR(255), " +
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
	
	static void createCountry(Statement stmt) {
		//Länder
		String sqlCountry = "CREATE TABLE IF NOT EXISTS country (" +
				" country_id INTEGER, " +
	            " country_name VARCHAR(255) NOT NULL, " + 
				" country_continent_id INTEGER, " +
				" PRIMARY KEY (country_id)" +
	            ");";
		
		try {
			stmt.executeUpdate(sqlCountry);
		} catch (SQLException e) {
			System.out.println("createCountry");
			e.printStackTrace();
		}	
	}
	
	static void createPlayer(Statement stmt) {
		//Länder
		String sqlPlayer = "CREATE TABLE IF NOT EXISTS player (" +
				" player_id INTEGER, " +
	            " player_name VARCHAR(255) NOT NULL " + 
	            ");";
		
		try {
			stmt.executeUpdate(sqlPlayer);
		} catch (SQLException e) {
			System.out.println(" createPlayer");
			e.printStackTrace();
		}	
	}
	
	static void createNeighbor(Statement stmt) {
		String sqlNeighbor = "CREATE TABLE IF NOT EXISTS neighbor (" +
				"nid int NOT NULL AUTO_INCREMENT, " +
				"country_id int, " +
				"neighbor_id int, " +
				"PRIMARY KEY(nid) " +
				");";
		
		try {
			stmt.executeUpdate(sqlNeighbor);
		} catch (SQLException e) {
			System.out.println();
			e.printStackTrace();
		}	
	}
	
}
