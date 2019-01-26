package sqlCreation;

import sqlConnection.*;

/**
 * @author pbs2h17awb
 * Testklasse für Performance optimierung und
 * implementierung neuer DB features
 */
public class SqlTester {

    public static void main(String[] args) {
//        boolean useTimer = true;
//        long endTime = 0;
//        long startTime = 0;
        
//        if(useTimer) {
//            startTime = System.nanoTime();
//        }

        Lobby lobby = new Lobby();
        int lobbyId = lobby.getLobbyId();

        System.out.println("LobbyID:"+lobbyId);

        lobby.addPlayer(new Player("Tom",lobby));

        LobbyUpdateListener updateListener = new LobbyUpdateListener(lobby);
        Player p1 = new Player("peter",lobby);
        Player p2 = new Player("heinz",lobby);
        // Thread starten
        updateListener.start();
        // Flag setzen
        updateListener.setRunning(true);

        lobby.addPlayer(p1);
        lobby.addPlayer(p2);

        lobby.removePlayer(p1);

        // Timed performance tests
//        if(useTimer) {
//            endTime = System.nanoTime();
//            System.out.printf("Zeit: %d Millisekunden",(endTime - startTime) /  1000000);
//        }
    }

}
