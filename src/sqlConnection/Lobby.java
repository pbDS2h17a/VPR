package sqlConnection;

import gui.LobbyFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

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

    public LobbyFX getLobbyFX() {
        return lobbyFX;
    }

    public void setLobbyFX(LobbyFX lobbyFX) {
        this.lobbyFX = lobbyFX;
    }

    private LobbyFX lobbyFX;

    public Lobby(LobbyFX lobbyFX) {
        this.MAX_PLAYER_COUNT = 6;
        this.players = new ArrayList<>();
        this.lobbyId = SqlHelper.insertLobby();
        this.lobbyFX = lobbyFX;
    }
    
    public Lobby(LobbyFX lobbyFX, int lobbyId, int leaderId) {
        this.MAX_PLAYER_COUNT = 6;
        this.players = new ArrayList<>();
        this.lobbyId = lobbyId;
        this.leaderId = leaderId;
        this.lobbyFX = lobbyFX;
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
            //System.out.println(player.toString());
        }
        //message Box falls Lobby voll
        else{
        	Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Lobby beitreten Fehler");
			alert.setHeaderText("Lobby ist voll!");
			alert.setContentText("Bitte suchen sie sich eine andere Lobby aus, oder Erstellen sie ein eigenens Spiel.");
			alert.showAndWait().ifPresent(rs -> {
			    if (rs == ButtonType.OK) {
			       alert.close();
			    }
			});
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
