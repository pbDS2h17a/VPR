package sqlConnection;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author Thuﬂ, Frederik
 * @author pbs2h17ath
 */
public class Lobby {
	private final int MAX_PLAYER_COUNT;
	private ArrayList<Player> players;
	private int lobbyId;
	
	public Lobby() {
		this.MAX_PLAYER_COUNT = 6;
		this.players = new ArrayList<>();
		this.lobbyId = SqlHelper.insertLobby();
	}
	
	public void setLobbyLeader(int playerId) {
		SqlHelper.updateLobbyLeader(lobbyId, playerId);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public int getLobbyId() {
		return lobbyId;
	}

	public int getMAX_PLAYER_COUNT() {
		return MAX_PLAYER_COUNT;
	}
	
	public void addPlayer(Player player) {
		if (players.size() < MAX_PLAYER_COUNT) {
			players.add(player);
		}
	}

	public void removePlayer(Player player) {
		players.remove(player);
		SqlHelper.deletePlayer(player.getPlayerId(), lobbyId);
	}
	
}
