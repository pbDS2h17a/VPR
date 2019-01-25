package sqlCreation;

import sqlConnection.Lobby;
import sqlConnection.Player;

/**
 * @author pbs2h17awb
 * Testklasse für Performance optimierung und
 * implementierung neuer DB features
 */
public class SqlTester {

    public static void main(String[] args) {
        boolean useTimer = true;
        long endTime = 0;
        long startTime = 0;
        
        if(useTimer) {
            startTime = System.nanoTime();
        }

        Lobby lobby = new Lobby();
        int lobbyId = lobby.getLobbyId();
        System.out.println("LobbyID:"+lobbyId);
        Player p1 = new Player("Bob1", lobbyId);
        Player p2 = new Player("Bob2", lobbyId);
        Player p3 = new Player("Bob3", lobbyId);
        Player p4 = new Player("Bob4", lobbyId);
        Player p5 = new Player("Bob5", lobbyId);
        Player p6 = new Player("Bob6", lobbyId);

        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        lobby.addPlayer(p3);
        lobby.addPlayer(p4);
        lobby.addPlayer(p5);
        lobby.addPlayer(p6);
        Player[] players = lobby.getPlayers();
        
        System.out.println(players.length);
        
        for (Player p : players) {
        	if (p == null) {
        		System.out.println("null");
        	}
        }

        // Timed performance tests
        if(useTimer) {
            endTime = System.nanoTime();
            System.out.printf("Zeit: %d Millisekunden",(endTime - startTime) /  1000000);
        }
    }

}
