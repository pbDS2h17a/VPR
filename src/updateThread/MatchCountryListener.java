// Units/country  
//country Owner
package updateThread;

import javafx.application.Platform;
import javafx.scene.control.Label;
import sqlConnection.Lobby;
import sqlConnection.Player;
//import sqlConnection.Player;
import sqlConnection.Country;
import sqlConnection.SqlHelper;

import java.util.ArrayList;

import gui.MatchFX;

/**
 * @author pbs2h17ath
 * Thread für das updaten der Länder im Spiel
 * Der Thread überprüft regelmäßig das last_change flag einer Lobby
 * Wenn der Wert sich änmdert aktualisiert er die Anzeige und seine
 * eigene last_change flag
 * Er vergleicht die aktuellen Country Owner und Units mit den neuen Werten aus der DB
 * und passt seine Daten entsprechend an.
 */
public class MatchCountryListener extends Thread {

    private Lobby lobby;
    //lastChange by default auf 1
    private long currentLastChange = 1;
    private int lobbyId;
    private Country[] currentCountrys;
    private boolean isRunning = false;
    private MatchFX matchFX;

    /**
     * Wird von der Lobby aufgerufen
     * @param lobby Lobby die überwacht werden soll
     */
    public MatchCountryListener(Lobby lobby, MatchFX matchFX) {
        this.lobby = lobby;
        this.lobbyId = lobby.getLobbyId();
        this.matchFX = matchFX;
        this.currentCountrys = matchFX.getCountrys();
    }

    /**
     *  Flag zum Starten/Stoppen des Threads
     * @param isRunning boolean true = läuft, false = angehalten
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }


    @Override
    public void run() {
        // läuft nur wenn isRunnung auf true steht
        while(isRunning) {
            // FLAG lastChange wird aus der DB gelesen
            long newLastChange = SqlHelper.getLastChange(lobbyId);
            if(newLastChange > currentLastChange) {

                for (Country c : currentCountrys) {
                    int dbUnits = SqlHelper.getCountryUnits(c.getCountryId(),lobbyId);

                    if (c.getUnits() != dbUnits) {
                        System.out.println("Änderungen land:"+c.getCountryId());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                // entsprechende UI Komponente updaten
                                final Label label = matchFX.getCountryUnitsLabelArray()[c.getCountryId()-1];
                                c.setUnits(dbUnits);
                                SqlHelper.updateLastChange(lobbyId);
                                label.setText(String.valueOf(dbUnits));
                                try {
                                    this.finalize();
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        });

                    }
                }
                currentLastChange = newLastChange;
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
