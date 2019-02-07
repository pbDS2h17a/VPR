package sqlCreation;

import sqlConnection.*;

import java.util.ArrayList;


/**
 * @author pbs2h17awb
 * Testklasse für Performance optimierung und
 * implementierung neuer DB features
 */
public class SqlTester {

    public static void main(String[] args) {
       ArrayList<Country> list = SqlHelper.getPlayerCountries(83, 28);
        System.out.println(list.size());
    }

    /**
     * Simuliert das Beitreten eines Spielers von einem anderen System
     * @param lobbyId
     */
    public static void simulateJoiningPlayer(String name, int lobbyId) {
            SqlHelper.insertPlayer(name, lobbyId);
    }
}
