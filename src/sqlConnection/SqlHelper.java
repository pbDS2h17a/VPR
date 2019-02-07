package sqlConnection;

import network.ResultSetManager;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
	// Private TestDb für home server
	// "jdbc:mysql://mysqlpb.pb.bib.de/pbs2h17azz","pbs2h17azz","Bib12345"
	// "jdbc:mysql://localhost/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","12345"
	private static String[] loginStringArray =  {
			"jdbc:mysql://localhost/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","12345"
	};

	//###################################################################################################################
	// Verbindung aufbauen
	//###################################################################################################################
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
			System.out.println("Sql Error! Check ob die Verbindungsdaten korrekt sind!");
			e.printStackTrace();
		}
	}

	/**
	 * Schließt das Statement
	 */
	public static void closeStatement() {
		try {
			getStatement().close();
		} catch (SQLException e) {
			System.out.println("closeStatement");
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

	//###################################################################################################################
	// Datenbank getter
	// Lesen Werte aus der Datenbank
	//###################################################################################################################

	/**
	 * Mithilfe der LobbyId returnt diese Methode den Namen des Lobbyleaders
	 * (mit Unterstützung der getPlayerName(playerId : int)-Methode).
	 * @param lobbyId Identifikationsnummer einer Lobby
	 * @return String Lobbyleadername
	 * @author Jona Petrikowski
	 * @author Nick Kuhn
	 */
	public static String getLeaderName (int lobbyId) {
		String query = String.format("SELECT leader_id FROM lobby WHERE lobby_id = %d", lobbyId);
		String leaderName = "Dummy";
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			leaderName = SqlHelper.getPlayerName(rs.getInt(1));
			rs.close();
		} catch (SQLException e) {
			System.out.println("Error getAllColors");
			e.printStackTrace();
		}

		return leaderName;
	}

	/**
	 * Ließt die werte (Hex String) aller Farben aus der Datenbank aus
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
			rs.close();
		} catch (SQLException e) {
			System.out.println("Error getAllColors");
			e.printStackTrace();
		}

		colorArray = new String[colorList.size()];
		colorList.toArray(colorArray);

		return colorArray;
	}
	/**
	 * Ließt die Lobby IDs aller Lobbies die zurzeit in der Datenbank sind
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
			rs.close();
		} catch (SQLException e) {
			System.out.println("Error getAllLobbyId");
			e.printStackTrace();
		}

		// Entfernt überzählige Lobbies damit nur die letzen 9 angezeigt werden
		while(lobbyIdList.size() > 9) {
			lobbyIdList.remove(0);
		}

		// Konvertiere Integer Liste in Integer Array
		return lobbyIdList.stream().mapToInt(Integer::intValue).toArray();
	}

	public static String getPlayerName(int playerId) {
		String query = String.format("SELECT name FROM player WHERE player_id = %d",playerId);
		String playerName = null;
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			playerName = rs.getString(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println("Error getPlayerName");
			e.printStackTrace();
		}

		return playerName;
	}

	/**
	 * Gibt den Names des Lands zurück
	 * @param countryId country_id
	 * @return Names des Lands
	 */
	public static String getCountryName (int countryId) {
		String query = String.format("SELECT name FROM country WHERE country_id = %d",countryId);
		String countryName = null;
		try{
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			countryName = rs.getString(1);
			rs.close();
		}catch(Exception e){
			System.out.println("Fehler beim holen des Ländernamens");
			e.printStackTrace();
		}
		return countryName;
	}
	
	/**
	 * Methode zum Auslesen der Einheiten, die in einem Land Stationiert sind.
	 * @param countryId
	 * @param lobbyId
	 * @return
	 */
	public static int getCountryUnits (int countryId, int lobbyId) {
		String query = String.format("SELECT unit_count FROM country_player WHERE country_id = %d  AND lobby_id= %d",countryId, lobbyId);
		int unitCount = -1;
		try{
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			unitCount = rs.getInt(1);
			rs.close();
		}catch(Exception e){
			System.out.println("Fehler beim holen der Einheiten im Land");
			e.printStackTrace();
		}
		return unitCount;
	}

	public static ArrayList<Country> getPlayerCountries(int playerId, int lobbyId) {
		String query = String.format("SELECT country_id FROM country_player WHERE lobby_id = %d AND player_id = %d", lobbyId, playerId);
		ArrayList<Integer> countryIdList = new ArrayList<>();
		ArrayList<Country> countryList = new ArrayList<>();
		try{
			ResultSet rs = getStatement().executeQuery(query);
			// Länder dürfen nicht in WHILE block erstellt werden
			// Da sie ihre Werte aus der DB Ziehen
			// ein weiterer BD Zugriff in einem offenem DB Zugriff ist nicht möglich
			while(rs.next()) {
				countryIdList.add(rs.getInt(1));
			}
			rs.close();
		}catch(Exception e){
			System.out.println("Fehler beim holen der Einheiten im Land");
			e.printStackTrace();
		}

		// Länder werden basierend auf IDs erstellt
		for (int id : countryIdList) {
			countryList.add(new Country(id));
		}

		return countryList;
	}

	public static String getColorValueFromPlayer(int playerId, int lobbyId) {
		String colorValue = null;
		String queryColor = String.format("SELECT c.value FROM color_player cp JOIN color c ON cp.color_id = c.color_id" +
				" WHERE player_id = %d AND lobby_id = %d;", playerId, lobbyId );

		try{
			ResultSet rs = getStatement().executeQuery(queryColor);
			rs.next();
			colorValue = rs.getString(1);
			rs.close();
		}catch(Exception e){
			System.out.println("Fehler beim holen der Farbe");
//			e.printStackTrace();
		}

		return colorValue;
	}


	/**
	 * Holt Namen und Farbe eines Spielers in einer Lobby
	 * @param playerId
	 * @param lobby
	 * @return
	 */
	public static Player getLobbyPlayerFromId(int playerId, Lobby lobby) {
		String playerName = null;
		String colorValue = null;
		int lobbyId = lobby.getLobbyId();

		String queryPlayer = String.format("SELECT name FROM player WHERE player_id = %d AND lobby_id = %d",
				playerId, lobbyId);

		try{
			ResultSet rs = getStatement().executeQuery(queryPlayer);
			rs.next();
			playerName = rs.getString(1);
			rs.close();
		}catch(Exception e){
			System.out.println("Fehler beim holen des Namen bzw LobbyId des Spielers");
			e.printStackTrace();
		}

		colorValue = getColorValueFromPlayer(playerId, lobbyId);



		return new Player(playerId, playerName, lobby, colorValue);
	}

//	public static Player getPlayerFromId(int playerId, Lobby lobby) {
////		int playerId, String name, Lobby lobby, String colorValue
//		String playerName = null;
//		String colorValue = null;
//		Country[] countryArray = null;
//		int lobbyId = -1;
//
//		String queryPlayer = String.format("SELECT name, lobby_id FROM player WHERE player_id = %d", playerId);
//		try{
//			ResultSet rs = getStatement().executeQuery(queryPlayer);
//			rs.next();
//			playerName = rs.getString(1);
//			lobbyId = rs.getInt(2);
//
//			rs.close();
//		}catch(Exception e){
//			System.out.println("Fehler beim holen des Namen bzw LobbyId des Spielers");
//			e.printStackTrace();
//		}
//
//
//		String queryColor = String.format("SELECT c.value FROM color_player cp JOIN color c ON cp.color_id = c.color_id" +
//				" WHERE player_id = %d AND lobby_id = %d;",playerId, lobbyId );
//
//		try{
//			ResultSet rs = getStatement().executeQuery(queryColor);
//			rs.next();
//			colorValue = rs.getString(1);
//			rs.close();
//		}catch(Exception e){
////			System.out.println("Fehler beim holen der Farbe");
////			e.printStackTrace();
//		}
//
//		return new Player(playerId, playerName, lobby, colorValue, getPlayerCountries(playerId,lobbyId));
//
//	}

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
			rs.close();
			for(Player p : lobby.getPlayers()){
				if(p.getPlayerId()==playerId){
					return p;
				}
			}
		}catch(Exception e){
			System.out.println("Fehler beim holen des Besitzers des Landes");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Holt Spielerdaten einer Lobby
	 * @param lobbyId
	 * @return
	 */
	public static Lobby getLobby (int lobbyId) {
		Lobby lobby = null;
		String queryLeader = String.format("SELECT leader_id FROM lobby WHERE lobby_id = %d",lobbyId);
		
		try{
			ResultSet rs = getStatement().executeQuery(queryLeader);
			rs.next();
			int leaderId = rs.getInt(1);
			rs.close();
			
			lobby = new Lobby(null, lobbyId, leaderId);
			
			for (int playerId : SqlHelper.getPlayerIdsFromLobby(lobbyId)) {
				Player tmpPlayer = getLobbyPlayerFromId(playerId, lobby);
				System.out.println(tmpPlayer.toString());
				lobby.addPlayer(tmpPlayer);
			}

		} catch(Exception e){
			//System.out.println("Fehler beim holen des Besitzers des Landes");
			//e.printStackTrace();
		}
		return lobby;
	}

	/**
	 * Methode zum Auslesen der ContinentId eines Landes
	 * @param countryId
	 * @return
	 */
	public static int getCountryContinentId(int countryId){
		String query = String.format("SELECT continent_id FROM country WHERE country_id = %d ",countryId);
		int continentId = -1;
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			continentId = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println("Error getCountryContinentId");
			e.printStackTrace();
		}
		return continentId;
	}

	/**
	 * Gib ein Array mit den Ids der Nachbarländer
	 * @param countryId country_id
	 * @return NachbarländerId array
	 */
	public static int[] getCountryNeighbor(int countryId) {
		String query = String.format("SELECT neighbor_id FROM neighbor WHERE country_id = %d ",countryId);
		ArrayList<Integer> neighborIdList = new ArrayList<>();

		try {
			ResultSet rs = getStatement().executeQuery(query);
			while(rs.next()) {
				neighborIdList.add(rs.getInt(1));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("Error getCountryNeighbor");
			e.printStackTrace();
		}
		
		// Konvertiere Integer Liste in Integer Array und return es direkt
		return neighborIdList.stream().mapToInt(Integer::intValue).toArray();
	}

	/**
	 * SVG String eines Landes
	 * @param countryId country_id
	 * @return SVG String
	 */
	public static String getCountrySVG(int countryId) {
		String query = String.format("SELECT svg FROM country WHERE country_id = %d ",countryId);
		String svg = null;
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			svg =  rs.getString(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println("error getCountrySVG");
			e.printStackTrace();
		}
		return svg;
	}

	/**
	 * Methode zum auslesen der lastChange in Lobby
	 * @param lobbyId lobby_id
	 * @return
	 */
	public static int getLastChange(int lobbyId){
		String query = String.format("SELECT last_change FROM lobby WHERE lobby_id = %d", lobbyId);
		int lastChange = -1;
		
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			lastChange = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println("Fehler beim lesen des LastChange");
//			e.printStackTrace();
		}

		return lastChange;
	}

	/**
	 * Gibt den Namen des Kontinent
	 * @param continentId continent_id
	 * @return Kontinentname
	 */
	public static String getContintentName(int continentId) {
		String query = String.format("SELECT name FROM continent where continent_id = %s",continentId);
		String continentName = null;
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			continentName = rs.getString("name");
			rs.close();
		} catch (SQLException e) {
			System.out.println("getContintentName");
			e.printStackTrace();
		}

		return continentName;

	}

	/**
	 * Bonus punkte die beim besitzt eines Kontinent vergeben werden
	 * @param continentId continent_id
	 * @return Bonuseinheiten
	 */
	public static int getBonus(int continentId) {
		String query = String.format("SELECT bonus FROM continent where continent_id = %d",continentId);
		int bonus = -1;
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			bonus = rs.getInt("bonus");
			rs.close();
		} catch (SQLException e) {
			System.out.println("getBonus");
			e.printStackTrace();
		}

		return bonus;
	}

	/**
	 * Erhählt eine KontinentId und gibt die Id der Länder in dem Kontinent zurück
	 * @param continentId continent_id
	 * @return countryId der Länder in dem Kontinent
	 */
	public static int[] getContinentCountryIds(int continentId) {
		String query = String.format("SELECT country_id FROM country where continent_id = %d",continentId);
		List <Integer> countryIdList = new ArrayList<>();

		try {
			ResultSet rs = getStatement().executeQuery(query);
			while(rs.next()){
				countryIdList.add(rs.getInt("country_id"));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("getContinentCountryIds");
			e.printStackTrace();
		}
		
		return countryIdList.stream().mapToInt(Integer::intValue).toArray();
	}

	/**
	 * Erhählt eine KartenId und gibt den Wert der Karte (1-3) zurück
	 * @param cardId card_id
	 * @return Einheit der Karte (Wert von 1 bis 3)
	 */
	public static int getCardValue(int cardId) {
		String query = String.format("SELECT value FROM card WHERE card_id = %d",cardId);
		int cardValue = -1;
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			cardValue = rs.getInt("card_id");
			rs.close();
		} catch (SQLException e) {
			System.out.println("getCardValue");
			e.printStackTrace();
		}
		return cardValue;
  	}

	/**
	 *
	 * @param missionId
	 * @return
	 */
	public static String getMissionDescription(int missionId){
		String query = String.format("SELECT description FROM mission WHERE mission_id =  %d",missionId);
		String description = null;
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			description = rs.getString("description");
			rs.close();
		} catch (SQLException e) {
			System.out.println("Fehler beim getMissionDescription");
			e.printStackTrace();
		}
		return description;
	}


	/**
	 * @param timestamp Zeitpunkt, nach dem Nachrichten angezeigt werden sollen
	 * @param lobbyId ID der Lobby, aus der die Nachrichten gelesen werden
	 * @return List<List<String>> Alle gültigen Nachrichten der Chat-History
	 * @author Schaumloeffel
	 */
	public static List<List<String>> getChatHistory(long timestamp, int lobbyId){
		String query = String.format("SELECT p.name, c.timestamp, c.message FROM player p, chat c "
				+ "WHERE p.player_id = c.player_id AND c.lobby_id = %d AND c.timestamp > %d;", lobbyId, timestamp);
		List<List<String>> history = null;
		try {
			ResultSet rs = getStatement().executeQuery(query);
			history = ResultSetManager.toList(rs);
			rs.close();
		} catch (SQLException e) {
			System.out.println("Fehler in der Chat History");
			e.printStackTrace();
		}
		return history;
	}

	/**
	 * Gibt ein Array mit den Ids aller Spieler in einer Lobby
	 * Array wird default mit 0 gefüllt
	 * @param lobbyId lobby_id
	 * @return 6 Stelliges int[] mit Spieler Ids
	 */
	public static ArrayList<Integer> getPlayerIdsFromLobby(int lobbyId) {
		String query = String.format("SELECT player_id FROM player WHERE lobby_id = %d", lobbyId);
		ArrayList<Integer> playerIdList = new ArrayList<>();

		try {
			ResultSet rs = getStatement().executeQuery(query);
			while(rs.next()) {

				playerIdList.add(rs.getInt(1));
			}

			rs.close();

		} catch (SQLException e) {
			System.out.println("Fehler beim lesen der SpielerIDs in einer Lobby");
			e.printStackTrace();
		}

		return playerIdList;
	}

	public static int getColorId(String colorValue) {
		System.out.println("Farbwert:"+colorValue);
		String query = String.format("SELECT color_id FROM color WHERE value = '%s'", colorValue);
		int colorId = -1;
		try {
			ResultSet rs = getStatement().executeQuery(query);
			rs.next();
			colorId = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println("Fehler beim auslesen der FarbId");
			//e.printStackTrace();
		}

		return colorId;
	}

	//###################################################################################################################
	// Datenbank inserter
	// Schreiben Werte in die Datenbank
	//###################################################################################################################

	/**
	 * Methode zum hinzufügen von Player
	 * @param name
	 * @param lobbyId
	 * @author pbs2h17ath
	 */
	public static int insertPlayer(String name, int lobbyId) {
		String query = String.format("INSERT INTO player (name, lobby_id) VALUES('%s', %d);", name, lobbyId);
		int id = -1;
		try {
			SqlHelper.getStatement().executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = SqlHelper.getStatement().getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
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
	 * @author pbs2h17ath
	 * @return lobbyId
	 */
	public static int insertLobby() {
		int id = -1;

		try {
			// RETURN_GENERATED_KEYS gibt die Id des Autoincrement zurück
			getStatement().executeUpdate("INSERT INTO lobby (last_change) VALUES (1)", Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = getStatement().getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println("insertLobby Error");
			e.printStackTrace();
		}
		return id;
	}

    public static void updateColor(int playerId, String colorValue, int lobbyId) throws SQLException {

        int colorId = getColorId(colorValue);

     	String delete = String.format("DELETE FROM color_player WHERE player_id = %d",playerId);
		// Farbe löschen
		try {
			getStatement().executeUpdate(delete);
		} catch (SQLException e) {
			System.out.println("Fehler beim löschen der Farbe");
		}

		// Versuche neue Farbe einzufügen
        String sql = String.format("INSERT INTO color_player VALUES(%d, %d, %d);", playerId, colorId, lobbyId);

		getStatement().executeUpdate(sql);
		updateLastChange(lobbyId);

    }

	/**
	 * @param message Inhalt der Nachricht
	 * @param player_id Eindeutige ID des Spielers, der die Nachricht absendet
	 * @param lobby_id Eindeutige ID der Lobby, in der sich der Spieler befindet. Nur Spieler dieser Lobby empfangen die Nachricht.
	 * @author pbs2h17asc
	 */
	public static void sendMessage(String message, int player_id, int lobby_id)
	{
		String sql = String.format("INSERT INTO chat(timestamp, message, player_id, lobby_id) VALUES(CURDATE()*1000000+CURTIME(), '%s', %d, %d);", message, player_id, lobby_id);
		try {
			getStatement().executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Fehler beim senden einer NAchricht");
			e.printStackTrace();
		}
	}

	/**
	 * Diese Methode, welche ein Player-Objekt benötigt, der als zukünftiger Host einer Lobby fungiert,
	 * erstellt einen Lobby-Datensatz, an dessen LeaderId-Spalte die Id des Spielers a.k.a. Host eingetragen wird.
	 * Des Weiteren wird die Methode joinLobby() mit demselben Player-Objekt aufgerufen.
	 * @param player = Der Spieler als Objekt Player.
	 * @see SqlHelper#joinLobby (Player player, int lobbyId)
	 * @author Jona Petrikowski
	 * @author Jörg Römmich
	 */
	public static void createLobby (Player player) {
		try{
			Statement stmt = getStatement();
			String queryCreateLobbyEntry = String.format("INSERT INTO lobby (leader_id) VALUES (%d);", player.getPlayerId());
			stmt.executeUpdate(queryCreateLobbyEntry);
			// zweites Resultset für die autoincremente LobbyId, um diese beim Leader einzutragen
			String queryGetLobbyId = String.format("SELECT lobby_id FROM lobby WHERE leader_id = %d;", player.getPlayerId());
			List<List<String>> listWithLobbyId = ResultSetManager.toList(getStatement().executeQuery(queryGetLobbyId));
			if (listWithLobbyId.get(0).size() == 1) {
				int lobbyId = Integer.parseInt(listWithLobbyId.get(0).get(0));
				// ein createLobby() ist für den Leader ein joinLobby()
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
	 * Methode zum einfügen von Daten in die Tabelle country_player
	 * @param lobbyId
	 * @param playerId
	 * @param countryId
	 * @author pbs2h17ath
	 */
	public static void insertCountryOwner(int lobbyId, int playerId, int countryId){
		String query = String.format("INSERT INTO country_player VALUES(%d, %d, %d, %d);", playerId, countryId, lobbyId, 1);
		try {
			getStatement().executeUpdate(query);
			updateLastChange(lobbyId);
		} catch (SQLException e) {
			System.out.println("Es ist ein Fehler beim einfügen in country_player aufgetreten");
			e.printStackTrace();
		}
	}

	//###################################################################################################################
	// Datenbank updater
	// Ändert Werte in der Datenbank
	//###################################################################################################################

	//TODO Implementieren
	public static void updateCardsPlayer() {

	}

	/**
	 * Wird von der Lobby aufgerufen wenn ein Spieler die Lobby verlässt
	 * Löscht den Spielerdatensatz vollständig
	 * @param playerId player_id
	 * @param lobbyId lobby_id
	 */
	public static void deletePlayer(int playerId, int lobbyId) {
		String queryJoinLobby = String.format("DELETE FROM player WHERE player_id = %d;",playerId);
		try {
			getStatement().executeUpdate(queryJoinLobby);
			updateLastChange(lobbyId);
		} catch (SQLException e) {
			System.out.println("Error deletePlayer");
			e.printStackTrace();
		}
	}

	/**
	 * Diese Methode, welche ein Player-Objekt und die LobbyId der zu joinenden Lobby benötigt,
	 * schreibt bei dem dazugehörigen Player-Datensatz in die Spalte LobbyId die Id der zu joinenden Lobby.
	 * @param player = Der Spieler als Objekt Player.
	 * @author Jörg Römmich
	 */
	public static void joinLobby (Player player, int lobbyId) {
		String queryJoinLobby = String.format("UPDATE player SET lobby_id = %d WHERE player_id = %d;", lobbyId, player.getPlayerId());
		try {
			getStatement().executeUpdate(queryJoinLobby);
		} catch (SQLException e) {
			System.out.println("Error joinLobby");
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum ändern des Besatzers eines Landes
	 * @param lobbyId
	 * @param playerId
	 * @param countryId
	 * @author pbs2h17ath
	 */
	public static void updateCountryOwner(int lobbyId, int playerId, int countryId){
		String query = String.format("UPDATE country_player SET player_id = %d  WHERE country_id = %d AND lobby_id= %d;", playerId, countryId, lobbyId);
		try {
			getStatement().executeUpdate(query);
			updateLastChange(lobbyId);
		} catch (SQLException e) {
			System.out.println("Fehler beim updaten vom country owner");
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum anpassen der Armeen anzahl
	 * @param lobbyId
	 * @param countryId
	 * @author pbs2h17ath
	 */
	public static void updateCountryUnits(int lobbyId, int countryId, int amountUnits){
		String query = String.format("UPDATE country_player SET unit_count = %d WHERE country_id = %d AND lobby_id= %d;", amountUnits, countryId, lobbyId);
		try {
			getStatement().executeUpdate(query);
			updateLastChange(lobbyId);
		} catch (SQLException e) {
			System.out.println("Fehler beim aktuallisieren der Units");
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum setzen des Leaders der Lobby
	 * @param lobbyId
	 * @param leaderId
	 */
	public static void updateLobbyLeader(int lobbyId, int leaderId){
		String query = String.format("UPDATE lobby SET leader_id = %d WHERE lobby_id = %d",leaderId, lobbyId);
		try {
			getStatement().executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Error updateLobbyLeader");
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
			System.out.println("Fehler beim ändern des Spielers, der 'dran' ist");
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
			System.out.println("Fehler beim Ändern der Spieler Reihenfolge");
			e.printStackTrace();
		}
	}

	/**
	 * Updated lastChange in der Lobby
	 * @param lobbyId lobby_id
	 */
	public static void updateLastChange(int lobbyId) {
		int currentLastChange = SqlHelper.getLastChange(lobbyId);
		String query = String.format("UPDATE lobby SET last_change = %d WHERE lobby_id = %d;",(currentLastChange + 1), lobbyId);
		try {
			getStatement().executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Fehler beim aktuallisieren des lastchange");
			e.printStackTrace();
		}
	}


	/**
	 * Gibt dem Spieler den angegebenen Namen
	 * @param player_id ID des Spielers
	 * @param newPlayerName Neuer Name des Spielers
	 * @author pbs2h17asc
	 */
	public static void updatePlayerName(int lobbyId, int player_id, String newPlayerName) {
		System.out.println("Name:"+player_id);
		String query = String.format("UPDATE player SET name = '%s' WHERE player_id = %d;", newPlayerName, player_id);
		try {
			getStatement().executeUpdate(query);
			updateLastChange(lobbyId);
		} catch (SQLException e) {
			System.out.printf("Fehler beim Schreiben des Spielernamens %s, ID %d ", newPlayerName, player_id);
			e.printStackTrace();
		}
	}

	/**
	 * Löscht die Inhalte einer Tabelle (Die Tabelle selbst wird nicht gelöscht!)
	 * @param tableName TABLE
	 */
	public static void clearTable(String tableName) {
		String query = String.format("TRUNCATE TABLE %s;",tableName);
		try {
			getStatement().executeUpdate(query);
		} catch(SQLException s) {
			System.out.println("Error clearTable");
			s.printStackTrace();
		}
	}

}
