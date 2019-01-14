package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlHelper {
	
	private static Statement stmt = null;
	private static Connection con;
	
	/*
	 * String Array der Logindaten der Datenbank
	 * 0=Addresse
	 * 1=Datenbankname
	 * 2=Password
	 * Erstellt ein Statement mit den Werten
	 */
	private static String[] loginStringArray =  {"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17azz","pbs2h17azz","Bib12345"};

	/**
	 * Versucht ein neues Statement zu erstellen
	 */
	private static void createStatement() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(loginStringArray[0],loginStringArray[1],loginStringArray[2]);
			stmt = con.createStatement();
		} catch (ClassNotFoundException e) {
			System.out.println("Driver nicht richtig eingebunden!");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Sql Error!");
			e.printStackTrace();
		}
	}

	/**
	 * Gibt ein Statement zur�ck
	 * Checkt ob das Statement vorhande ist (nicht NULL)
	 * Sonst erstellt es ein neues Statement
	 * @return aktuelles Statement der Verbindung
	 */
	public static Statement getStatement()  {
		if(stmt == null) {
			createStatement();
		}

		return stmt;
	}
	
	public static int[] getAllLobbyId() {
		ResultSet rs = null;
		ArrayList<Integer> lobbyIdList = new ArrayList<Integer>();
		
		try {
			rs = getStatement().executeQuery("SELECT lobby_id FROM lobby;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			while(rs.next()) {
				lobbyIdList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Konvertiere Integer Liste in Integer Array
		return lobbyIdList.stream().mapToInt(Integer::intValue).toArray();
	}
	
	public static void clearTable(String tableName) {
		try { 
			stmt.executeUpdate("TRUNCATE TABLE "+tableName+";");
		} catch(SQLException s) {
			s.printStackTrace();
		}
	}
	
	public static int[] getAllCountryId() throws SQLException {
		ResultSet rs = getStatement().executeQuery("SELECT country_id FROM country;");
		int[] countryIdArray = new int[42];
		int i = 0;
		
		while(rs.next()) {
			countryIdArray[i] = rs.getInt(1);
			i++;
		}
		
		return countryIdArray;
		
	}
	
	public static void createPlayer(int playerId) {
		
	}
	
	public static void createPlayer(int playerId, int lobbyId) {
		
	}
	
	public static String getPlayerName(int playerId) throws SQLException {
		ResultSet rs = getStatement().executeQuery("SELECT name FROM player WHERE player_id = "+playerId+";");
		
		 rs.next(); 
		 return rs.getString(1);
	}
	
	public static String getCountryName (int countryId) throws SQLException {
		ResultSet rs = getStatement().executeQuery("SELECT name FROM country WHERE country_id ="+countryId);
	
		 rs.next(); 
			 return rs.getString(1);	
	}
	
	public static int getCountryContinentId(int countryId) throws SQLException{
		ResultSet rs = getStatement().executeQuery("SELECT continent_id FROM country WHERE country_id ="+countryId);
		
		 rs.next(); 
		 	return rs.getInt(1);	
	}

	public static int[] getCountryNeighbor(int countryId) {
		ResultSet rs = null;
		try {
			rs = getStatement().executeQuery("SELECT neighbor_id FROM neighbor WHERE country_id ="+countryId);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ArrayList<Integer> neighborIdList = new ArrayList<Integer>();
		int[] neighborIdArray = null;
		
		try {
			while(rs.next()) {
				neighborIdList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Konvertiere Integer Liste in Integer Array
		neighborIdArray = neighborIdList.stream().mapToInt(Integer::intValue).toArray();

		return neighborIdArray;	 	
	}

	public static String getCountrySVG(int countryId) throws SQLException {
		ResultSet rs = getStatement().executeQuery("SELECT svg FROM country WHERE country_id = "+countryId);
		rs.next();
			return  rs.getString(1);
	}
	
	public static String[] getAllCountrySVG() {
		String[] data = new String[42];
		int i = 0;
		try {
			ResultSet rs = getStatement().executeQuery("SELECT svg FROM country");
			while(rs.next()) {
				data[i] = rs.getString(1);
				i++;
			}
		} catch (SQLException e) {
			//do nothing
		}

		return data;
	}
	
	//TODO outdated
	public static void createLobby (Player player) throws SQLException, ClassNotFoundException {
		String dbStatement = "INSERT INTO lobby (host_id) values (" + player.getId() + ");";
		getStatement().executeQuery(dbStatement);
	}
	
	public static String getContintentName(int continentID) throws SQLException{		
		ResultSet rs = getStatement().executeQuery("SELECT name FROM continent where continent_id = "+continentID+";");
		rs.next();
		return rs.getString("name");
	}
	
	public static int Bonus(int continentID) throws SQLException{
		ResultSet rs = getStatement().executeQuery("SELECT bonus FROM continent where continent_id = "+continentID+";");
		rs.next();
		return rs.getInt("bonus");
	}
	
	public static List <String> ContinentCountries(int continentID) throws SQLException{
		List <String> countries = new ArrayList<>();
		ResultSet rs = getStatement().executeQuery("SELECT name FROM country where continent_id = "+continentID+";");
		while(rs.next()){	   		
	   		countries.add(rs.getString("name"));	   				   			
	   		}
		rs.next();
		return countries;
	}
	

	public static int getPlayerID(String name) throws SQLException{
		ResultSet rs = getStatement().executeQuery("SELECT player_id FROM player WHERE name = "+name+";");
		
		 rs.next(); 
		 return rs.getInt(1);	
	}
	
	public static int getCardCountryId(int cardId) throws SQLException{
		ResultSet rs = getStatement().executeQuery("SELECT country_id FROM card WHERE card_id = "+cardId+";");
		
		rs.next();
			return rs.getInt("country_id");
	}

	public static int getCardValue(int value) throws SQLException{
		ResultSet rs = getStatement().executeQuery("SELECT value FROM card WHERE value = "+value+";");
		
		rs.next();
		return rs.getInt("value");
	}

	public static String getMissionDescription(int missionID) throws SQLException {
		ResultSet rs = getStatement().executeQuery("SELECT description FROM mission WHERE mission_id = "+missionID+";");
		rs.next();
		return rs.getString("description");
	}
}
