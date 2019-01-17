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

public class ChatManager {
	private int lobby_id;
	private int player_id;
	
	public ChatManager(int lobby_id, int player_id) {
		this.lobby_id = lobby_id;
		this.player_id = player_id;
		SqlHelper.getStatement();
	}
	
	public List<List<String>> getChatHistory(long timestamp) throws SQLException {
		return SqlHelper.getChatHistory(timestamp, lobby_id);
	}
	
	public String formatMessage(List<String> message) {
		return String.format("%s [%s:%s] %s", message.get(0), message.get(1).substring(8,10), message.get(1).substring(10,12), message.get(2));
	}
	
	public void sendMessage(String message, int pid, int lid) throws SQLException {
		SqlHelper.sendMessage(message, pid, lid);
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
