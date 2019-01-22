package sqlConnection;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author Thuﬂ, Frederik
 * @author pbs2h17ath
 */
public class Lobby {
	private int playerCount;
	private final int MAX_PLAYER_COUNT;
	private Player[] players;
	private int lobbyId;
	
	public Lobby() {
		this.playerCount = 0;
		this.MAX_PLAYER_COUNT = 6;
		this.players = new Player[MAX_PLAYER_COUNT];
		try {
			this.lobbyId = SqlHelper.insertLobby(LocalDateTime.now(), System.currentTimeMillis());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public int getLobbyId() {
		return lobbyId;
	}

	public void setLobbyId(int lobbyId) {
		this.lobbyId = lobbyId;
	}

	public int getMAX_PLAYER_COUNT() {
		return MAX_PLAYER_COUNT;
	}
	
	public void addPlayer(Player player) {
		if (playerCount < MAX_PLAYER_COUNT) {
			players[playerCount] = player;
			playerCount++;
		}
	}
	
}
