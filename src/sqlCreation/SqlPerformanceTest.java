package sqlCreation;

import sqlConnection.SqlHelper;

public class SqlPerformanceTest {

    public static void main(String[] args) {
        boolean useTimer = true;
        long endTime = 0;
        long startTime = 0;

        if(useTimer) {
            startTime = System.nanoTime();
        }

        String[] data = SqlHelper.getAllCountrySVG2();


        if(useTimer) {
            endTime = System.nanoTime();
            System.out.printf("Zeit: %d ",(endTime - startTime) /  1000000);
        }
    }

}
