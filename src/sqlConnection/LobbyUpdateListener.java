package sqlConnection;

import java.util.Arrays;

public class LobbyUpdateListener extends Thread {

    private Lobby lobby;
    private long currentLastChange = 1;
    private int lobbyId;
    private int[] currentPlayerIds;
    private boolean isRunning = false;

    public LobbyUpdateListener(Lobby lobby) {
        this.lobby = lobby;
        this.lobbyId = lobby.getLobbyId();
        this.currentPlayerIds = SqlHelper.getPlayerIdsFromLobby(lobbyId);
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        while(isRunning) {
            long newLastChange = SqlHelper.getLastChange(lobbyId);


            // Wir erhalten ein Array welches die IDs aller Spieler enth�hlt,
            // die sich zurzeit in der lobby befinden

            System.out.println("Aktueller Spieler:");
            for(int playerId : currentPlayerIds) {
                if( playerId != 0) {
                    System.out.println("ID:"+playerId);
                }
            }

            // 1) Neue IDs m�ssen hinzugef�gt werden
            // 2) Ids die nichtmehr vorhanden sind m�ssen entfernt werden
            // Neuer Spieler in Java schreiben bzw entfernen
            if(newLastChange > currentLastChange) {
                System.out.println("�nderungen");
                currentLastChange = newLastChange;
            } else {
                System.out.println("Keine �nderungen");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
