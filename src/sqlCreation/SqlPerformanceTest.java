package sqlCreation;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Statement;

import sqlConnection.SqlHelper;

public class SqlPerformanceTest {

    public static void main(String[] args) throws SQLException {
        boolean useTimer = true;
        long endTime = 0;
        long startTime = 0;
        String localIP = "";
        
        if(useTimer) {
            startTime = System.nanoTime();
        }
        
        try {
			// Eigene IP speichern
			localIP = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
        
//        Statement stmt = SqlHelper.getStatement();
        
//        SqlQuery.disableForeignKeyConstraints();
//        stmt.executeUpdate("INSERT INTO player VALUES(1,'Testuser1','"+localIP+"',NULL,NULL)");
//        stmt.executeUpdate("INSERT INTO lobby VALUES(NULL,DEFAULT,NULL,1,1,1)");
//        SqlQuery.enableForeignKeyConstraints();
        
        if(useTimer) {
            endTime = System.nanoTime();
            System.out.printf("Zeit: %d ",(endTime - startTime) /  1000000);
        }
    }

}
