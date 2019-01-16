package sqlCreation;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Statement;

import sqlConnection.Player;
import sqlConnection.SqlHelper;

public class SqlPerformanceTest {

    public static void main(String[] args) throws SQLException, IOException {
        boolean useTimer = true;
        long endTime = 0;
        long startTime = 0;
        String localIP = "";
        
        if(useTimer) {
            startTime = System.nanoTime();
        }

        
        FileReader.readFile("src\\resources\\stammdaten4.csv");
        
//      try {
//			// Eigene IP speichern
//			localIP = Inet4Address.getLocalHost().getHostAddress();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//        Statement stmt = SqlHelper.getStatement();
//        SqlQuery.disableForeignKeyConstraints();
//        
//        SqlHelper.clearTable("player");
//        SqlHelper.clearTable("lobby");
//       
//        stmt.executeUpdate("INSERT INTO player VALUES(1,'Testuser1','"+localIP+"',1, 1)");
//        stmt.executeUpdate("INSERT INTO player VALUES(2,'Testuser2','"+localIP+"',1, 2)");
//        stmt.executeUpdate("INSERT INTO player VALUES(3,'Testuser3','"+localIP+"',1, 3)");
//        stmt.executeUpdate("INSERT INTO player VALUES(4,'Testuser4','"+localIP+"',1, 4)");
//    	stmt.executeUpdate("INSERT INTO player VALUES(5,'Testuser5','"+localIP+"',1, 5)");
//        stmt.executeUpdate("INSERT INTO player VALUES(6,'Testuser6','"+localIP+"',1, 6)");
//        stmt.executeUpdate("INSERT INTO lobby VALUES(NULL,DEFAULT,NULL,1,1,1)");
//        SqlQuery.enableForeignKeyConstraints();
//        
//        Player[] data = SqlHelper.getAllPlayersForLobby(1);       
//        for(Player p : data) {
//        	System.out.println(p.getName());
//        }
        
        if(useTimer) {
            endTime = System.nanoTime();
            System.out.printf("Zeit: %d ",(endTime - startTime) /  1000000);
        }
    }

}
