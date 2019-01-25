package sqlConnection;

public class UpdateListener extends Thread {

    private int lobbyId;
    private long currentLastChange = 1;

    private boolean isRunning = false;

    public UpdateListener(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {

        while(isRunning) {
            long newLastChange = SqlHelper.getLastChange(lobbyId);

            if(newLastChange > currentLastChange) {
                int[] playerIds = null;
                System.out.println("Änderungen");
            } else {
                System.out.println("Keine Änderungen");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
