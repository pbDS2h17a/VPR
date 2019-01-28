// Units/country  
//country Owner
package updateThread;

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

    /**
     * Wird von der Lobby aufgerufen
     * @param lobby Lobby die überwacht werden soll
     */
    public MatchCountryListener(Lobby lobby, MatchFX mfx) {
        this.lobby = lobby;
        this.lobbyId = lobby.getLobbyId();
        this.currentCountrys = mfx.getCountrys();
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
                // Ids aller Spieler die sich laut Datenbank in der Lobby befinden sollte
                // Änderungen bearbeiten
                for(Country country : currentCountrys) {
                    // Spieler die sich bereits vorher in der Lobby befanden werden ignoriert
                	//int units = SqlHelper.getCountryUnits(country.getCountryId(), lobby.getLobbyId());
                	//Player owner = SqlHelper.getCountyOwner(country.getCountryId(), lobby);
                	
                	System.out.printf("Java units:%d | DB units %d\n",country.getUnits(), 	0);
                	
//                	if(country.getUnits() != units){
//                		// Units changed
//                		country.setUnits(units);
//                		System.out.println("Change in Units");
//                	}
                	/*
                	if(country.getOwner() != owner){
                		// Owner changed
                		country.setOwner(owner);
                		System.out.println("Owner changed");
                	}    */    
                	
                	//System.out.println("Name:"+country.getOwner().getName());
                	//System.out.println("Units:" +country.getUnits());
                }

                System.out.println("Aktuallisiert:");
                // lastChange aktualiseren
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
