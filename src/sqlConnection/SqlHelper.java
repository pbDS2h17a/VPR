package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import network.ResultSetManager;

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
	
	// Private TestDb für home server
	// "jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17azz","pbs2h17azz","Bib12345"
	// "jdbc:mysql://localhost/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","123456"
	private static String[] loginStringArray =  {
			"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17azz","pbs2h17azz","Bib12345"
	};

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
	
	public static void closeStatement() {
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Gibt ein Statement zurück
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
	
	public static Player[] getAllPlayersForLobby(int lobbyId) {
		ResultSet rs = null;
		Player[] playerArray = new Player[6];
		int index = 0;
		
		try {
			rs = stmt.executeQuery("SELECT player_id, player.name, value FROM player, color WHERE lobby_id="+lobbyId+" AND player.color_id = color.color_id;");
			while(rs.next()) {
				Player p = new Player(rs.getInt("player_id"), rs.getString("player.name"), rs.getString("value"));
				playerArray[index] = p;
				index++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return playerArray;
	}
	
	public static String[] getAllColors() {
		String[] data = new String[6];
		int i = 0;
		try {
			ResultSet rs = getStatement().executeQuery("SELECT value FROM color");
			while(rs.next()) {
				data[i] = rs.getString(1);
				i++;
			}
		} catch (SQLException e) {
			//do nothing
		}

		return data;
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
	
	public static int[] ContinentCountries(int continentID) throws SQLException{
		List <Integer> countryIdList = new ArrayList<>();
		ResultSet rs = getStatement().executeQuery("SELECT country_id FROM country where continent_id = "+continentID+";");
		while(rs.next()){	   		
	   		countryIdList.add(rs.getInt("country_id"));	   				   			
	   		}
		rs.next();
		
		return countryIdList.stream().mapToInt(Integer::intValue).toArray();
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
	
	public static List<List<String>> getChatHistory(long timestamp, int lid) throws SQLException {
		ResultSet r = stmt.executeQuery(String.format("SELECT p.name, c.timestamp, c.message FROM player p, chat c WHERE p.player_id = c.player_id AND c.lobby_id = %d AND c.timestamp > %d;", lid, timestamp));
		// System.out.println("Call läuft");
		return ResultSetManager.toList(r);
	}
	
	public static void sendMessage(String message, int pid, int lid) throws SQLException
	{
		String sql = String.format("INSERT INTO chat(timestamp, message, player_id, lobby_id) VALUES(CURDATE()*1000000+CURTIME(), '%s', %d, %d);", message, pid, lid);
		System.out.println(sql);
		stmt.executeUpdate(sql);
	}
	
	public static List<List<String>> getLobbies() throws SQLException
	{
		//TODO: WHERE player_6 IS NULL
		ResultSet r = stmt.executeQuery("SELECT * FROM lobby;");
		return ResultSetManager.toList(r);
	}
	
	
	/**
	 * Methode zum einfügen von Daten in die Tabelle country_player
	 * @param lobbyId
	 * @param playerId
	 * @param countryId
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static void insertCountryOwner(int lobbyId, int playerId, int countryId) throws SQLException{
		stmt.executeUpdate("INSERT INTO country_player VALUES("+playerId+","+countryId+","+lobbyId+", 1)");
	};
	/**
	 * Methode zum ändern des Besatzers eines Landes 
	 * @param lobbyId
	 * @param playerId
	 * @param countryId
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static void updateCountryOwner(int lobbyId, int playerId, int countryId)throws SQLException{
		stmt.executeUpdate("UPDATE country_player SET player_id = "+playerId+") WHERE country_id ="+countryId+" AND lobby_id="+lobbyId);
	};
	/**
	 * Methode zum anpassen der Armeen anzahl
	 * @param lobbyId
	 * @param playerId
	 * @param countryId
	 * @param amountUnits
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static void updateUnits(int lobbyId, int playerId, int countryId, int amountUnits) throws SQLException{
		stmt.executeUpdate("UPDATE country_player SET unit_count = "+amountUnits+") WHERE country_id ="+countryId+" AND lobby_id="+lobbyId);
	};
	/**
	 * Methode zum hinzufügen von Player
	 * @param name
	 * @param lobbyId
	 * @param colorId
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static void insertPlayer(String name, int lobbyId, int colorId) throws SQLException{
		stmt.executeUpdate("INSERT INTO player VALUES(NULL,'"+name+"','127.0.0.1', "+lobbyId+" ,"+colorId+")");

	};
	
	/**
	 * Methode zum initiellen hinzufügen der Lobby in die Datenbank
	 * einige felder bleiben hier vorerst Null, da die zugehörigen werte nicht vorhanden sein können.
	 * @param localDateTime
	 * @throws SQLException
	 * @author pbs2h17ath
	 * @return lobbyIdS
	 */
	public static int insertLobby(LocalDateTime localDateTime, long lastChange) throws SQLException{
		stmt.executeUpdate("INSERT INTO lobby VALUES(NULL,'"+localDateTime.toString()+"',"+lastChange+",NULL, NULL, NULL");
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		return rs.getInt(1);

	}
	/**
	 * Methode zum setzen des Leaders der Lobby
	 * @param lobbyId
	 * @param leaderId
	 * @throws SQLException
	 */
	public static void updateLobbyLeader(int lobbyId, int leaderId) throws SQLException{
		stmt.executeUpdate("UPDATE lobby SET leader_id = "+leaderId+") WHERE lobby_id="+lobbyId);
	};
	/**
	 * Methode zum setzen des Spielers, der aktuell dran ist
	 * @param lobbyId
	 * @param playerTurnId
	 * @throws SQLException
	 */
	public static void updatePlayerTurn(int lobbyId, int PlayerTurnId) throws SQLException{
		stmt.executeUpdate("UPDATE lobby SET leader_id = "+PlayerTurnId+") WHERE lobby_id="+lobbyId);
	};
	/**
	 * Methode zum setzen der reihenfolge der Spieler
	 * @param lobbyId
	 * @param PlayerOrder
	 * @throws SQLException
	 */
	public static void updatePlayerOrder(int lobbyId, String PlayerOrder) throws SQLException{
		stmt.executeUpdate("UPDATE lobby SET leader_id = "+PlayerOrder+") WHERE lobby_id="+lobbyId);
	};
	
	
}
