package network;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sqlConnection.SqlHelper;

public class ChatManager {
	private String ip;
	private int lobby_id;
	private int player_id;
	
	public ChatManager(String ip, int lobby_id, int player_id) {
		this.ip = ip;
		this.lobby_id = lobby_id;
		this.player_id = player_id;
	}

	public String getIp() {
		return ip;
	}
	
	public List<List<String>> getChatHistory(long timestamp) throws SQLException {
		return SqlHelper.getChatHistory(timestamp, lobby_id);
	}
	
	public String formatMessage(List<String> message) {
		return String.format("%s [%s]: %s", message.get(0), message.get(1).substring(0,5), message.get(2));
	}
	
	public void sendMessage(String message, int pid, int lid) throws SQLException {
		SqlHelper.sendMessage(message, pid, lid);
	}
	
	public long getTimestamp() {
		//Datet
		return 0;
	}
}
