package sqlCreation;

import sqlConnection.*;

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

//        UpdateListener updateListener = new UpdateListener(lobbyId);
//        updateListener.setRunning(true);
//        updateListener.start();

//        while (true) {
//            // Run forever
//        }


        // Timed performance tests
//        if(useTimer) {
//            endTime = System.nanoTime();
//            System.out.printf("Zeit: %d Millisekunden",(endTime - startTime) /  1000000);
//        }
    }

}
