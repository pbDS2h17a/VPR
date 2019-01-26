package sqlConnection;

import java.util.ArrayList;

/**
 * @author pbs2h17awb
 * UpdateListener f�r das beitreten der Lobby
 * Der Thread �berpr�ft regelm��ig das last_change flag einer Lobby
 * Wenn der Wert sich �nmdert aktualisiert er die Anzeige und seine
 * eigene last_change flag
 */
public class LobbyUpdateListener extends Thread {

    private Lobby lobby;
    //lastChaneg by default auf 1
    private long currentLastChange = 1;
    private int lobbyId;
    private ArrayList<Integer> currentPlayerIds;
    private boolean isRunning = false;

    /**
     * Wird von der Lobby aufgerufen
     * @param lobby Lobby die �berwacht werden soll
     */
    public LobbyUpdateListener(Lobby lobby) {
        this.lobby = lobby;
        this.lobbyId = lobby.getLobbyId();
        this.currentPlayerIds = SqlHelper.getPlayerIdsFromLobby(lobbyId);
    }

    /**
     *  Flag zum Starten/Stoppen des Threads
     * @param isRunning boolean true = l�uft, false = angehalten
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        // l�uft nur wenn isRunnung auf true steht
        while(isRunning) {
            // FLAG lastChange wird aus der DB gelesen
            long newLastChange = SqlHelper.getLastChange(lobbyId);

            // Wir erhalten eine Liste welches die IDs aller Spieler enth�hlt,
            // die sich zurzeit in der lobby befinden
            // 1) Neue IDs m�ssen hinzugef�gt werden
            // 2) Ids die nichtmehr vorhanden sind m�ssen entfernt werden
            // Neuer Spieler in Java schreiben bzw entfernen

            // Wenn die DB lastCHange gr��er als der default wert ist
            // gab es eine �nderung in der DB
            if(newLastChange > currentLastChange) {
                ArrayList<Integer> newPlayerIds = SqlHelper.getPlayerIdsFromLobby(lobbyId);
                // �nderungen bearbeiten
                System.out.println("�nderungen!");
                System.out.println("Aktueller Spieler:");

                for(int playerId :newPlayerIds) {
                    if(currentPlayerIds.contains(playerId)) {
                        System.out.println("ID:"+playerId);
                    } else {
                        System.out.println("#NEU ID:"+playerId);
                    }

                }
                // lastChange aktualiseren
                currentLastChange = newLastChange;
                currentPlayerIds = newPlayerIds;
            } else {
                System.out.print(".");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
