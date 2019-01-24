package sqlConnection;

import network.ResultSetManager;

import java.sql.*;
import java.time.LocalDateTime;
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
	
	// Private TestDb fÃ¼r home server
	// "jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17azz","pbs2h17azz","Bib12345"
	// "jdbc:mysql://localhost/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","123456"
	private static String[] loginStringArray =  {
			 "jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17azztest","pbs2h17azz","Bib12345"
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
			System.out.println("closeStatement");
			e.printStackTrace();
		}
		
	}

	/**
	 * Gibt ein Statement zurÃ¼ck
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

	@Deprecated
	public static Player[] getAllPlayersForLobby(int lobbyId) {
		Player[] playerArray = new Player[6];
		int index = 0;
		int[] playerIdArray = new int[6];
		String[] playerNameArray = new String[6];
		String colorValue = null;
		
		// 
		try {
			// Alle Spieler einer Lobby auswählen
			ResultSet rs = stmt.executeQuery("SELECT player_id, name FROM player WHERE lobby_id="+lobbyId+";");
			
			// Werte in Array speichern
			while(rs.next()) {
				// Name und ID auslesen
				playerIdArray[index] = rs.getInt("player_id");
				playerNameArray[index] = rs.getString("name");
				index++;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < 6; i++) {
			ResultSet rs2;
			try {
				rs2 = stmt.executeQuery("SELECT c.value FROM color c, color_player cp "
						+ "WHERE cp.player_id = "+playerIdArray[i]+" "
						+ "AND cp.color_id = c.color_id;");
				rs2.next();
				colorValue = rs2.getString("value");
				
				Player p = new Player(playerNameArray[i], lobbyId);
				playerArray[i] = p;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		}
		
		
		
		return playerArray;
	}
	
	public static String[] getAllColors() {
		String[] colorArray;
		String query = "SELECT value FROM color;";
		ArrayList<String> colorList = new ArrayList<>();

		try {
			ResultSet rs = getStatement().executeQuery(query);
			while(rs.next()) {
				colorList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("getAllColors");
			e.printStackTrace();
		}

		colorArray = new String[colorList.size()];
		colorList.toArray(colorArray);

		return colorArray;
	}
	
	public static int[] getAllLobbyId() {
		ArrayList<Integer> lobbyIdList = new ArrayList<Integer>();
		String query = "SELECT lobby_id FROM lobby;";

		try {
			ResultSet rs = getStatement().executeQuery(query);
			while(rs.next()) {
				lobbyIdList.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("getAllLobbyId");
			e.printStackTrace();
		}
		
		// Konvertiere Integer Liste in Integer Array
		return lobbyIdList.stream().mapToInt(Integer::intValue).toArray();
	}
	
	public static void clearTable(String tableName) {
		String query = String.format("TRUNCATE TABLE %s;",tableName);
		try { 
			stmt.executeUpdate(query);
		} catch(SQLException s) {
			System.out.println("clearTable");
			s.printStackTrace();
		}
	}
	
	public static int[] getAllCountryId() {
		String query = "SELECT country_id FROM country;";
		int[] countryIdArray = new int[42];
		int i = 0;

		try {
			ResultSet rs = getStatement().executeQuery(query);
			while(rs.next()) {
				countryIdArray[i] = rs.getInt(1);
				i++;
			}
		} catch (SQLException e) {
			System.out.println("getAllCountryId");
			e.printStackTrace();
		}

		return countryIdArray;
		
	}
	
	public static String getPlayerName(int playerId) {
		String query = String.format("SELECT name FROM player WHERE player_id = %d",playerId);
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			return rs.getString(1);
		} catch (SQLException e) {
			System.out.println("getPlayerName");
			e.printStackTrace();
		}

		return null;
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
	 * Diese Methode, welche ein Player-Objekt benï¿½tigt, der als zukï¿½nftiger Host einer Lobby fungiert,
	 * erstellt einen Lobby-Datensatz, an dessen LeaderId-Spalte die Id des Spielers a.k.a. Host eingetragen wird. 
	 * Des Weiteren wird die Methode joinLobby() mit demselben Player-Objekt aufgerufen.
	 * @param player = Der Spieler als Objekt Player.
	 * @throws SQLException = Eine Datenbank-Exception, die bei einem Fehler in der Kommunikation mit der Datenbank auftritt.
	 * @throws ClassNotFoundException = Falls eine benï¿½tigte Klasse im Zusammenhang mit dem Datenbankaustausch auftritt.
	 * @see SqlHelper#joinLobby (Player player, int lobbyId)
	 * @author Jona Petrikowski
	 * @author Jï¿½rg Rï¿½mmich
	 */
	public static void createLobby (Player player) throws SQLException {
		stmt = getStatement();
		String queryCreateLobbyEntry = String.format("INSERT INTO lobby (leader_id) VALUES (%d);", player.getPlayerId());
		stmt.executeUpdate(queryCreateLobbyEntry);
		// zweites Resultset fï¿½r die autoincremente LobbyId, um diese beim Leader einzutragen
		String queryGetLobbyId = String.format("SELECT lobby_id FROM lobby WHERE leader_id = %d;", player.getPlayerId());
		List<List<String>> listWithLobbyId = ResultSetManager.toList(stmt.executeQuery(queryGetLobbyId));
		if (listWithLobbyId.get(0).size() == 1) {
			int lobbyId = Integer.parseInt(listWithLobbyId.get(0).get(0));
			// ein createLobby() ist fï¿½r den Leader ein joinLobby()
			joinLobby(player, lobbyId);
			System.out.println("createLobby() successfull.");
		}
		else {
			System.out.println("createLobby(). Problem with getting lobby_id from returned ResultSet.");
		}
	}
			
	/**
	 * Diese Methode, welche ein Player-Objekt und die LobbyId der zu joinenden Lobby benï¿½tigt,
	 * schreibt bei dem dazugehï¿½rigen Player-Datensatz in die Spalte LobbyId die Id der zu joinenden Lobby.
	 * @param player = Der Spieler als Objekt Player.
	 * @throws SQLException = Eine Datenbank-Exception, die bei einem Fehler in der Kommunikation mit der Datenbank auftritt.
	 * @throws ClassNotFoundException = Falls eine benï¿½tigte Klasse im Zusammenhang mit dem Datenbankaustausch auftritt.
	 * @author Jona Petrikowski 
	 * @author Jï¿½rg Rï¿½mmich
	 */
	public static void joinLobby (Player player, int lobbyId) {
		String queryJoinLobby = String.format("UPDATE player SET lobby_id = %d WHERE player_id = %d;", lobbyId, player.getPlayerId());
		try {
			getStatement().executeUpdate(queryJoinLobby);
		} catch (SQLException e) {
			System.out.println("joinLobby");
			e.printStackTrace();
		}
	}
	
	public static String getContintentName(int continentId) {
		String qeury = String.format("SELECT name FROM continent where continent_id = %s",continentId);
		try {
			ResultSet rs = getStatement().executeQuery(qeury);
			rs.next();
			return rs.getString("name");
		} catch (SQLException e) {
			System.out.println("getContintentName");
			e.printStackTrace();
		}

		return null;

	}
	
	public static int getBonus(int continentId) {
		String qeury = String.format("SELECT bonus FROM continent where continent_id = %d",continentId);
		try {
			ResultSet rs = getStatement().executeQuery(qeury);
			rs.next();
			return rs.getInt("bonus");
		} catch (SQLException e) {
			System.out.println("getBonus");
			e.printStackTrace();
		}

		return -1;
	}
	
	public static int[] getContinentCountries(int continentId) {
		String qeury = String.format("SELECT country_id FROM country where continent_id = %d",continentId);
		List <Integer> countryIdList = new ArrayList<>();

		try {
			ResultSet rs = getStatement().executeQuery(qeury);
			while(rs.next()){
				countryIdList.add(rs.getInt("country_id"));
			}
		} catch (SQLException e) {
			System.out.println("getContinentCountries");
			e.printStackTrace();
		}
		
		return countryIdList.stream().mapToInt(Integer::intValue).toArray();
	}
  	
   	// TODO Rework
	// Spielername muss nicht unebdingt Unique sein!
	public static int getPlayerId(String name) {
		String query = String.format("SELECT player_id FROM player WHERE name = %s",name);
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("getPlayerId");
			e.printStackTrace();
		}

		return -1;
	}
	
	public static int getCardValue(int cardId) {
		String query = String.format("SELECT value FROM card WHERE card_id = %d",cardId);
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			return rs.getInt("card_id");
		} catch (SQLException e) {
			System.out.println("getCardValue");
			e.printStackTrace();
		}
		return -1;
  	}
  
	public static int getCardCountryId(int cardId) throws SQLException{
		ResultSet rs = getStatement().executeQuery("SELECT country_id FROM card WHERE card_id = "+cardId+";");
		
		rs.next();
		return rs.getInt("country_id");
	}
	
	public static void fillCardsPlayer() {
		
	}

	public static String getMissionDescription(int missionID) throws SQLException {
		ResultSet rs = getStatement().executeQuery("SELECT description FROM mission WHERE mission_id = "+missionID+";");
		rs.next();
		return rs.getString("description");
	}
	
	public static List<List<String>> getChatHistory(long timestamp, int lid) throws SQLException {
		ResultSet r = stmt.executeQuery(String.format("SELECT p.name, c.timestamp, c.message FROM player p, chat c WHERE p.player_id = c.player_id AND c.lobby_id = %d AND c.timestamp > %d;", lid, timestamp));
		// System.out.println("Call lÃ¤uft");
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
	
	
	/**
	 * Methode zum einfÃ¼gen von Daten in die Tabelle country_player
	 * @param lobbyId
	 * @param playerId
	 * @param countryId
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static void insertCountryOwner(int lobbyId, int playerId, int countryId) throws SQLException{
		stmt.executeUpdate("INSERT INTO country_player VALUES("+playerId+","+countryId+","+lobbyId+", 1)");
	}

	/**
	 * Methode zum Ã¤ndern des Besatzers eines Landes 
	 * @param lobbyId
	 * @param playerId
	 * @param countryId
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static void updateCountryOwner(int lobbyId, int playerId, int countryId)throws SQLException{
		stmt.executeUpdate("UPDATE country_player SET player_id = "+playerId+") WHERE country_id ="+countryId+" AND lobby_id="+lobbyId);
	}

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
	}

	/**
	 * Methode zum hinzufÃ¼gen von Player
	 * @param name
	 * @param lobbyId
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static int insertPlayer(String name, int lobbyId) {
		Statement stmt = SqlHelper.getStatement();
		int id = -1;
		try {
			stmt.executeUpdate("INSERT INTO player VALUES(NULL, '"+name+"', '127.0.0.1',"+lobbyId+")",Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("fillDatabase error");
			e.printStackTrace();
		}

		return id;
	}

	/**
	 * Methode zum initiellen hinzufügen der Lobby in die Datenbank
	 * einige felder bleiben hier vorerst Null, da die zugehörigen werte nicht vorhanden sein können.
	 * @param localDateTime
	 * @throws SQLException
	 * @author pbs2h17ath
	 * @return lobbyIdS
	 */
	public static int insertLobby(LocalDateTime localDateTime, long lastChange) {
		int id = -1;
		ResultSet rs = null;
		try {
			// RETURN_GENERATED_KEYS gibt die Id des Autoincrement zur�ck
			getStatement().executeUpdate("INSERT INTO lobby (last_change) VALUES (1)", Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("insertLobby Error");
			e.printStackTrace();
		}
		return id;
	}
	/**
	 * Methode zum setzen des Leaders der Lobby
	 * @param lobbyId
	 * @param leaderId
	 * @throws SQLException
	 */
	public static void updateLobbyLeader(int lobbyId, int leaderId){
		String query = String.format("UPDATE lobby SET leader_id = %d WHERE lobby_id = %d",leaderId, lobbyId);
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("updateLobbyLeader");
			e.printStackTrace();
		}
		
	}

	/**
	 * Methode zum setzen des Spielers, der aktuell dran ist
	 * @param lobbyId
	 * @param playerTurnId
	 * @throws SQLException
	 */
	public static void updatePlayerTurn(int lobbyId, int playerTurnId) throws SQLException{
		stmt.executeUpdate("UPDATE lobby SET leader_id = "+playerTurnId+") WHERE lobby_id="+lobbyId);
	}

	/**
	 * Methode zum setzen der reihenfolge der Spieler
	 * @param lobbyId
	 * @param PlayerOrder
	 * @throws SQLException
	 */
	public static void updatePlayerOrder(int lobbyId, String PlayerOrder) throws SQLException{
		stmt.executeUpdate("UPDATE lobby SET leader_id = "+PlayerOrder+") WHERE lobby_id="+lobbyId);
	}


}
