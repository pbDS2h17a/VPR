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
 * Bietet Methoden f�r den Chat an. Fungiert als Container f�r die SQLHelper-Klasse.
 * @author Erik Schauml�ffel
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
	
	
	public String formatMessage(List<String> message) {
		return String.format("%s [%s:%s] %s", message.get(0), message.get(1).substring(8,10), message.get(1).substring(10,12), message.get(2));
	}
	
	public void sendMessage(String message) throws SQLException {
		SqlHelper.sendMessage(message, player_id, lobby_id);
	}
	
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
