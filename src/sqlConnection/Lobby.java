package sqlConnection;

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

}
