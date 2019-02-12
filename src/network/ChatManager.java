package network;

import java.time.LocalDateTime;
import java.util.List;

import sqlConnection.SqlHelper;

/**
 * Bietet Methoden für den Chat an. Fungiert als Container für die SQLHelper-Klasse.
 * @author Erik Schaumlöffel
 * @author PeRoScKu
 * @see sqlConnection.SqlHelper
 */
class ChatManager {
	private final int lobby_id;
	private final int player_id;
	
	/**
	 * Konstruktor
	 * @param lobby_id Lobby des chattenden Spielers
	 * @param player_id ID des chattenden Spielers
	 */
	public ChatManager(int lobby_id, int player_id) {
		this.lobby_id = lobby_id;
		this.player_id = player_id;
		SqlHelper.getStatement();
	}
	
	/**
	 * Chatverlauf aus der Datenbank abfragen
	 * @param timestamp Zeitstempel
	 * @return Chatverlauf ab dem gegebenen Zeitstempel
	 */
	public List<List<String>> getChatHistory(long timestamp) {
		return SqlHelper.getChatHistory(timestamp, lobby_id);
	}
	
	/**
	 * Formatiert die Nachricht für das Chatfenster.
	 * @param message zu formatierende Nachricht
	 * @return Formatierte Nachricht
	 */
	public String formatMessage(List<String> message) {
		return String.format("%s [%s:%s] %s", message.get(0), message.get(1).substring(8,10), message.get(1).substring(10,12), message.get(2));
	}
	
	/**
	 * Sendet die Nachricht an die Datenbank
	 * @param message zu sendende Nachricht
	 */
	public void sendMessage(String message) {
		SqlHelper.sendMessage(message, player_id, lobby_id);
	}
	
	/**
	 * Zeitstempel holen
	 * @return Aktueller Zeitstempel
	 */
	public long getTimestamp() {
		LocalDateTime dt = LocalDateTime.now();
		long timestamp = 0;
		timestamp += dt.getSecond();
		timestamp += dt.getMinute()*100;
		timestamp += dt.getHour()*10000;
		timestamp += dt.getDayOfMonth()*1000000;
		timestamp += dt.getMonthValue()*100000000;
		timestamp += (long)dt.getYear()*100000*100000;
		return timestamp;
	}
}
