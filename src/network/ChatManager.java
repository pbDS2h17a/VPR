package network;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.print.attribute.standard.DateTimeAtProcessing;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sqlConnection.SqlHelper;

/**
 * Bietet Methoden für den Chat an. Fungiert als Container für die SQLHelper-Klasse.
 * @author Erik Schaumlöffel
 * @author PeRoScKu
 * @see sqlConnection.SqlHelper
 */
public class ChatManager {
	private int lobby_id;
	private int player_id;
	
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
	 * @throws SQLException
	 */
	public List<List<String>> getChatHistory(long timestamp) throws SQLException {
		return SqlHelper.getChatHistory(timestamp, lobby_id);
	}
	
	/**
	 * Formatiert die Nachricht für das Chatfenster.
	 * @param message zu formatierende Nachricht
	 * @return Formatierte Nachricht
	 */
	public String formatMessage(List<String> message) {
		//(0) player_id
		//(1) lobby_id
		//(2) name
		//(3) timestamp
		//(4) message
		return String.format("%s [%s:%s] %s", message.get(2), message.get(3).substring(8,10), message.get(3).substring(10,12), message.get(4));
	}
	
	/**
	 * Liest die Player-ID aus der jeweiligen Nachricht
	 * @param message Chat-Nachricht
	 * @return player_id des Spielers, der die Nachricht abgeschickt hat
	 */
	public String getMessagePlayerColor(List<String> message) {
		try {
			return SqlHelper.getColorValueFromPlayer(Integer.parseInt(message.get(0)), Integer.parseInt(message.get(1)));
		}
		//Wenn der Spieler keine Farbe hat, wird null zurückgegeben
		catch(NullPointerException n) {
			return null;
		}
	}
	
	/**
	 * Sendet die Nachricht an die Datenbank
	 * @param message zu sendende Nachricht
	 * @throws SQLException
	 */
	public void sendMessage(String message) throws SQLException {
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
