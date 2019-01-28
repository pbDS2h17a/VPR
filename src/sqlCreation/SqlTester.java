package sqlCreation;

import sqlConnection.*;
import updateThread.LobbyJoinListener;

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
    public static void simulateJoiningPlayer(String name, int lobbyId) {
            SqlHelper.insertPlayer(name, lobbyId);
    }
}
