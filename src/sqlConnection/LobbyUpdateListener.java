package sqlConnection;

import java.util.ArrayList;
import java.util.Arrays;

public class LobbyUpdateListener extends Thread {

    private Lobby lobby;
    //lastChaneg by default auf 1
    private long currentLastChange = 1;
    private int lobbyId;
    private ArrayList<Integer> currentPlayerIds;
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
            // FLAG lastChange wird aus der DB gelesen
            long newLastChange = SqlHelper.getLastChange(lobbyId);


            // Wir erhalten ein Array welches die IDs aller Spieler enth�hlt,
            // die sich zurzeit in der lobby befinden
            // 1) Neue IDs m�ssen hinzugef�gt werden
            // 2) Ids die nichtmehr vorhanden sind m�ssen entfernt werden
            // Neuer Spieler in Java schreiben bzw entfernen

            // Wenn die DB lastCHange gr��er als der default wert ist
            // gab es eine �nderung in der DB
            if(newLastChange > currentLastChange) {
                // �nderungen bearbeiten
                System.out.println("�nderungen!");
                System.out.println("Aktueller Spieler:");
                for(int playerId : SqlHelper.getPlayerIdsFromLobby(lobbyId)) {
                    if( playerId != 0) {
                        System.out.println("ID:"+playerId);
                    }
                }
                // lastChange aktualiseren
                currentLastChange = newLastChange;
            } else {
                System.out.print(".");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
