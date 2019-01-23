package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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
	// "jdbc:mysql://localhost/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","123456"
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
	
	/**
	 * Diese Methode, welche ein Player-Objekt benötigt, der als zukünftiger Host einer Lobby fungiert,
	 * erstellt einen Lobby-Datensatz, an dessen LeaderId-Spalte die Id des Spielers a.k.a. Host eingetragen wird. 
	 * Des Weiteren wird die Methode joinLobby() mit demselben Player-Objekt aufgerufen.
	 * @param player = Der Spieler als Objekt Player.
	 * @throws SQLException = Eine Datenbank-Exception, die bei einem Fehler in der Kommunikation mit der Datenbank auftritt.
	 * @throws ClassNotFoundException = Falls eine benötigte Klasse im Zusammenhang mit dem Datenbankaustausch auftritt.
	 * @see joinLobby (Player player, int lobbyId)
	 * @author Jona Petrikowski
	 * @author Jörg Römmich
	 */
	public static void createLobby (Player player) throws SQLException, ClassNotFoundException {
		stmt = getStatement();
		String queryCreateLobbyEntry = String.format("INSERT INTO lobby (leader_id) VALUES (%d);", player.getId());
		stmt.executeUpdate(queryCreateLobbyEntry);
		// zweites Resultset für die autoincremente LobbyId, um diese beim Leader einzutragen
		String queryGetLobbyId = String.format("SELECT lobby_id FROM lobby WHERE leader_id = %d;", player.getId());
		List<List<String>> listWithLobbyId = ResultSetManager.toList(stmt.executeQuery(queryGetLobbyId));
		if (listWithLobbyId.get(0).size() == 1) {
			int lobbyId = Integer.parseInt(listWithLobbyId.get(0).get(0));
			// ein createLobby() ist für den Leader ein joinLobby()
			joinLobby(player, lobbyId);
			System.out.println("createLobby() successfull.");
		}
		else {
			System.out.println("createLobby(). Problem with getting lobby_id from returned ResultSet.");
		}
	}
			
	/**
	 * Diese Methode, welche ein Player-Objekt und die LobbyId der zu joinenden Lobby benötigt,
	 * schreibt bei dem dazugehörigen Player-Datensatz in die Spalte LobbyId die Id der zu joinenden Lobby.
	 * @param player = Der Spieler als Objekt Player.
	 * @throws SQLException = Eine Datenbank-Exception, die bei einem Fehler in der Kommunikation mit der Datenbank auftritt.
	 * @throws ClassNotFoundException = Falls eine benötigte Klasse im Zusammenhang mit dem Datenbankaustausch auftritt.
	 * @author Jona Petrikowski 
	 * @author Jörg Römmich
	 */
	public static void joinLobby (Player player, int lobbyId) throws SQLException, ClassNotFoundException {
		String queryJoinLobby = String.format("UPDATE player SET lobby_id = %d WHERE player_id = %d;", lobbyId, player.getId());
		getStatement().executeUpdate(queryJoinLobby);
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
	
	public static List<List<String>> getChatHistory(long timestamp, int lid) throws SQLException {
		ResultSet r = stmt.executeQuery(String.format("SELECT p.name, c.timestamp, c.message FROM player p, chat c WHERE p.player_id = c.player_id AND c.lobby_id = %d AND c.timestamp > %d;", lid, timestamp));
		return ResultSetManager.toList(r);
	}
	
	public static void sendMessage(String message, int pid, int lid) throws SQLException
	{
		String sql = String.format("INSERT INTO chat(timestamp, message, player_id, lobby_id) VALUES(CURDATE()*1000000+CURTIME(), '%s', %d, %d);", message, pid, lid);
		stmt.executeUpdate(sql);
	}
	
	public static List<List<String>> getLobbies() throws SQLException
	{
		//TODO: WHERE player_6 IS NULL
		ResultSet r = stmt.executeQuery("SELECT * FROM lobby;");
		return ResultSetManager.toList(r);
	}
}
