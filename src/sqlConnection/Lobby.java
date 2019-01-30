package sqlConnection;

import java.util.ArrayList;

/**
 * @author Thuß, Frederik
 * @author pbs2h17ath
 */
public class Lobby {
    private final int MAX_PLAYER_COUNT;
    private ArrayList<Player> players;
    private int lobbyId;
    private int leaderId;

    public Lobby() {
        this.MAX_PLAYER_COUNT = 6;
        this.players = new ArrayList<>();
        this.lobbyId = SqlHelper.insertLobby();
    }

    public int getLeaderId()
	{
		return leaderId;
	}

	public void setLobbyLeader(Player player) {
        SqlHelper.updateLobbyLeader(lobbyId, player.getPlayerId());
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int playerId) {
        for (Player player : players) {
            if (player.getPlayerId() == playerId) {
                return  player;
            }
        }

        return null;
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
            System.out.println(player);
        }
    }

    /**
     * Entfernt den Spieler aus der Lobby und Datenbank
     * @param player spieler Objekt
     */
    public void removePlayer(Player player) {
        players.remove(player);
        SqlHelper.deletePlayer(player.getPlayerId(), lobbyId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nLobbyId=");
        sb.append(lobbyId);
        sb.append("\n");
        for (Player player : players) {
            if(player == null) {
                sb.append("Slot: Leer");
            } else {
                sb.append("Slot: ID=");
                sb.append(player.getPlayerId());
                sb.append(" Name=");
                sb.append(player.getName());
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public void changePlayerName(int slot_id, String newPlayerName) {
    	SqlHelper.updatePlayerName(players.get(slot_id).getPlayerId(), newPlayerName);
    }

	/**
	 * @return nächster freier Slot in der Spielerliste. Wenn kein Slot frei ist, -1.
	 */
	public int getNextSlotId()
	{
		// TODO Auto-generated method stub
		int nextSlot = players.size();
		if(nextSlot > 6) {
		    return -1;
        }
		return nextSlot;
	}
	
	/**
	 * Löscht alle Spieler aus der Lobby
	 */
	public void clearPlayers() {
		this.players.clear();
	}
}
