package sqlCreation;

import sqlConnection.Lobby;
import sqlConnection.Player;
import sqlConnection.SqlHelper;
import sqlConnection.UpdateListener;

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
        long lastChange = SqlHelper.getLastChange(lobbyId);

        System.out.println("LobbyID:"+lobbyId);
        System.out.println("LastChange:"+lastChange);

        UpdateListener updateListener = new UpdateListener(lobbyId);
        updateListener.setRunning(true);
        updateListener.start();

        while (true) {
            // Run forever
        }


        // Timed performance tests
//        if(useTimer) {
//            endTime = System.nanoTime();
//            System.out.printf("Zeit: %d Millisekunden",(endTime - startTime) /  1000000);
//        }
    }

}
