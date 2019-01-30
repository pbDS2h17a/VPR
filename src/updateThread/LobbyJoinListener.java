package updateThread;

import sqlConnection.Lobby;
import sqlConnection.Player;
import sqlConnection.SqlHelper;

import java.util.ArrayList;

/**
 * @author pbs2h17awb
 * Thread f�r das beitreten der Lobby
 * Der Thread �berpr�ft regelm��ig das last_change flag einer Lobby
 * Wenn der Wert sich �nmdert aktualisiert er die Anzeige und seine
 * eigene last_change flag
 * Er vergleicht die aktuellen Spieler einer Lobby mit den neuen Werten aus der DB
 * und passt seine Daten entsprechend an (L�scht oder f�gt einen Spieler hinzu)
 */
public class LobbyJoinListener extends Thread {

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
    public LobbyJoinListener(Lobby lobby) {
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

            // Wenn die DB lastChange gr��er als der aktuelle Wert ist
            // gab es eine �nderung in der DB
            if(newLastChange > currentLastChange) {
                // Ids aller Spieler die sich laut Datenbank in der Lobby befinden sollten
                ArrayList<Integer> newPlayerIds = SqlHelper.getPlayerIdsFromLobby(lobbyId);
                // �nderungen bearbeiten
                for(int playerId : newPlayerIds) {
                    // Spieler die sich bereits vorher in der Lobby befanden werden ignoriert
                    if(!currentPlayerIds.contains(playerId)) {
                        // Es wird ein neues Spielerobjekt basierend auf den Datenbank daten erstellt
                        // Beim erstellen eines neuen Spielers wird er automatisch der Lobby hinzugef�gt
                        // Datenbankloser Konstruktor da die Db eintr�ge bereits vorhanden sind
                        //new Player(playerId, SqlHelper.getPlayerName(playerId), lobby);
                    }

                }

                System.out.println(lobby);
                // lastChange aktualiseren
                currentLastChange = newLastChange;
                currentPlayerIds = newPlayerIds;
            } else {
                System.out.print(".");
            }


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
