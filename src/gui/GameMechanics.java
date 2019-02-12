package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import sqlConnection.Country;
import sqlConnection.Player;

/**
 * Beinhaltet alle Spielmechaniken, die beim Start der Partie ben�tigt werden. 
 * Dinge die w�hrend der Partie passieren werden direkt in die Datenbank gespeist,
 * um sie immer aktuell zu halten.
 * 
 * @author Adrian Ledwinka
 * @author Kevin Daniels
 */
public class GameMechanics {

	// Globale Variablen
	private final ArrayList<Player> playerList;
	private final MatchFX match;
	private Country countryA = null;
	private Country countryB = null;
	private boolean isAssigning = true;
	private boolean isAdding = false;
	private boolean isFighting = false;
	private boolean isMoving = false;
	private int activePlayerIndex;
	private int battleUnitsA;
	private int battleUnitsB;
	private int additionalAttacker;

	/**
	 * Konstruktor, der die Spielerliste der Lobby bekommt und mit den Match-Daten die 
	 * genau richtige Partie erstellt, zugeschnitten auf die Spieler.
	 * 
	 * @param match MatchFX
	 * @param playerList ArrayList<Player>
	 */
	public GameMechanics(MatchFX match, ArrayList<Player> playerList) {
		// Setzt den Aktuellen Spieler zur�ck
		this.activePlayerIndex = 0;
		// F�llt die Klasse mit Daten
		this.playerList = playerList;
		this.match = match;
		// Startet die 0. Phase (einzelnes Setzen)
		this.startInitialRound(playerList);
	}
	
	/**
	 * Bei einem Klick auf auf das Land wird der Klick abh�ngig
	 * von der Phase unterschiedlich behandelt.
	 *
	 * @param index int
	 */
	public void manageCountryClick(int index) {
		// Wenn man in der 0. Phase (einzeln setzen) steckt...
		if(isAssigning()) {
			// Wenn einem das Land geh�rt...
			if(isOwnLand(match.getCountryArray()[index])) {
				// ...werden die ungesetzten Einheiten um eins reduziert
				getActivePlayer().setUnassignedUnits(getActivePlayer().getUnassignedUnits() - 1);
				// ...bekommt das Land eine Einheit dazu
				match.getCountryArray()[index].setUnits(match.getCountryArray()[index].getUnits() + 1);
				match.getCountryUnitsLabelArray()[index].setText(String.valueOf(match.getCountryArray()[index].getUnits()));
				
				// Wenn man am Ende der Spieler-Liste angekommen ist...
				if(getActivePlayerIndex() == getPlayerList().size() -1) {
					// ...ist der 1. Spieler wieder der aktive Spieler
					setActivePlayerIndex(0);
				} else {
					// sonst wird der n�chste Spieler der aktive Spieler
					setActivePlayerIndex(getActivePlayerIndex() + 1);
				}
			}
		}
		
		// Wenn man in der 1. Phase (setzen) steckt...
		else if(isAdding()) {
			// Wenn einem das Land geh�rt und noch nicht unverteilte Einheiten im Inventar sind...
			if(isOwnLand(match.getCountryArray()[index]) && getActivePlayer().getUnassignedUnits() > 0) {
				// ...wird ein Spieler aus dem Inventar verschoben...
				getActivePlayer().setUnassignedUnits(getActivePlayer().getUnassignedUnits() - 1);
				// ...und dem Land hinzugef�gt 
				match.getCountryArray()[index].setUnits(match.getCountryArray()[index].getUnits() + 1);
				match.getCountryUnitsLabelArray()[index].setText(String.valueOf(match.getCountryArray()[index].getUnits()));
			}
		}
		
		// Wenn man in der 2. Phase (k�mpfen) steckt...
		else if(isFighting()) {
			// Wenn noch nicht das erste Land ausgew�hlt wurde...
			if(getCountryA() == null) {
				// Wenn es das eigene Land ist und sich mehr als eine Einheit darauf befindet...
				if(isOwnLand(match.getCountryArray()[index]) && match.getCountryArray()[index].getUnits() > 1) {
					// ...wird das erste Land ausgew�hlt
					setCountryA(match.getCountryArray()[index]);
				}
			}
			
			// Sonst wenn es nicht das eigene Land ist und benachbart ist...
			else if(!isOwnLand(match.getCountryArray()[index]) && isNeighbour(getCountryA(), match.getCountryArray()[index])) {
				// ...wird das zweite Land ausgew�hlt und der Kampf gestartet
				setCountryB(match.getCountryArray()[index]);
				match.setFightStarting(true);
				startFight();
			}
		}
		
		// Wenn man in der 3. Phase (bewegen) steckt...
		else if(isMoving()) {
			// Wenn es das eigene Land ist...
			if(isOwnLand(match.getCountryArray()[index])) {
				// Wenn noch nicht das erste Land ausgew�hlt wurde...
				if(getCountryA() == null) {
					// ...wird das erste Land ausgew�hlt
					setCountryA(match.getCountryArray()[index]);
				}
				
				// Sonst...
				else {
					// ...wird das zweite Land ausgew�hlt
					setCountryB(match.getCountryArray()[index]);
					// ...Wenn das Land benachbart ist und das erste Land mehr als eine Einheit beinhaltet
					if(isNeighbour(getCountryA(), getCountryB()) && getCountryA().getUnits() > 1) {
						// ...wird eine Einheit vom ersten Land zum zweiten Land verschoben
						getCountryA().setUnits(getCountryA().getUnits() - 1);
						match.getCountryUnitsLabelArray()[getCountryA().getCountryId()-1].setText(String.valueOf(getCountryA().getUnits()));
						getCountryB().setUnits(getCountryB().getUnits() + 1);
						match.getCountryUnitsLabelArray()[getCountryB().getCountryId()-1].setText(String.valueOf(getCountryB().getUnits()));
					}
					// ...werden das erste und zweite Land zur�ckgesetzt
					setCountryA(null);
					setCountryB(null);
				}	
			}
		}
		
		// Wenn man alle Einheiten in der 0. Phase (einzeln setzen) verteilt hat...
		if(isAssigning() && isFinishedAssigning()) {
			// ...erh�lt der erste Spieler seine Einheiten pro Runde
			getActivePlayer().setUnassignedUnits(getActivePlayer().getUnassignedUnits() + getActivePlayer().getUnitsPerRound());
			// ...wird die 0. Phase beendet und die 1. Phase begonnen
			this.isAssigning = false;
			this.isAdding = true;
			phaseAdd();
		}
		
		// Aktualisiert das Interface
		updatePlayerInterface(getActivePlayer());
		match.updateCountryInfo(match.getCountryArray()[index]);
	}

	/**
	 * Startet die 0. Phase mit der aktuellen Spielerliste, 
	 * die ein einzelnes Setzen einer Einheit pro Spieler erm�glicht.
	 * 
	 * @param playerArray ArrayList<Player>
	 */
	private void startInitialRound(ArrayList<Player> playerArray) {
			
		int firstUnits;

//		for (Player p : playerArray) {
//			System.out.println(p.toString());
//		}

		/*
		 * Je nach Anzahl der Spieler die an der Partie teilnehmen, 
		 * werden die zu Beginn erhaltenen Einheiten reguliert.
		 */
		switch (playerArray.size()) {
			case 2:
				firstUnits = 40;
				break;
				
			case 3:
				firstUnits = 35;
				break;
				
			case 4:
				firstUnits = 30;
				break;
				
			case 5:
				firstUnits = 25;
				break;
				
			case 6:
				firstUnits = 20;
				break;
			
			default:
				firstUnits = 20;
		}
		
		// Setzt die Einheiten auf 1 aus TESTZWECKEN
		firstUnits = 1;
		
		// Verteilt diese Anzahl Einheiten auf alle Spieler
		for (int i = 0; i < this.getPlayerList().size(); i++) {
			this.getPlayerList().get(i).setUnassignedUnits(firstUnits);
		}
		
		// das Interface wird aktualisiert mit dem Inventar des aktuellen Spielers
		updatePlayerInterface(this.getActivePlayer());

	}
	
	/**
	 * Methode, die kontrolliert, ob sich noch ungesetzte Einheiten im Inventar befinden.
	 * 
	 * @return ein true/false-Wert ob alle Spieler fertig mit setzen sind
	 */
	private boolean isFinishedAssigning() {
		// Schleife die alle Spieler kontrolliet
		for (int i = 0; i < this.getPlayerList().size(); i++) {
			// Wenn der Spieler noch ungesetzte Einheiten hat...
			if(this.getPlayerList().get(i).getUnassignedUnits() != 0) {
				// ... wird sofort false ausgegeben
				return false;
			}
		}
		
		// Sonst wird regul�r true ausgegeben
		return true;
	}
	
	/**
	 * Prozedur, die die 1. Phase (setzen) startet
	 */
	public void phaseAdd() {
		// Startet die 1. Phase und deaktiviert alle anderen
		this.isAdding = true;
		this.isFighting = false;
		this.isMoving = false;
		this.match.editPhaseButtons(false, true, true, true);
		// Aktualisiert das Spieler-Interface
		updatePlayerInterface(this.getActivePlayer());
	}

	/**
	 * Prozedur, die die 2. Phase (k�mpfen) startet
	 */
	public void phaseFight() {
		// Startet die 2. Phase und deaktiviert alle anderen
		this.isAdding = false;
		this.isFighting = true;
		this.isMoving = false;
		this.match.editPhaseButtons(false, false, true, true);
		// Aktualisiert das Spieler-Interface
		updatePlayerInterface(this.getActivePlayer());
	}
	
	/**
	 * Prozedur, die die 3. Phase (bewegen) startet
	 */
	public void phaseMove() {
		// Startet die 3. Phase und deaktiviert alle anderen
		this.isAdding = false;
		this.isFighting = false;
		this.isMoving = true;
		this.match.editPhaseButtons(false, false, false, true);
		// Aktualisiert das Spieler-Interface
		updatePlayerInterface(this.getActivePlayer());
	}
	
	/**
	 * Prozedur, die die Runde beendet und den n�chsten Spieler am Zug l�sst
	 */
	public void nextTurn() {
		// Wenn der aktive Spieler am Ende der Spielerliste angekommen ist...
		if(this.getActivePlayerIndex() == this.getPlayerList().size() -1) {
			// ...ist der erste Spieler wieder am Zug
			this.setActivePlayerIndex(0);
		}
		
		// Sonst...
		else {
			// ...ist der n�chste Spieler am Zug
			this.setActivePlayerIndex(this.getActivePlayerIndex() + 1);
		}
		
		// Gibt dem aktuellen Spieler die Einheiten, die er pro Runde bekommen w�rde dazu
		this.getActivePlayer().setUnassignedUnits(this.getActivePlayer().getUnassignedUnits() + this.getActivePlayer().getUnitsPerRound());
		// Startet wieder die 1. Phase
		phaseAdd();
	}

	/**
	 * Prozedur, die einen Kampf beginnt, wenn zwei passende L�nder ausgew�hlt wurden
	 */
	private void startFight() {
		// ordnet den Lambda Ausdruck zu
		UnaryOperator<Change> integerFilter = change -> {
			// der "neue" (tempor�re) String wird gesetzt
		    String newText = change.getControlNewText();
		    //pr�ft ob der neue String am Anfang eine Ziffer, welche nicht 0 sein darf, hat und ob der gesammte String nicht 99 �berschreitet
		    if (newText.matches("-?([1-9][0-9]*)?")) {
		    	//dann wird der neue String in das Textlabel gesetzt
		        return change;
		    }
		    // sonst wird nix ge�ndert
		    return null;
		};
		
		// Gibt beiden Eingabefeldern schon den Minimal-Wert
		this.match.getBattleInputA().setText("1");
		this.match.getBattleInputB().setText("1");
		// Beschr�nkt das Eingabefeld A auf zwei Zeichen (max. 99)
		MainApp.addTextLimiter(this.match.getBattleInputA(), 2);
		// f�gt den Lambdaausdruck in A hinzu
		this.match.getBattleInputA().setTextFormatter(
				new TextFormatter<Integer>(new IntegerStringConverter(), 1, integerFilter)
	    );
		// Beschr�nkt das Eingabefeld A auf zwei Zeichen (max. 99)
		MainApp.addTextLimiter(this.match.getBattleInputB(), 2);
		// f�gt den Lambdaausdruck in B hinzu
		this.match.getBattleInputB().setTextFormatter(
				new TextFormatter<Integer>(new IntegerStringConverter(), 1, integerFilter)
	    );
		
		// Deaktiviert Eingabefeld B und aktiviert A
		this.match.getBattleInputA().setDisable(false);
		this.match.getBattleInputB().setDisable(true);
		// F�llt die linke Seite des Kampfbildschirms mit Informationen von Land A
		this.match.getBattleBackgroundA().setFill(this.countryA.getFill());
		this.match.getCountryNameA().setText("Angreifer\n" + this.countryA.getCountryName());
		this.match.getCountryUnitsA().setText("/ " + (this.countryA.getUnits()-1));
		// F�llt die rechte Seite des Kampfbildschirms mit Informationen von Land B
		this.match.getBattleBackgroundB().setFill(this.countryB.getFill());
		this.match.getCountryNameB().setText("Verteidiger\n" + this.countryB.getCountryName());
		this.match.getCountryUnitsB().setText("/ " + this.countryB.getUnits());
		
		// Deaktiviert die Weltkarte tempor�r
		this.match.activateWorldMap(false);
		// Versteckt die Phasen-Buttons
		this.match.getPhaseBtnGroup().setVisible(false);
		// Zeigt den modifizierten Kampfbildschirm an
		this.match.getBattleInterface().setVisible(true);
	}

	/**
	 * Prozedur, die das Ergebnis des Kampfes verarbeitet und die 
	 * Konsequenzen weitergibt
	 * 
	 * @param rolledDices Integer[][]
	 * @param countryAttack Country
	 * @param countryDefense Country
	 */
	public void updateFightResults(Integer[][] rolledDices, Country countryAttack, Country countryDefense) {

		// F�gt die W�rfel von Land A in eine Liste ein
		List<Integer> diceListA = new ArrayList<Integer>();
		diceListA.addAll(Arrays.asList(rolledDices[0]));
		// F�gt die W�rfel von Land B in eine Liste ein
		List<Integer> diceListB = new ArrayList<Integer>();
		diceListB.addAll(Arrays.asList(rolledDices[1]));
		
		/*
		 * Variablen, die die verlorenen Einheiten speichert
		 * und auch das Limit f�r die Schleife festlegt
		 */
		int lostUnitsA = 0;
		int lostUnitsB = 0;
		int limit = diceListA.size();
		
		// Wenn mehr W�rfel in A geworfen wurden als in B...
		if(diceListA.size() > diceListB.size()) {
			// ...ist die Anzahl von B das neue Limit
			limit = diceListB.size();
		}
		
		// Schleife, die den W�rfel-Vergleich durchf�hrt
		for (int i = 0; i < limit; i++) {
			// Wenn das W�rfel-Ergebnis von A gr��er als das von B ist...
			if(diceListA.get(i) > diceListB.get(i)) {
				// ...verliert B eine Einheit
				lostUnitsB++;
			}
			// Sonst...
			else {
				// ...verliert A eine Einheit
				lostUnitsA++;
			}
		}
		
		// Gibt die verlorenen Einheiten in der Konsole aus
		System.out.println("A verlorene Einheiten: " + lostUnitsA);
		System.out.println("B verlorene Einheiten: " + lostUnitsB);
		
		// Speichert das Kampfergebnis in Arrays
		int[] fightA = {diceListA.size(), lostUnitsA};
		int[] fightB = {diceListB.size(), lostUnitsB};
		
		// Setzt die W�rfel von A zur�ck
		for (int i = 0; i < this.match.getDicesA().getChildren().size(); i++) {
			Label tmpLabel = (Label) this.match.getDicesA().getChildren().get(i);
			tmpLabel.setText("");
		}
		
		// Setzt die W�rfel von B zur�ck
		for (int i = 0; i < this.match.getDicesB().getChildren().size(); i++) {
			Label tmpLabel = (Label) this.match.getDicesB().getChildren().get(i);
			tmpLabel.setText("");
		}
		
		// Setzt die Werte in die W�rfel von A
		for (int i = 0; i < diceListA.size(); i++) {
			Label tmpLabel = (Label) this.match.getDicesA().getChildren().get(i);
			tmpLabel.setText(String.valueOf(diceListA.get(i)));
		}
		
		// Setzt die Werte in die W�rfel von B
		for (int i = 0; i < diceListB.size(); i++) {
			Label tmpLabel = (Label) this.match.getDicesB().getChildren().get(i);
			tmpLabel.setText(String.valueOf(diceListB.get(i)));
		}
		
		// Startet die Prozedur, die die Konsequenzen des Kampfes auswertet
		countryAftermath(fightA, fightB, countryAttack, countryDefense);
	}

	/**
	 * Prozedur, die die Konsequenzen des Kampfes auswertet
	 * 
	 * @param fightA int[]
	 * @param fightB int[]
	 * @param cAtk Country
	 * @param cDef Country
	 */
	private void countryAftermath(int[] fightA, int[] fightB, Country cAtk, Country cDef) {
		// Wenn alle Einheiten in Land B besiegt wurden...
		if(cDef.getUnits() - fightB[1] == 0) {
			// ...wird das Land erobert und erh�lt die Informationen des Eroberers
			cDef.setUnits(fightA[0] - fightA[1] + additionalAttacker);
			cDef.setOwner(cAtk.getOwner());
			cDef.setFill(cAtk.getFill());
			// ... werden alle eingesetzten Einheiten von Land A abgezogen
			cAtk.setUnits(cAtk.getUnits() - fightA[0] - additionalAttacker);
			// ...werden die Einheiten auf der Weltkarte angepasst
			match.getCountryUnitsBGArray()[cDef.getCountryId()-1].setFill(cDef.getFill());
			match.getCountryUnitsLabelArray()[cDef.getCountryId()-1].setText(String.valueOf(cDef.getUnits()));
			match.getCountryUnitsBGArray()[cAtk.getCountryId()-1].setFill(cAtk.getFill());
			match.getCountryUnitsLabelArray()[cAtk.getCountryId()-1].setText(String.valueOf(cAtk.getUnits()));
		}
		// Sonst...
		else {
			// ... werden die gestorbenen Einheiten in beiden L�ndern abgezogen
			cAtk.setUnits(cAtk.getUnits() - fightA[1]);
			cDef.setUnits(cDef.getUnits() - fightB[1]);
			// ...werden die Einheiten auf der Weltkarte angepasst
			match.getCountryUnitsLabelArray()[cDef.getCountryId()-1].setText(String.valueOf(cDef.getUnits()));
			match.getCountryUnitsLabelArray()[cAtk.getCountryId()-1].setText(String.valueOf(cAtk.getUnits()));

		}
		
		// Differenz der Einheiten von Land A, die nicht gestorben sind
		additionalAttacker = 0;
		
		// Gibt die Einheiten der L�nder nach dem Kampf aus
		System.out.println("A Einheiten nachher: " + cAtk.getUnits());
		System.out.println("B Einheiten nachher: " + cDef.getUnits());
		System.out.println();

		// Debug Ausgabe
//		for (Player p : playerList) {
//			System.out.println(p);
//		}
		
	}

	/**
	 * Prozedur, die den Kampf beendet
	 */
	public void endFight() {
		// Setzte beide gew�hlten L�nder zur�ck
		this.countryA = null;
		this.countryB = null;
		// Aktiviert die Weltkarte und zeigt alle Phasen-Buttons wieder an
		this.match.getBattleReadyBtn().setActive(true);
		this.match.activateWorldMap(true);
		this.match.getFightTextGroup().setVisible(false);
		this.match.getBattleBackgroundA().relocate(-960, 0);
		this.match.getBattleBackgroundB().relocate(1920, 0);
		this.match.getDicesA().relocate(-400, this.match.getContainer().getPrefHeight()/2 - 515/2);
		this.match.getDicesB().relocate(2320, this.match.getContainer().getPrefHeight()/2 - 415/2);
		this.match.getPhaseBtnGroup().setVisible(true);
		// Setzt den Kampfbildschirm zur�ck
		this.match.getBattleInterface().setVisible(false);
		this.match.getBattleBackgroundA().relocate(-960, 0);
		this.match.getBattleBackgroundB().relocate(1920, 0);
		// Aktualisiert das Interface mit dem aktiven Spieler
		updatePlayerInterface(this.getActivePlayer());
	}

	/**
	 * Methode, die anhand der eingesetzten Einheiten W�rfel w�rfelt
	 * 
	 * @param battleUnitsA int
	 * @param battleUnitsB int
	 * 
	 * @return gibt ein 2D Array mit den W�rfen zur�ck
	 */
	public Integer[][] rollTheDice(int battleUnitsA, int battleUnitsB) {
		// Speichert die Anzahl der zu w�rfelnden W�rfel
		int limitA = battleUnitsA;
		int limitB = battleUnitsB;
		additionalAttacker = 0;
		
		// Wenn mehr als 3 Einheiten in B eingesetzt werden...
		if(battleUnitsA > 3) {
			// ...werden die W�rfel von A auf drei gesetzt
			limitA = 3;
			// Differenz an Einheiten werden f�r den sp�teren Kampf gespeichert
			additionalAttacker = battleUnitsA - 3;
		}
		
		// Wenn mehr als zwei Einheiten in Land B eingesetzt werden...
		if(battleUnitsB > 2) {
			// ...werden die W�rfel von B auf zwei gesetzt
			limitB = 2;
		}
		
		// Array mit den W�rfel-Ergebnissen werden erstellt
		Integer[] dicesA = new Integer[limitA];
		Integer[] dicesB = new Integer[limitB];
		
		// W�rfelt die W�rfel f�r Land A
		for (int i = 0; i < dicesA.length; i++) {
			dicesA[i] = randomInt();
		}
		// W�rfelt die W�rfel f�r Land B
		for (int i = 0; i < dicesB.length; i++) {
			dicesB[i] = randomInt();
		}
		
		// Sortiert die W�rfel von beiden L�ndern vom gr��ten zum kleinsten
		Arrays.sort(dicesA, Collections.reverseOrder());
		Arrays.sort(dicesB, Collections.reverseOrder());
		
		// Gibt die W�rfelergebnisse in der Konsole aus
		System.out.print("A w�rfelte: ");
		for (int i = 0; i < dicesA.length; i++) {
			System.out.print(dicesA[i] + ", ");
		}
		System.out.println();
		
		System.out.print("B w�rfelte: ");
		for (int i = 0; i < dicesB.length; i++) {
			System.out.print(dicesB[i] + ", ");
		}
		System.out.println();
		
		// Speichert die Ergebnisse in ein 2D Array

		// Gibt das 2D Array zur�ck
		return new Integer[][]{dicesA, dicesB};
	}

	/**
	 * Methode, die kontrolliert, ob beide L�nder miteinander benachbart sind
	 * 
	 * @param a Country
	 * @param b Country
	 * 
	 * @return gibt den true/false-Wert zur�ck
	 */
	private boolean isNeighbour(Country a, Country b) {
		return IntStream.of(a.getNeighborIdArray()).anyMatch(x -> x == b.getCountryId());
	}
		
	/**
	 * Methode, die kontrolliert, ob das Land dem aktuellen Spieler geh�rt
	 * 
	 * @param country Country
	 * 
	 * @return gibt den true/false-Wert zur�ck
	 */
	private boolean isOwnLand(Country country) {
        return this.getActivePlayer().equals(country.getOwner());

    }

	/**
	 * Prozedur, die das Spieler-Interface anhand des aktuellen Spielers aktualisiert
	 * 
	 * @param activePlayer Player
	 */
	private void updatePlayerInterface(Player activePlayer) {
		this.match.updateActivePlayer(activePlayer.getName(), Color.web(activePlayer.getColorValue()));
		this.match.setInventoryCountryLabel(activePlayer.getCountryList().size());
		this.match.setInventoryUnitsLabel(activePlayer.getUnassignedUnits());
	}
	
	/**
	 * Methode, die den aktuellen Spieler holt
	 * 
	 * @return gibt das Player-Objekt zur�ck
	 */
	private Player getActivePlayer() {
		return playerList.get(activePlayerIndex);
	}

	/**
	 * Prozedur, die den Index f�r den aktuellen Spieler setzt
	 * 
	 * @param i int
	 */
	private void setActivePlayerIndex(int i) {
		this.activePlayerIndex = i;
	}

	/**
	 * Methode, die den Index des aktuellen Spielers ausgibt
	 * 
	 * @return gibt den Index zur�ck
	 */
	private int getActivePlayerIndex() {
		return this.activePlayerIndex;
	}
	
	/**
	 * Methode, die die aktuelle Spielerliste ausgibt
	 * 
	 * @return gibt die ArrayList zur�ck
	 */
	private ArrayList<Player> getPlayerList() {
		return this.playerList;
	}

	/**
	 * Methode, die kontrolliert, ob man noch in der 0. Phase ist
	 * 
	 * @return gibt den true/false-Wert zur�ck
	 */
	private boolean isAssigning() {
		return isAssigning;
	}
	
	/**
	 * Methode, die kontrolliert, ob man noch in der 1. Phase ist
	 * 
	 * @return gibt den true/false-Wert zur�ck
	 */
	private boolean isAdding() {
		return isAdding;
	}

	/**
	 * Methode, die kontrolliert, ob man noch in der 2. Phase ist
	 * 
	 * @return gibt den true/false-Wert zur�ck
	 */
	private boolean isFighting() {
		return isFighting;
	}

	/**
	 * Methode, die kontrolliert, ob man noch in der 3. Phase ist
	 * 
	 * @return gibt den true/false-Wert zur�ck
	 */
	private boolean isMoving() {
		return isMoving;
	}

	/**
	 * Methode, die das Land A ausgibt
	 * 
	 * @return gibt das Country-Objekt zur�ck
	 */
	public Country getCountryA() {
		return countryA;
	}

	/**
	 * Prozedur, die das Land A neu setzt
	 * 
	 * @param countryA Country
	 */
	private void setCountryA(Country countryA) {
		this.countryA = countryA;
	}

	/**
	 * Methode, die das Land B ausgibt
	 * 
	 * @return gibt das Country-Objekt zur�ck
	 */
	public Country getCountryB() {
		return countryB;
	}

	/**
	 * Prozedur, die das Land B neu setzt
	 * 
	 * @param countryB Country
	 */
	private void setCountryB(Country countryB) {
		this.countryB = countryB;
	}

	/**
	 * Methode, die die k�mpfenden Einheiten von Land A ausgibt
	 * 
	 * @return gibt die Anzahl zur�ck
	 */
	public int getBattleUnitsA() {
		return battleUnitsA;
	}
	
	/**
	 * Prozedur, die die k�mpfenden Einheiten von Land A setzt
	 * 
	 * @param battleUnitsA int
	 */
	public void setBattleUnitsA(int battleUnitsA) {
		this.battleUnitsA = battleUnitsA;
	}

	/**
	 * Methode, die die k�mpfenden Einheiten von Land B ausgibt
	 * 
	 * @return gibt die Anzahl zur�ck
	 */
	public int getBattleUnitsB() {
		return battleUnitsB;
	}
	
	/**
	 * Prozedur, die die k�mpfenden Einheiten von Land B setzt
	 * 
	 * @param battleUnitsB int
	 */
	public void setBattleUnitsB(int battleUnitsB) {
		this.battleUnitsB = battleUnitsB;
	}

	/**
	 * Methode, die eine zuf�llige Ganzzahl ausgibt
	 * 
	 * @return gibt eine Zahl zwischen min und max zur�ck (inklusive)
	 */
	private static int randomInt() {
	    return (int)(Math.random() * (6 - 1 + 1)) + 1;
	}
	
}
