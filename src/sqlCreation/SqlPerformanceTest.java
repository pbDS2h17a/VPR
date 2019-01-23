package sqlCreation;

import sqlConnection.Lobby;
import sqlConnection.Player;
import sqlConnection.SqlHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlPerformanceTest {

    public static void main(String[] args) throws SQLException, IOException {
        boolean useTimer = true;
        long endTime = 0;
        long startTime = 0;
        
        if(useTimer) {
            startTime = System.nanoTime();
        }
        
        Statement stmt = SqlHelper.getStatement();
        Lobby lobby = new Lobby();
        int lobbyId = lobby.getLobbyId();
        Player p1 = new Player("Bob1","FFD800", lobbyId);
        Player p2 = new Player("Bob2","C42B2B", lobbyId);
        Player p3 = new Player("Bob3","26BF00", lobbyId);
        Player p4 = new Player("Bob4","0066ED", lobbyId);
        Player p5 = new Player("Bob5","000000", lobbyId);
        Player p6 = new Player("Bob6","EF4CE7", lobbyId);

        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        lobby.addPlayer(p3);
        lobby.addPlayer(p4);
        lobby.addPlayer(p5);
        lobby.addPlayer(p6);

        System.out.println(lobby.getPlayerCount());

        Player[] players = lobby.getPlayers();

        for (Player p : players) {
            System.out.println(p.toString());
        }

        // Timed performance tests
        if(useTimer) {
            endTime = System.nanoTime();
            System.out.printf("Zeit: %d Millisekunden",(endTime - startTime) /  1000000);
        }
    }

}
