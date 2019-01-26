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

        Player[] players = lobby.getPlayers();

        for (Player p : players) {
            if(p != null) {
                System.out.println(p.toString());
            }
        }

        int[] ids = SqlHelper.getPlayerIdsFromLobby(lobbyId);

        for (int id : ids) {
            System.out.print("ID:"+id+", ");
        }

        System.out.println();

        // LobbyUpdateListener updateListener = new LobbyUpdateListener(lobby);

        // Thread starten
        // updateListener.start();
        // Flag setzen
        // updateListener.setRunning(true);

        lobby.addPlayer(new Player("peter",lobby));
        players = lobby.getPlayers();
        ids = SqlHelper.getPlayerIdsFromLobby(lobbyId);

        for (int id : ids) {
            System.out.print("ID:"+id+", ");
        }
        for (Player p : players) {
            if(p != null) {
                System.out.println(p.toString());
            }
        }
        System.out.println();

        lobby.addPlayer(new Player("thomas",lobby));
        players = lobby.getPlayers();
        ids = SqlHelper.getPlayerIdsFromLobby(lobbyId);

        for (int id : ids) {
            System.out.print("ID:"+id+", ");
        }
        System.out.println();
        for (Player p : players) {
            if(p != null) {
                System.out.println(p.toString());
            }
        }
        // Timed performance tests
//        if(useTimer) {
//            endTime = System.nanoTime();
//            System.out.printf("Zeit: %d Millisekunden",(endTime - startTime) /  1000000);
//        }
    }

}
