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
        Player p1 = new Player("peter",lobby);
        Player p2 = new Player("heinz",lobby);
        Player p3 = new Player("dieter",lobby);

        System.out.println("Aktuelle LobbyId="+lobbyId);
        LobbyUpdateListener updateListener = new LobbyUpdateListener(lobby);

        // Thread starten
        updateListener.start();
        // Flag setzen
        updateListener.setRunning(true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lobby.removePlayer(p1);
        Player p4 = new Player("tom",lobby);

        // Timed performance tests
//        if(useTimer) {
//            endTime = System.nanoTime();
//            System.out.printf("Zeit: %d Millisekunden",(endTime - startTime) /  1000000);
//        }
    }

}
