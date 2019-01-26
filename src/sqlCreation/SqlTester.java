package sqlCreation;

import sqlConnection.*;

import java.sql.SQLException;
import java.sql.Statement;

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

        System.out.println(p1.equals(p2));
//
//        LobbyJoinListener updateListener = new LobbyJoinListener(lobby);
//
//        System.out.println(lobby);
//
//        // Thread starten
//        updateListener.start();
//        // Flag setzen
//        updateListener.setRunning(true);
//
//        // Warte für eine sekunden
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        simulateJoiningPlayer("Neo", lobbyId);
//
//        // Warte für eine sekunden
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        simulateJoiningPlayer("Merpheus", lobbyId);
//
//        // Warte für eine sekunden
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        simulateJoiningPlayer("Trinity", lobbyId);
//
//        // Warte für eine sekunden
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        simulateJoiningPlayer("Cipher", lobbyId);
//
//        // Warte für eine sekunden
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        lobby.removePlayer(p1);
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        simulateJoiningPlayer("Cipher", lobbyId);

        //lobby.removePlayer(p1);
        //Player p4 = new Player("tom",lobby);

        // Timed performance tests
//        if(useTimer) {
//            endTime = System.nanoTime();
//            System.out.printf("Zeit: %d Millisekunden",(endTime - startTime) /  1000000);
//        }
    }

    /**
     * Simuliert das Beitreten eines Spielers von einem anderen System
     * @param lobbyId
     */
    private static void simulateJoiningPlayer(String name, int lobbyId) {
            SqlHelper.insertPlayer(name, lobbyId);
    }
}
