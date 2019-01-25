package sqlConnection;

import network.ResultSetManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlHelper {
	
	private static Statement stmt = null;

    /*
	 * String Array der Logindaten der Datenbank
	 * 0=Addresse
	 * 1=Datenbankname
	 * 2=Password
	 * Erstellt ein Statement mit den Werten
	 */
	
	// Private TestDb f�r home server
	// "jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17azz","pbs2h17azz","Bib12345"
	// "jdbc:mysql://localhost/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","123456"
	private static String[] loginStringArray =  {
			"jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17azz","pbs2h17azz","Bib12345"
	};

	private static void updateLastChange(int lobbyId) {

	    long currentLastChange = SqlHelper.getLastChange(lobbyId);


        String query = String.format("UPDATE lobby SET last_change = %d;",currentLastChange++);
        try {
            getStatement().executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Fehler beim aktuallisieren des lastchange");
            e.printStackTrace();
        }
    }

	/**
	 * Versucht ein neues Statement zu erstellen
	 */
	private static void createStatement() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(loginStringArray[0], loginStringArray[1], loginStringArray[2]);
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
			getStatement().close();
		} catch (SQLException e) {
			System.out.println("closeStatement");
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

	/**
	 * Lie�t die werte (Hex String) aller Farben aus der Datenbank aus
	 * @return StringArray mit Hexwerten
	 */
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
	/**
	 * Lie�t die Lobby IDs aller Lobbies die zurzeit in der Datenbank sind
	 * @return
	 */
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
		
		while(lobbyIdList.size() > 9) {
			lobbyIdList.remove(0);
		}

		for (int id : lobbyIdList) {
			System.out.println(id);
		}

		// Konvertiere Integer Liste in Integer Array
		return lobbyIdList.stream().mapToInt(Integer::intValue).toArray();
	}
	
	public static void clearTable(String tableName) {
		String query = String.format("TRUNCATE TABLE %s;",tableName);
		try { 
			getStatement().executeUpdate(query);
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
	
	public static String getCountryName (int countryId) {
		String query = String.format("SELECT name FROM country WHERE country_id = %d",countryId);
		try{
			ResultSet rs = getStatement().executeQuery(query);
			rs.next(); 
			return rs.getString(1);	
		}catch(Exception e){
			System.out.println("Fehler beim holen des L�ndernamens");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Methode zum Auslesen der Einheiten, die in einem Land Stationiert sind.
	 * @param countryId
	 * @param lobbyId
	 * @return
	 */
	public static int getCountryUnits (int countryId, int lobbyId) {
		String query = String.format("SELECT unit_count FROM country_player WHERE country_id = %d  AND lobby_id= %d",countryId, lobbyId);

		try{
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			return rs.getInt(1);
		}catch(Exception e){
			System.out.println("Fehler beim holen der Einheiten im Land");
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * Methode zum Auslesen des Besatzers eines Landes
	 * @param countryId
	 * @param lobby
	 * @return
	 */
	public static Player getCountyOwner (int countryId, Lobby lobby) {
		String query = String.format("SELECT player_id FROM country_player WHERE country_id = %d  AND lobby_id= %d",countryId, lobby.getLobbyId());
		try{
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			int playerId = rs.getInt(1);
			for(Player p : lobby.getPlayers()){
				if(p.getPlayerId()==playerId){
					return p;
				}
			}
		}catch(Exception e){
			System.out.println("Fehler beim holen des Besatzers des Landes");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Methode zum Auslesen der ContinentId eines Landes
	 * @param countryId
	 * @return
	 */
	public static int getCountryContinentId(int countryId){
		String query = String.format("SELECT continent_id FROM country WHERE country_id = %d ",countryId);
		try {
			ResultSet rs;
			rs = getStatement().executeQuery(query);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public static int[] getCountryNeighbor(int countryId) {
		String query = String.format("SELECT neighbor_id FROM neighbor WHERE country_id = %d ",countryId);
		ResultSet rs = null;
		try {
			rs = getStatement().executeQuery(query);
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

	public static String getCountrySVG(int countryId) {
		String query = String.format("SELECT svg FROM country WHERE country_id = %d ",countryId);
		ResultSet rs;
		try {
			rs = getStatement().executeQuery(query);
			rs.next();
			return  rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	 * Diese Methode, welche ein Player-Objekt ben�tigt, der als zukï¿½nftiger Host einer Lobby fungiert,
	 * erstellt einen Lobby-Datensatz, an dessen LeaderId-Spalte die Id des Spielers a.k.a. Host eingetragen wird. 
	 * Des Weiteren wird die Methode joinLobby() mit demselben Player-Objekt aufgerufen.
	 * @param player = Der Spieler als Objekt Player.
	 * @see SqlHelper#joinLobby (Player player, int lobbyId)
	 * @author Jona Petrikowski
	 * @author J�rg R�mmich
	 */
	public static void createLobby (Player player) {
		try{
			Statement stmt = getStatement();
			String queryCreateLobbyEntry = String.format("INSERT INTO lobby (leader_id) VALUES (%d);", player.getPlayerId());
			stmt.executeUpdate(queryCreateLobbyEntry);
			// zweites Resultset fï¿½r die autoincremente LobbyId, um diese beim Leader einzutragen
			String queryGetLobbyId = String.format("SELECT lobby_id FROM lobby WHERE leader_id = %d;", player.getPlayerId());
			List<List<String>> listWithLobbyId = ResultSetManager.toList(getStatement().executeQuery(queryGetLobbyId));
			if (listWithLobbyId.get(0).size() == 1) {
				int lobbyId = Integer.parseInt(listWithLobbyId.get(0).get(0));
				// ein createLobby() ist fï¿½r den Leader ein joinLobby()
				joinLobby(player, lobbyId);
				System.out.println("createLobby() successfull.");
			}
			else {
				System.out.println("createLobby(). Problem with getting lobby_id from returned ResultSet.");
			}
		}catch(Exception e){
			System.out.println("fehler beim erstellen der Lobby");
			e.printStackTrace();
		}
	}
			
	/**
	 * Diese Methode, welche ein Player-Objekt und die LobbyId der zu joinenden Lobby benï¿½tigt,
	 * schreibt bei dem dazugehï¿½rigen Player-Datensatz in die Spalte LobbyId die Id der zu joinenden Lobby.
	 * @param player = Der Spieler als Objekt Player.
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
		String query = String.format("SELECT name FROM continent where continent_id = %s",continentId);
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			return rs.getString("name");
		} catch (SQLException e) {
			System.out.println("getContintentName");
			e.printStackTrace();
		}

		return null;

	}
	
	public static int getBonus(int continentId) {
		String query = String.format("SELECT bonus FROM continent where continent_id = %d",continentId);
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			return rs.getInt("bonus");
		} catch (SQLException e) {
			System.out.println("getBonus");
			e.printStackTrace();
		}

		return -1;
	}
	
	public static int[] getContinentCountries(int continentId) {
		String query = String.format("SELECT country_id FROM country where continent_id = %d",continentId);
		List <Integer> countryIdList = new ArrayList<>();

		try {
			ResultSet rs = getStatement().executeQuery(query);
			while(rs.next()){
				countryIdList.add(rs.getInt("country_id"));
			}
		} catch (SQLException e) {
			System.out.println("getContinentCountries");
			e.printStackTrace();
		}
		
		return countryIdList.stream().mapToInt(Integer::intValue).toArray();
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
  
	public static int getCardCountryId(int cardId){
		String query = String.format("SELECT country_id FROM card WHERE card_id = %d",cardId);
		try {
            ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			return rs.getInt("country_id");
		} catch (SQLException e) {
			System.out.println("Fehler in getCardCountryId");
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void fillCardsPlayer() {
		
	}

	public static String getMissionDescription(int missionID){
		String query = String.format("SELECT description FROM mission WHERE mission_id =  %d",missionID);
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			return rs.getString("description");
		} catch (SQLException e) {
			System.out.println("Fehler beim getMissionDescription");
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<List<String>> getChatHistory(long timestamp, int lobbyId){
		String query = String.format("SELECT p.name, c.timestamp, c.message FROM player p, chat c "
				+ "WHERE p.player_id = c.player_id AND c.lobby_id = %d AND c.timestamp > %d;", lobbyId, timestamp);
		try {
			ResultSet r = getStatement().executeQuery(query);
			// System.out.println("Call lÃ¤uft");
			return ResultSetManager.toList(r);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Fehler in der Chat History");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void sendMessage(String message, int pid, int lid)
	{
		String sql = String.format("INSERT INTO chat(timestamp, message, player_id, lobby_id) VALUES(CURDATE()*1000000+CURTIME(), '%s', %d, %d);", message, pid, lid);
		try {
			getStatement().executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Fehler beim senden einer NAchricht");
			e.printStackTrace();
		}
	}
	
	public static List<List<String>> getLobbies()
	{
		//TODO: WHERE player_6 IS NULL
		ResultSet r;
		try {
			r = getStatement().executeQuery("SELECT * FROM lobby;");
			return ResultSetManager.toList(r);
		} catch (SQLException e) {
			System.out.println("getLobbies Error");
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Methode zum einfÃ¼gen von Daten in die Tabelle country_player
	 * @param lobbyId
	 * @param playerId
	 * @param countryId
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static void insertCountryOwner(int lobbyId, int playerId, int countryId){
		String query = String.format("INSERT INTO country_player VALUES(%d, %d, %d, %d);", playerId, countryId, lobbyId, 1);
		try {
			getStatement().executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Es ist ein Fehler beim einf�gen in country_player aufgetreten");
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum Ã¤ndern des Besatzers eines Landes 
	 * @param lobbyId
	 * @param playerId
	 * @param countryId
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static void updateCountryOwner(int lobbyId, int playerId, int countryId){
		String query = String.format("UPDATE country_player SET player_id = %d  WHERE country_id = %d AND lobby_id= %d;", playerId, countryId, lobbyId);
		try {
			getStatement().executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Fehler beim updaten vom country owner");
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum anpassen der Armeen anzahl
	 * @param lobbyId
	 * @param countryId
	 * @param amountUnits
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static void updateUnits(int lobbyId, int countryId, int amountUnits){
		String query = String.format("UPDATE country_player SET unit_count = %d WHERE country_id = %d AND lobby_id= %d;", amountUnits, countryId, lobbyId);
		try {
			getStatement().executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Fehler beim aktuallisieren der Units");
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum hinzufÃ¼gen von Player
	 * @param name
	 * @param lobbyId
	 * @throws SQLException
	 * @author pbs2h17ath
	 */
	public static int insertPlayer(String name, int lobbyId) {
		String query = String.format("INSERT INTO player VALUES(NULL, '%s' ,'127.0.0.1', %d);", name, lobbyId);
		Statement stmt = SqlHelper.getStatement();
		int id = -1;
		try {
			stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			updateLastChange(lobbyId);
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
			rs = getStatement().getGeneratedKeys();
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
			getStatement().executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("updateLobbyLeader");
			e.printStackTrace();
		}	
	}

	/**
	 * Methode zum setzen des Spielers, der aktuell dran ist
	 * @param lobbyId
	 * @param playerTurnId
	 */
	public static void updatePlayerTurn(int lobbyId, int playerTurnId){
		String query = String.format("UPDATE lobby SET leader_id = %d WHERE lobby_id=%d",playerTurnId, lobbyId);
		try {
			getStatement().executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Fehler beim �ndern des Spielers, der 'dran' ist");
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum setzen der reihenfolge der Spieler
	 * @param lobbyId
	 * @param PlayerOrder
	 */
	public static void updatePlayerOrder(int lobbyId, String PlayerOrder){
		String query = String.format("UPDATE lobby SET player_order = %s WHERE lobby_id=%d",PlayerOrder, lobbyId);
		try {
			getStatement().executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Fehler beim �ndern der Spieler Reihenfolge");
			e.printStackTrace();
		}
	}


	public static int[] getPlayerIdsFromLobby(int lobbyId) {
        String query = String.format("SELECT player_id FROM player WHERE lobby_id = %d", lobbyId);
        int[] playerIdArray = new int[6];
        int i = 0;

        try {
            ResultSet rs = getStatement().executeQuery(query);
            while(rs.next()) {
               playerIdArray[i] = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim lesen der SpielerIDs in einer Lobby");
            e.printStackTrace();
        }

        return playerIdArray;
    }

    /**
     * Methode zum auslesen der lastChange in Lobby
     * @param lobbyId
     * @return
     */
    public static long getLastChange(int lobbyId){
        String query = String.format("SELECT last_change FROM lobby WHERE lobby_id = %d", lobbyId);
        long lastChange = -1;

        try {
            ResultSet rs = getStatement().executeQuery(query);
            rs.next();
            lastChange = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Fehler beim lesen des LastChange");
            e.printStackTrace();
        }

        return lastChange;
    }


}
