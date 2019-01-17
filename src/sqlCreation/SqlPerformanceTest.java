package sqlCreation;

import java.io.IOException;
import java.sql.SQLException;

public class SqlPerformanceTest {

    public static void main(String[] args) throws SQLException, IOException {
        boolean useTimer = true;
        long endTime = 0;
        long startTime = 0;
        
        if(useTimer) {
            startTime = System.nanoTime();
        }

        // Timed performance tests
        
        
        if(useTimer) {
            endTime = System.nanoTime();
            System.out.printf("Zeit: %d Millisekunden",(endTime - startTime) /  1000000);
        }
    }

}
