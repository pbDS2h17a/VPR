package mySQL;

import java.sql.SQLException;
import java.sql.Statement;

public class SqlQuery {
	
	static void fillContinent(Statement stmt,String[] data) throws SQLException {
		for (String string : data) {
			String[] dataArray = string.split(",");
			String id = dataArray[0].trim();
			String name = dataArray[1].trim();
			String bonus = dataArray[2].trim();
			
			String sql =
					"INSERT INTO continent (continent_id, continent_name, bonus)" +
					"VALUES ('"+id+"', '"+name+"', '"+bonus+"');";
				
			stmt.executeUpdate(sql);
		}
	}
	
	static void fillCountry(Statement stmt,String[] data) throws SQLException {
		for (String string : data) {
			String[] dataArray = string.split(",");
			String id = dataArray[0].trim();
			String name = dataArray[1].trim();
			String continent = dataArray[2].trim();
			
			String sql =
					"INSERT INTO country (country_id, country_name, country_continent_id)" +
					"VALUES ('"+id+"', '"+name+"', '"+continent+"');";
			
			for(int i = 3; i < dataArray.length; i++) {
				
				String sql2 = 
						"INSERT INTO neighbor (country_id, neighbor_id)" +
						"VALUES('"+id+"', '"+dataArray[i].trim()+"');";
				stmt.executeUpdate(sql2);
			}
				
			stmt.executeUpdate(sql);
		}
		
		
	}
	
	static void dropCountry(Statement stmt) throws SQLException {
		stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
		stmt.executeUpdate("DROP TABLE IF EXISTS country");
		stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
	}
	
	static void dropNeighbor(Statement stmt) throws SQLException {
		stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
		stmt.executeUpdate("DROP TABLE IF EXISTS neighbor");
		stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
	}
	
	static void dropContinent(Statement stmt) throws SQLException {
		stmt.executeUpdate("DROP TABLE IF EXISTS continent");
	}
	
	static void createContinent(Statement stmt) throws SQLException {
		//Kontinente
		String sqlContinent = "CREATE TABLE IF NOT EXISTS continent (" +
				" continent_id INTEGER, " +
                " continent_name VARCHAR(255), " +
				" bonus INTEGER," +
				" PRIMARY KEY (continent_id)" +
				");";
		
		stmt.executeUpdate(sqlContinent);	
	}
	
	static void createCountry(Statement stmt) throws SQLException {
		//Länder
		String sqlCountry = "CREATE TABLE IF NOT EXISTS country (" +
				" country_id INTEGER, " +
	            " country_name VARCHAR(255) NOT NULL, " + 
				" country_continent_id INTEGER, " +
				" PRIMARY KEY (country_id)" +
	            ");";
		
		stmt.executeUpdate(sqlCountry);	
	}
	
	static void createPlayer(Statement stmt) throws SQLException {
		//Länder
		String sqlPlayer = "CREATE TABLE IF NOT EXISTS player (" +
				" player_id INTEGER, " +
	            " player_name VARCHAR(255) NOT NULL, " + 
				" country_continent_id INTEGER, " +
				" PRIMARY KEY (country_id)" +
	            ");";
		
		stmt.executeUpdate(sqlPlayer);	
	}
	
	
	static void createNeighbor(Statement stmt) throws SQLException {
		String sqlNeighbor = "CREATE TABLE IF NOT EXISTS neighbor (" +
				"nid int NOT NULL AUTO_INCREMENT, " +
				"country_id int, " +
				"neighbor_id int, " +
				"PRIMARY KEY(nid) " +
				");";
		
		stmt.executeUpdate(sqlNeighbor);	
	}
	
}
